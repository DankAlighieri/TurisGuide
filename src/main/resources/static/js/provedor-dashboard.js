document.addEventListener('DOMContentLoaded', function() {
    loadProviderInfo();
    setupUserMenu();
});

function setupUserMenu() {
    const userName = document.getElementById('userName');
    const userDropdown = document.getElementById('userDropdown');
    const logoutBtn = document.getElementById('logoutBtn');
    const welcomeNavItem = document.getElementById('welcomeNavItem');

    // Toggle dropdown ao clicar no nome
    if (userName && userDropdown) {
        userName.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            userDropdown.classList.toggle('show');
        });

        // Fecha o dropdown ao clicar fora
        document.addEventListener('click', function(e) {
            if (welcomeNavItem && !welcomeNavItem.contains(e.target)) {
                userDropdown.classList.remove('show');
            }
        });
    }

    // Logout
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault();
            localStorage.removeItem('authToken');
            localStorage.removeItem('user');
            window.location.href = 'login.html';
        });
    }
}

function loadProviderInfo() {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        document.getElementById('providerName').textContent = user.nome || user.username;
        const userName = document.getElementById('userName');
        if (userName) {
            userName.textContent = user.nome || user.username;
        }
    }
}

function loadServices() {
    const user = JSON.parse(localStorage.getItem('user'));
    
    fetch(`/api/catalog/provider/${user.id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao carregar serviços');
            }
            return response.json();
        })
        .then(services => {
            displayServices(services);
            document.getElementById('servicesList').style.display = 'block';
        })
        .catch(error => {
            console.error('Erro ao carregar serviços:', error);
            alert('Erro ao carregar serviços. Verifique se você tem serviços cadastrados.');
            // Exibe seção vazia
            displayServices([]);
            document.getElementById('servicesList').style.display = 'block';
        });
}

function displayServices(services) {
    const grid = document.getElementById('servicesGrid');
    
    if (services.length === 0) {
        grid.innerHTML = '<p style="text-align: center; color: #666; padding: 2rem;">Você ainda não tem serviços cadastrados. Clique em "Novo Serviço" para começar.</p>';
        return;
    }
    
    grid.innerHTML = services.map(service => `
        <div class="service-card">
            <img src="${service.imagem || 'https://via.placeholder.com/400x300?text=Sem+Imagem'}" alt="${service.nome}">
            <div class="service-info">
                <span class="service-type">${formatTipoServico(service.tipo)}</span>
                <h3>${service.nome}</h3>
                <p>${service.descricao ? (service.descricao.substring(0, 100) + (service.descricao.length > 100 ? '...' : '')) : ''}</p>
                <div class="service-details">
                    <span>💰 R$ ${formatPrice(service.preco)}</span>
                    <span>📍 ${service.localizacao}</span>
                </div>
                <div class="service-actions">
                    <button onclick="editService(${service.id})" class="btn-edit">Editar</button>
                    <button onclick="deleteService(${service.id})" class="btn-delete">Excluir</button>
                </div>
            </div>
        </div>
    `).join('');
}

function formatTipoServico(tipo) {
    const tipos = {
        'HOSPEDAGEM': 'Hospedagem',
        'TRANSPORTE': 'Transporte',
        'PASSEIO': 'Passeio/Tour',
        'RESTAURANTE': 'Restaurante',
        'EVENTO': 'Evento'
    };
    return tipos[tipo] || tipo;
}

function formatPrice(price) {
    return parseFloat(price).toFixed(2).replace('.', ',');
}

function editService(id) {
    // Por enquanto apenas navega para a página de cadastro
    // Você pode implementar uma página de edição separada depois
    window.location.href = `editar-servico.html?id=${id}`;
}

function deleteService(id) {
    if (!confirm('Tem certeza que deseja excluir este serviço?')) return;
    
    fetch(`/api/catalog/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            alert('Serviço excluído com sucesso!');
            loadServices();
        } else {
            throw new Error('Erro ao excluir');
        }
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao excluir serviço. Tente novamente.');
    });
}

document.getElementById('logoutBtn')?.addEventListener('click', function(e) {
    e.preventDefault();
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.href = 'index.html';
});
