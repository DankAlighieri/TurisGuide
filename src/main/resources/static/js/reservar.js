let currentService = null;
let allServices = [];

document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const serviceId = params.get('serviceId');
    const user = JSON.parse(localStorage.getItem('user'));

    if (!user) {
        alert('Você precisa estar logado para reservar.');
        window.location.href = 'login.html?redirect=reservar.html?serviceId=' + serviceId;
        return;
    }

    // Definir data mínima como hoje
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').min = today;
    document.getElementById('endDate').min = today;

    // Carregar serviço e sugestões
    loadService(serviceId);
    setupEventListeners();
});

function loadService(serviceId) {
    fetch('/api/services')
        .then(res => res.json())
        .then(services => {
            allServices = services;
            currentService = services.find(s => s.id == serviceId);
            
            if (!currentService) {
                alert('Serviço não encontrado');
                window.location.href = 'busca.html';
                return;
            }
            
            renderService(currentService);
            loadSuggestions(services, currentService);
            updateSummary();
        })
        .catch(err => {
            console.error('Erro ao carregar serviço:', err);
            alert('Erro ao carregar informações do serviço');
        });
}

function renderService(service) {
    document.getElementById('serviceTitle').textContent = service.name;
    document.getElementById('serviceType').textContent = getTypeLabel(service.type);
    document.getElementById('serviceLocation').textContent = service.location;
    document.getElementById('serviceCapacity').textContent = `Até ${service.capacity} pessoas`;
    document.getElementById('serviceDescription').textContent = service.description;
    document.getElementById('pricePerPerson').textContent = `R$ ${service.price.toFixed(2)}`;
    document.getElementById('summaryService').textContent = service.name;
    
    // Atualizar imagem principal se disponível
    if (service.imageUrl) {
        document.getElementById('mainImage').src = service.imageUrl;
    }
    
    // Atualizar capacidade máxima
    document.getElementById('guests').max = service.capacity;
}

function loadSuggestions(services, current) {
    const container = document.getElementById('suggestionsList');
    
    // Sugerir serviços complementares (tipo diferente, mesma localização)
    const targetType = current.type === 'HOSPEDAGEM' ? 'PASSEIO' : 'HOSPEDAGEM';
    
    const suggestions = services.filter(s => 
        s.id !== current.id &&
        (s.type === targetType || s.location.includes(current.location.split(',')[0]))
    ).slice(0, 3);

    if (suggestions.length === 0) {
        container.innerHTML = '<p style="color: #999; font-size: 0.9rem;">Sem sugestões disponíveis</p>';
        return;
    }

    container.innerHTML = suggestions.map(s => `
        <div class="suggestion-card" onclick="window.location.href='reservar.html?serviceId=${s.id}'">
            <strong>${s.name}</strong>
            <small>R$ ${s.price.toFixed(2)}</small>
        </div>
    `).join('');
}

function setupEventListeners() {
    // Atualizar resumo quando dados mudarem
    document.getElementById('startDate').addEventListener('change', updateSummary);
    document.getElementById('endDate').addEventListener('change', updateSummary);
    document.getElementById('guests').addEventListener('input', updateSummary);
    
    // Mostrar/ocultar campos de cartão
    document.getElementById('paymentMethod').addEventListener('change', function(e) {
        const cardFields = document.getElementById('cardFields');
        const showCard = e.target.value === 'CREDITO' || e.target.value === 'DEBITO';
        cardFields.style.display = showCard ? 'block' : 'none';
        
        // Atualizar total com desconto PIX
        updateSummary();
    });
    
    // Formatação de campos de cartão
    document.getElementById('cardNumber')?.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\s/g, '');
        let formatted = value.match(/.{1,4}/g)?.join(' ') || value;
        e.target.value = formatted;
    });
    
    document.getElementById('cardExpiry')?.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.slice(0, 2) + '/' + value.slice(2, 4);
        }
        e.target.value = value;
    });
    
    // Submissão do formulário
    document.getElementById('bookingForm').addEventListener('submit', handleSubmit);
}

