let allReservations = [];
let filteredReservations = [];
let providerServices = [];

document.addEventListener('DOMContentLoaded', function() {
    const provider = JSON.parse(localStorage.getItem('provider'));
    
    if (!provider) {
        alert('Você precisa estar logado como prestador.');
        window.location.href = 'login.html';
        return;
    }
    
    loadProviderServices();
    loadReservations();
    
    // Logout
    document.getElementById('logoutBtn')?.addEventListener('click', function(e) {
        e.preventDefault();
        localStorage.removeItem('provider');
        localStorage.removeItem('authToken');
        window.location.href = 'login.html';
    });
});

function loadProviderServices() {
    const provider = JSON.parse(localStorage.getItem('provider'));
    
    fetch(`/api/services/provider/${provider.id}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        }
    })
    .then(res => res.json())
    .then(services => {
        providerServices = services;
        populateServiceFilter(services);
    })
    .catch(err => {
        console.error('Erro ao carregar serviços:', err);
    });
}

function populateServiceFilter(services) {
    const select = document.getElementById('filterService');
    services.forEach(service => {
        const option = document.createElement('option');
        option.value = service.id;
        option.textContent = service.name;
        select.appendChild(option);
    });
}

function loadReservations() {
    const provider = JSON.parse(localStorage.getItem('provider'));
    
    fetch(`/api/bookings/provider/${provider.id}`, {
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        }
    })
    .then(res => res.json())
    .then(reservations => {
        allReservations = reservations;
        filteredReservations = reservations;
        updateStats(reservations);
        applyFilters();
    })
    .catch(err => {
        console.error('Erro ao carregar reservas:', err);
        document.getElementById('reservationsList').innerHTML = 
            '<p class="empty-state">❌ Erro ao carregar reservas. Tente novamente mais tarde.</p>';
    });
}

function updateStats(reservations) {
    const pending = reservations.filter(r => r.status === 'PENDING').length;
    const confirmed = reservations.filter(r => r.status === 'CONFIRMED').length;
    const completed = reservations.filter(r => r.status === 'COMPLETED').length;
    const revenue = reservations
        .filter(r => r.status !== 'CANCELLED')
        .reduce((sum, r) => sum + (r.totalAmount || 0), 0);
    
    document.getElementById('statPending').textContent = pending;
    document.getElementById('statConfirmed').textContent = confirmed;
    document.getElementById('statCompleted').textContent = completed;
    document.getElementById('statRevenue').textContent = `R$ ${revenue.toFixed(2)}`;
}

function applyFilters() {
    const statusFilter = document.getElementById('filterStatus').value;
    const serviceFilter = document.getElementById('filterService').value;
    const dateFrom = document.getElementById('filterDateFrom').value;
    const dateTo = document.getElementById('filterDateTo').value;
    
    filteredReservations = allReservations.filter(reservation => {
        const matchStatus = !statusFilter || reservation.status === statusFilter;
        const matchService = !serviceFilter || reservation.serviceId == serviceFilter;
        
        let matchDateFrom = true;
        let matchDateTo = true;
        
        if (dateFrom && reservation.startDateTime) {
            const resDate = new Date(reservation.startDateTime);
            matchDateFrom = resDate >= new Date(dateFrom);
        }
        
        if (dateTo && reservation.startDateTime) {
            const resDate = new Date(reservation.startDateTime);
            matchDateTo = resDate <= new Date(dateTo);
        }
        
        return matchStatus && matchService && matchDateFrom && matchDateTo;
    });
    
    renderReservations();
}

function renderReservations() {
    const container = document.getElementById('reservationsList');
    const countElement = document.getElementById('resultsCount');
    
    if (!filteredReservations || filteredReservations.length === 0) {
        countElement.textContent = '0 reservas';
        container.innerHTML = `
            <div class="empty-state">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
                    <line x1="16" y1="2" x2="16" y2="6"></line>
                    <line x1="8" y1="2" x2="8" y2="6"></line>
                    <line x1="3" y1="10" x2="21" y2="10"></line>
                </svg>
                <h3>Nenhuma reserva encontrada</h3>
                <p>Não há reservas com os filtros selecionados</p>
            </div>
        `;
        return;
    }
    
    countElement.textContent = `${filteredReservations.length} reserva${filteredReservations.length > 1 ? 's' : ''}`;
    
    container.innerHTML = filteredReservations.map(reservation => {
        const service = providerServices.find(s => s.id === reservation.serviceId);
        const serviceName = service ? service.name : 'Serviço não encontrado';
        
        return `
            <div class="reservation-card">
                <div class="reservation-header">
                    <div class="reservation-info">
                        <h4>${serviceName}</h4>
                        <div class="reservation-meta">
                            <div class="meta-item">
                                <span>📅</span>
                                <span>${formatDateTime(reservation.startDateTime)}</span>
                            </div>
                            <div class="meta-item">
                                <span>👤</span>
                                <span>${reservation.customerName || 'Cliente'}</span>
                            </div>
                            <div class="meta-item">
                                <span>👥</span>
                                <span>${reservation.numberOfGuests} pessoa(s)</span>
                            </div>
                            <div class="meta-item">
                                <span>💰</span>
                                <span>R$ ${(reservation.totalAmount || 0).toFixed(2)}</span>
                            </div>
                        </div>
                    </div>
                    <span class="status-badge ${getStatusClass(reservation.status)}">
                        ${getStatusLabel(reservation.status)}
                    </span>
                </div>
                
                <div class="reservation-details">
                    <div class="detail-row">
                        <span class="detail-label">ID da Reserva:</span>
                        <span class="detail-value">#${reservation.id}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Data de Término:</span>
                        <span class="detail-value">${formatDateTime(reservation.endDateTime)}</span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Método de Pagamento:</span>
                        <span class="detail-value">${reservation.paymentMethod || 'N/A'}</span>
                    </div>
                    ${reservation.observations ? `
                    <div class="detail-row">
                        <span class="detail-label">Observações:</span>
                        <span class="detail-value">${reservation.observations}</span>
                    </div>
                    ` : ''}
                </div>
                
                <div class="reservation-actions">
                    ${reservation.status === 'PENDING' ? `
                        <button class="btn-action btn-confirm" onclick="updateReservationStatus(${reservation.id}, 'CONFIRMED')">
                            ✅ Confirmar
                        </button>
                        <button class="btn-action btn-reject" onclick="updateReservationStatus(${reservation.id}, 'CANCELLED')">
                            ❌ Recusar
                        </button>
                    ` : ''}
                    ${reservation.status === 'CONFIRMED' ? `
                        <button class="btn-action btn-confirm" onclick="updateReservationStatus(${reservation.id}, 'COMPLETED')">
                            🎉 Marcar como Concluída
                        </button>
                        <button class="btn-action btn-reject" onclick="updateReservationStatus(${reservation.id}, 'CANCELLED')">
                            ❌ Cancelar
                        </button>
                    ` : ''}
                    <button class="btn-action btn-contact" onclick="contactCustomer('${reservation.customerEmail || ''}')">
                        💬 Contatar Cliente
                    </button>
                    <button class="btn-action btn-details" onclick="viewReservationDetails(${reservation.id})">
                        📋 Ver Detalhes
                    </button>
                </div>
            </div>
        `;
    }).join('');
}

function updateReservationStatus(reservationId, newStatus) {
    const statusLabels = {
        'CONFIRMED': 'confirmar',
        'CANCELLED': 'cancelar',
        'COMPLETED': 'marcar como concluída'
    };
    
    const action = statusLabels[newStatus] || 'atualizar';
    
    if (!confirm(`Deseja realmente ${action} esta reserva?`)) {
        return;
    }
    
    fetch(`/api/bookings/${reservationId}/status`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('authToken')
        },
        body: JSON.stringify({ status: newStatus })
    })
    .then(res => {
        if (res.ok) {
            alert(`✅ Reserva ${action} com sucesso!`);
            loadReservations(); // Recarregar lista
        } else {
            throw new Error('Erro ao atualizar status');
        }
    })
    .catch(err => {
        console.error('Erro ao atualizar status:', err);
        alert('❌ Erro ao atualizar status da reserva');
    });
}

function contactCustomer(email) {
    if (email) {
        window.location.href = `mailto:${email}?subject=Sobre sua reserva no TurisGuide`;
    } else {
        alert('Email do cliente não disponível');
    }
}

function viewReservationDetails(reservationId) {
    const reservation = allReservations.find(r => r.id === reservationId);
    if (reservation) {
        alert(`Detalhes da Reserva #${reservationId}\n\n${JSON.stringify(reservation, null, 2)}`);
        // TODO: Implementar modal ou página de detalhes
    }
}

function getStatusClass(status) {
    const classes = {
        'PENDING': 'pending',
        'CONFIRMED': 'confirmed',
        'COMPLETED': 'completed',
        'CANCELLED': 'cancelled'
    };
    return classes[status] || '';
}

function getStatusLabel(status) {
    const labels = {
        'PENDING': '⏳ Pendente',
        'CONFIRMED': '✅ Confirmada',
        'COMPLETED': '🎉 Concluída',
        'CANCELLED': '❌ Cancelada'
    };
    return labels[status] || status;
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return 'N/A';
    
    try {
        const date = new Date(dateTimeString);
        return date.toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    } catch (e) {
        return dateTimeString;
    }
}
