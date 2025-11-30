let allServices = [];
let filteredServices = [];

document.addEventListener('DOMContentLoaded', function() {
    loadServices();
});

function loadServices() {
    fetch('/api/services')
        .then(res => res.json())
        .then(services => {
            allServices = services;
            filteredServices = services;
            applyFilters();
        })
        .catch(err => {
            console.error('Erro ao carregar serviços:', err);
            document.getElementById('resultsList').innerHTML = 
                '<p class="no-results">❌ Erro ao carregar serviços. Tente novamente mais tarde.</p>';
        });
}

function applyFilters() {
    const searchTerm = document.getElementById('filterSearch')?.value.toLowerCase() || '';
    const location = document.getElementById('filterLocation')?.value.toLowerCase() || '';
    const type = document.getElementById('filterType')?.value || '';
    const maxPrice = parseFloat(document.getElementById('filterMaxPrice')?.value) || Infinity;
    const minCapacity = parseInt(document.getElementById('filterCapacity')?.value) || 0;

    filteredServices = allServices.filter(service => {
        const matchSearch = !searchTerm || 
            service.name.toLowerCase().includes(searchTerm) ||
            service.description.toLowerCase().includes(searchTerm);
        
        const matchLocation = !location || 
            service.location.toLowerCase().includes(location);
        
        const matchType = !type || service.type === type;
        
        const matchPrice = service.price <= maxPrice;
        
        const matchCapacity = service.capacity >= minCapacity;

        return matchSearch && matchLocation && matchType && matchPrice && matchCapacity;
    });

    sortResults();
}

function sortResults() {
    const sortBy = document.getElementById('sortBy')?.value || 'relevance';

    switch(sortBy) {
        case 'price-asc':
            filteredServices.sort((a, b) => a.price - b.price);
            break;
        case 'price-desc':
            filteredServices.sort((a, b) => b.price - a.price);
            break;
        case 'name':
            filteredServices.sort((a, b) => a.name.localeCompare(b.name));
            break;
        default:
            // Relevância (mantém ordem original)
            break;
    }

    renderResults();
}

function renderResults() {
    const container = document.getElementById('resultsList');
    const countElement = document.getElementById('resultsCount');

    if (!filteredServices || filteredServices.length === 0) {
        countElement.textContent = 'Nenhum serviço encontrado';
        container.innerHTML = `
            <div class="no-results">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <circle cx="11" cy="11" r="8"></circle>
                    <path d="m21 21-4.35-4.35"></path>
                </svg>
                <h3>Nenhum serviço encontrado</h3>
                <p>Tente ajustar os filtros ou fazer uma nova busca</p>
            </div>
        `;
        return;
    }

    countElement.textContent = `${filteredServices.length} serviço${filteredServices.length > 1 ? 's' : ''} encontrado${filteredServices.length > 1 ? 's' : ''}`;
    
    container.innerHTML = filteredServices.map(service => `
        <div class="listing-card" onclick="window.location.href='reservar.html?serviceId=${service.id}'">
            <img src="${service.imageUrl || 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400'}" 
                 class="listing-img" 
                 alt="${service.name}"
                 onerror="this.src='https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400'">
            
            <div class="listing-content">
                <div class="listing-header">
                    <div class="listing-info">
                        <span class="tag">${getTypeLabel(service.type)}</span>
                        <h3>${service.name}</h3>
                        <div class="listing-location">
                            <span>📍</span>
                            <span>${service.location}</span>
                        </div>
                    </div>
                    <div class="listing-price">
                        R$ ${service.price.toFixed(2)}
                        <small>/pessoa</small>
                    </div>
                </div>
                
                <p class="listing-description">${truncateText(service.description, 150)}</p>
                
                <div class="listing-footer">
                    <span class="listing-capacity">👥 Capacidade: até ${service.capacity} pessoas</span>
                    <button class="btn-reserve" onclick="event.stopPropagation(); window.location.href='reservar.html?serviceId=${service.id}'">
                        Ver Detalhes
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function clearFilters() {
    document.getElementById('filterSearch').value = '';
    document.getElementById('filterLocation').value = '';
    document.getElementById('filterType').value = '';
    document.getElementById('filterMaxPrice').value = '';
    document.getElementById('filterCapacity').value = '1';
    document.getElementById('sortBy').value = 'relevance';
    
    filteredServices = allServices;
    renderResults();
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

function truncateText(text, maxLength) {
    if (!text) return '';
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength) + '...';
}