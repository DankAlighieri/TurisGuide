// Pacotes de exemplo (mesmos do pacotes.js)
const packages = [
    {
        id: 1,
        title: "Escapada em Gramado - 3 Dias",
        description: "Descubra a magia de Gramado com hospedagem em hotel boutique, passeios pelos pontos turísticos e jantar especial. Inclui transporte, café da manhã e 2 passeios guiados pela região.",
        duration: "3 dias / 2 noites",
        price: 1499.00,
        imageUrl: "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=800",
        includes: ["🏨 Hospedagem em Hotel 4★", "🍽️ Café da Manhã Completo", "🎯 2 Passeios Turísticos", "🚗 Transporte Incluso", "🎭 Ingresso Show Noturno"]
    },
    {
        id: 2,
        title: "Aventura na Chapada - 5 Dias",
        description: "Trilhas, cachoeiras e natureza exuberante. Hospedagem em pousada rústica com guias especializados. Perfeito para amantes de aventura e ecoturismo.",
        duration: "5 dias / 4 noites",
        price: 2299.00,
        imageUrl: "https://images.unsplash.com/photo-1501594907352-04cda38ebc29?w=800",
        includes: ["🏕️ Pousada Rural", "🥾 Trilhas Guiadas", "🍽️ Alimentação Completa", "🚐 Transfer Aeroporto", "📸 Fotógrafo Profissional"]
    },
    {
        id: 3,
        title: "Praia Paradise - Weekend",
        description: "Relaxe em resort à beira-mar com tudo incluído. Perfeito para casais e famílias que buscam descanso e lazer completo.",
        duration: "2 dias / 1 noite",
        price: 899.00,
        imageUrl: "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800",
        includes: ["🏖️ Resort All-Inclusive", "🍹 Bebidas Liberadas", "🏊 Piscina e SPA", "🎉 Atividades Aquáticas", "🍴 3 Refeições"]
    },
    {
        id: 4,
        title: "Cidade Histórica - 4 Dias",
        description: "Explore Ouro Preto e Mariana com guias especializados em história colonial brasileira. Viagem cultural inesquecível.",
        duration: "4 dias / 3 noites",
        price: 1799.00,
        imageUrl: "https://images.unsplash.com/photo-1483181957632-8bda974cbc91?w=800",
        includes: ["🏛️ Hotéis Centro Histórico", "📚 Tours Guiados", "🍽️ Gastronomia Mineira", "🚌 Transfer", "🎫 Ingressos Museus"]
    },
    {
        id: 5,
        title: "Foz do Iguaçu Completo",
        description: "Cataratas, Itaipu e muito mais! Pacote familiar com hospedagem premium. Inclui lado brasileiro e argentino das Cataratas.",
        duration: "4 dias / 3 noites",
        price: 2599.00,
        imageUrl: "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=800",
        includes: ["🏨 Hotel 4★", "🌊 Cataratas BR+AR", "⚡ Usina de Itaipu", "🍽️ Refeições", "✈️ Transfer Aeroporto"]
    },
    {
        id: 6,
        title: "Serra Gaúcha Romântica",
        description: "Roteiro especial para casais em Gramado e Canela com experiências exclusivas. Jantar romântico, vinícola e muito mais.",
        duration: "3 dias / 2 noites",
        price: 1999.00,
        imageUrl: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800",
        includes: ["🏩 Hotel Romântico", "🍷 Tour Vinícola", "🎭 Espetáculos", "🍫 Fábrica Chocolate", "🍽️ Jantar a Dois"]
    }
];

let currentPackage = null;

document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const packageId = parseInt(params.get('packageId'));
    const user = JSON.parse(localStorage.getItem('user'));

    if (!user) {
        alert('Você precisa estar logado para reservar um pacote.');
        window.location.href = 'login.html?redirect=reservar-pacote.html?packageId=' + packageId;
        return;
    }

    currentPackage = packages.find(p => p.id === packageId);

    if (!currentPackage) {
        alert('Pacote não encontrado');
        window.location.href = 'pacotes.html';
        return;
    }

    loadPackage(currentPackage);
    setupEventListeners();

    // Definir data mínima como hoje
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').min = today;
});

function loadPackage(pkg) {
    document.getElementById('packageTitle').textContent = pkg.title;
    document.getElementById('packageDuration').textContent = `⏰ ${pkg.duration}`;
    document.getElementById('packageDescription').textContent = pkg.description;
    document.getElementById('packageImage').src = pkg.imageUrl;
    document.getElementById('packagePrice').textContent = `R$ ${pkg.price.toFixed(2)}`;
    document.getElementById('summaryPackage').textContent = pkg.title;

    // Renderizar itens incluídos
    const includesList = document.getElementById('includesList');
    includesList.innerHTML = pkg.includes.map(item => `
        <div class="include-item">
            <span>✓</span>
            <span>${item}</span>
        </div>
    `).join('');

    updateSummary();
}

function setupEventListeners() {
    document.getElementById('guests').addEventListener('input', updateSummary);
    document.getElementById('paymentMethod').addEventListener('change', updateSummary);
    document.getElementById('bookingForm').addEventListener('submit', handleSubmit);
}

function updateSummary() {
    if (!currentPackage) return;

    const guests = parseInt(document.getElementById('guests').value) || 1;
    const paymentMethod = document.getElementById('paymentMethod').value;

    let subtotal = currentPackage.price * guests;
    let discount = 0;

    // Desconto PIX 5%
    if (paymentMethod === 'PIX') {
        discount = subtotal * 0.05;
    }

    const total = subtotal - discount;

    document.getElementById('summaryGuests').textContent = guests;
    document.getElementById('summarySubtotal').textContent = `R$ ${subtotal.toFixed(2)}`;
    document.getElementById('totalPrice').textContent = `R$ ${total.toFixed(2)}`;
}

function handleSubmit(e) {
    e.preventDefault();

    const user = JSON.parse(localStorage.getItem('user'));
    const paymentMethod = document.getElementById('paymentMethod').value;

    if (!paymentMethod) {
        alert('Por favor, selecione um método de pagamento');
        return;
    }

    const startDate = document.getElementById('startDate').value;
    const guests = parseInt(document.getElementById('guests').value);
    const observations = document.getElementById('observations').value;
    const total = parseFloat(document.getElementById('totalPrice').textContent.replace('R$ ', '').replace(',', '.'));

    // Simular reserva de pacote
    // Em produção, isso seria um endpoint específico para pacotes
    const bookingData = {
        userId: user.id,
        packageId: currentPackage.id,
        packageName: currentPackage.title,
        startDate: startDate,
        numberOfGuests: guests,
        observations: observations,
        paymentMethod: paymentMethod,
        totalAmount: total,
        type: 'PACKAGE'
    };

    console.log('Reservando pacote:', bookingData);

    // Simular sucesso (em produção, chamar API real)
    alert(`✅ Pacote "${currentPackage.title}" reservado com sucesso!\n\nTotal: R$ ${total.toFixed(2)}\nVocê receberá a confirmação por email.`);
    
    // Redirecionar para minhas reservas
    setTimeout(() => {
        window.location.href = 'minhas-reservas.html';
    }, 1500);

    /* Em produção, descomentar:
    fetch('/api/bookings/package', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        },
        body: JSON.stringify(bookingData)
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error('Erro ao processar reserva');
    })
    .then(data => {
        alert('✅ Pacote reservado com sucesso!');
        window.location.href = 'minhas-reservas.html';
    })
    .catch(err => {
        console.error('Erro:', err);
        alert('❌ Erro ao processar reserva: ' + err.message);
    });
    */
}