function updateSummary() {
    if (!currentService) return;
    
    const guests = parseInt(document.getElementById('guests').value) || 1;
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const paymentMethod = document.getElementById('paymentMethod').value;
    
    // Calcular subtotal
    let subtotal = currentService.price * guests;
    
    // Aplicar desconto PIX (5%)
    let discount = 0;
    if (paymentMethod === 'PIX') {
        discount = subtotal * 0.05;
    }
    
    const total = subtotal - discount;
    
    // Atualizar interface
    document.getElementById('summaryGuests').textContent = guests;
    document.getElementById('summarySubtotal').textContent = `R$ ${subtotal.toFixed(2)}`;
    document.getElementById('totalPrice').textContent = `R$ ${total.toFixed(2)}`;
    
    // Atualizar data no resumo
    if (startDate) {
        const dateFormatted = new Date(startDate + 'T00:00:00').toLocaleDateString('pt-BR');
        const endFormatted = endDate ? ' - ' + new Date(endDate + 'T00:00:00').toLocaleDateString('pt-BR') : '';
        document.getElementById('summaryDate').textContent = dateFormatted + endFormatted;
    }
}

function handleSubmit(e) {
    e.preventDefault();
    
    const user = JSON.parse(localStorage.getItem('user'));
    const paymentMethod = document.getElementById('paymentMethod').value;
    
    if (!paymentMethod) {
        alert('Por favor, selecione um método de pagamento');
        return;
    }
    
    // Validar campos de cartão se necessário
    if ((paymentMethod === 'CREDITO' || paymentMethod === 'DEBITO') && !validateCardFields()) {
        return;
    }
    
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value || startDate;
    const startTime = document.getElementById('startTime').value;
    
    // Criar timestamps ISO
    const startDateTime = `${startDate}T${startTime}:00`;
    const endDateTime = `${endDate}T${startTime}:00`;
    
    const bookingData = {
        userId: user.id,
        serviceId: currentService.id,
        startDateTime: startDateTime,
        endDateTime: endDateTime,
        numberOfGuests: parseInt(document.getElementById('guests').value),
        observations: document.getElementById('observations').value,
        paymentMethod: paymentMethod,
        totalAmount: parseFloat(document.getElementById('totalPrice').textContent.replace('R$ ', '').replace(',', '.'))
    };
    
    console.log('Enviando reserva:', bookingData);
    
    // Enviar para API
    fetch('/api/bookings', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        },
        body: JSON.stringify(bookingData)
    })
    .then(async res => {
        if (res.ok) {
            return res.json();
        } else {
            const errorText = await res.text();
            throw new Error(errorText || 'Erro ao criar reserva');
        }
    })
    .then(data => {
        alert('✅ Reserva realizada com sucesso!\n\nSeu pagamento foi processado e você receberá uma confirmação por email.');
        window.location.href = 'minhas-reservas.html';
    })
    .catch(err => {
        console.error('Erro na reserva:', err);
        alert('❌ Erro ao processar reserva: ' + err.message);
    });
}

function validateCardFields() {
    const cardName = document.getElementById('cardName').value.trim();
    const cardNumber = document.getElementById('cardNumber').value.replace(/\s/g, '');
    const cardExpiry = document.getElementById('cardExpiry').value;
    const cardCVV = document.getElementById('cardCVV').value;
    
    if (!cardName || cardName.length < 3) {
        alert('Por favor, insira o nome como está no cartão');
        return false;
    }
    
    if (!cardNumber || cardNumber.length < 13) {
        alert('Número de cartão inválido');
        return false;
    }
    
    if (!cardExpiry || cardExpiry.length !== 5) {
        alert('Data de validade inválida (use MM/AA)');
        return false;
    }
    
    if (!cardCVV || cardCVV.length < 3) {
        alert('CVV inválido');
        return false;
    }
    
    return true;
}

function getTypeLabel(type) {
    const labels = {
        'HOSPEDAGEM': '🏨 Hospedagem',
        'PASSEIO': '🎯 Passeio',
        'TRANSPORTE': '🚗 Transporte',
        'ALIMENTACAO': '🍽️ Alimentação',
        'EVENTO': '🎉 Evento'
    };
    return labels[type] || type;
}
