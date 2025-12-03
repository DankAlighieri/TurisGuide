let currentService = null;
let serviceId = null;

document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    serviceId = params.get('id');
    
    const provider = JSON.parse(localStorage.getItem('provider'));
    
    if (!provider) {
        alert('Você precisa estar logado como prestador.');
        window.location.href = 'login.html';
        return;
    }
    
    if (!serviceId) {
        alert('ID do serviço não fornecido.');
        window.location.href = 'provedor-dashboard.html';
        return;
    }
    
    loadService();
    setupEventListeners();
});

function loadService() {
    fetch(`/api/services/${serviceId}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        }
    })
    .then(res => {
        if (!res.ok) throw new Error('Serviço não encontrado');
        return res.json();
    })
    .then(service => {
        currentService = service;
        populateForm(service);
        document.getElementById('loadingMessage').style.display = 'none';
        document.getElementById('editServiceForm').style.display = 'block';
    })
    .catch(err => {
        console.error('Erro ao carregar serviço:', err);
        alert('Erro ao carregar informações do serviço: ' + err.message);
        window.location.href = 'provedor-dashboard.html';
    });
}

function populateForm(service) {
    document.getElementById('serviceName').value = service.name || '';
    document.getElementById('serviceDescription').value = service.description || '';
    document.getElementById('serviceType').value = service.type || '';
    document.getElementById('serviceCapacity').value = service.capacity || '';
    document.getElementById('serviceLocation').value = service.location || '';
    document.getElementById('servicePrice').value = service.price || '';
    document.getElementById('serviceDuration').value = service.duration || '';
    document.getElementById('serviceImage').value = service.imageUrl || '';
    document.getElementById('serviceAmenities').value = service.amenities || '';
    document.getElementById('servicePolicies').value = service.policies || '';
    
    // Atualizar contadores
    updateCharCount('serviceName', 'nameCount', 100);
    updateCharCount('serviceDescription', 'descCount', 500);
    
    // Preview da imagem
    if (service.imageUrl) {
        updateImagePreview(service.imageUrl);
    }
}

function setupEventListeners() {
    // Contadores de caracteres
    document.getElementById('serviceName').addEventListener('input', () => 
        updateCharCount('serviceName', 'nameCount', 100));
    document.getElementById('serviceDescription').addEventListener('input', () => 
        updateCharCount('serviceDescription', 'descCount', 500));
    
    // Preview de imagem
    document.getElementById('serviceImage').addEventListener('input', (e) => 
        updateImagePreview(e.target.value));
    
    // Submissão do formulário
    document.getElementById('editServiceForm').addEventListener('submit', handleSubmit);
}

function updateCharCount(fieldId, countId, max) {
    const field = document.getElementById(fieldId);
    const count = document.getElementById(countId);
    const length = field.value.length;
    count.textContent = length;
    
    if (length > max * 0.9) {
        count.style.color = '#e74c3c';
    } else {
        count.style.color = '#999';
    }
}

function updateImagePreview(url) {
    const preview = document.getElementById('imagePreview');
    
    if (!url || !url.startsWith('http')) {
        preview.innerHTML = '';
        return;
    }
    
    preview.innerHTML = `
        <img src="${url}" class="preview-img" 
             onerror="this.src='https://via.placeholder.com/150?text=Imagem+Inv%C3%A1lida'" 
             alt="Preview">
    `;
}

function handleSubmit(e) {
    e.preventDefault();
    
    const provider = JSON.parse(localStorage.getItem('provider'));
    
    const updatedService = {
        id: serviceId,
        providerId: provider.id,
        name: document.getElementById('serviceName').value.trim(),
        description: document.getElementById('serviceDescription').value.trim(),
        type: document.getElementById('serviceType').value,
        capacity: parseInt(document.getElementById('serviceCapacity').value),
        location: document.getElementById('serviceLocation').value.trim(),
        price: parseFloat(document.getElementById('servicePrice').value),
        duration: document.getElementById('serviceDuration').value.trim(),
        imageUrl: document.getElementById('serviceImage').value.trim(),
        amenities: document.getElementById('serviceAmenities').value.trim(),
        policies: document.getElementById('servicePolicies').value.trim(),
        active: true
    };
    
    // Validações
    if (!updatedService.name || updatedService.name.length < 5) {
        alert('Nome do serviço deve ter pelo menos 5 caracteres');
        return;
    }
    
    if (!updatedService.description || updatedService.description.length < 20) {
        alert('Descrição deve ter pelo menos 20 caracteres');
        return;
    }
    
    if (updatedService.price <= 0) {
        alert('Preço deve ser maior que zero');
        return;
    }
    
    if (updatedService.capacity < 1) {
        alert('Capacidade deve ser de pelo menos 1 pessoa');
        return;
    }
    
    console.log('Atualizando serviço:', updatedService);
    
    // Enviar para API
    fetch(`/api/services/${serviceId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        },
        body: JSON.stringify(updatedService)
    })
    .then(async res => {
        if (res.ok) {
            return res.json();
        } else {
            const errorText = await res.text();
            throw new Error(errorText || 'Erro ao atualizar serviço');
        }
    })
    .then(data => {
        alert('✅ Serviço atualizado com sucesso!');
        window.location.href = 'provedor-dashboard.html';
    })
    .catch(err => {
        console.error('Erro ao atualizar:', err);
        alert('❌ Erro ao atualizar serviço: ' + err.message);
    });
}

function deleteService() {
    if (!confirm('⚠️ Tem certeza que deseja excluir este serviço?\n\nEsta ação não pode ser desfeita e todas as reservas futuras serão canceladas.')) {
        return;
    }
    
    if (!confirm('Confirmar exclusão definitiva?')) {
        return;
    }
    
    fetch(`/api/services/${serviceId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        }
    })
    .then(res => {
        if (res.ok) {
            alert('✅ Serviço excluído com sucesso!');
            window.location.href = 'provedor-dashboard.html';
        } else {
            throw new Error('Erro ao excluir serviço');
        }
    })
    .catch(err => {
        console.error('Erro ao excluir:', err);
        alert('❌ Erro ao excluir serviço: ' + err.message);
    });
}
