let allPackages = [];
let currentFilter = 'all';

// Pacotes de exemplo - em produção viriam do backend
const samplePackages = [
    {
        id: 1,
        title: "Escapada em Gramado - 3 Dias",
        description: "Descubra a magia de Gramado com hospedagem em hotel boutique, passeios pelos pontos turísticos e jantar especial.",
        duration: "3 dias / 2 noites",
        price: 1499.00,
        category: "weekend",
        type: "relax",
        badge: "popular",
        imageUrl: "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=400",
        includes: ["🏨 Hospedagem", "🍽️ Café da Manhã", "🎯 2 Passeios", "🚗 Transporte"]
    },
    {
        id: 2,
        title: "Aventura na Chapada - 5 Dias",
        description: "Trilhas, cachoeiras e natureza exuberante. Hospedagem em pousada rústica com guias especializados.",
        duration: "5 dias / 4 noites",
        price: 2299.00,
        category: "week",
        type: "adventure",
        badge: "new",
        imageUrl: "https://images.unsplash.com/photo-1501594907352-04cda38ebc29?w=400",
        includes: ["🏕️ Pousada", "🥾 Trilhas Guiadas", "🍽️ Alimentação", "🚐 Transfer"]
    },
    {
        id: 3,
        title: "Praia Paradise - Weekend",
        description: "Relaxe em resort à beira-mar com tudo incluído. Perfeito para casais e famílias.",
        duration: "2 dias / 1 noite",
        price: 899.00,
        category: "weekend",
        type: "relax",
        badge: "hot",
        imageUrl: "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=400",
        includes: ["🏖️ Resort All-Inclusive", "🍹 Bebidas", "🏊 Piscina", "🎉 Atividades"]
    },
    {
        id: 4,
        title: "Cidade Histórica - 4 Dias",
        description: "Explore Ouro Preto e Mariana com guias especializados em história colonial brasileira.",
        duration: "4 dias / 3 noites",
        price: 1799.00,
        category: "week",
        type: "family",
        badge: null,
        imageUrl: "https://images.unsplash.com/photo-1483181957632-8bda974cbc91?w=400",
        includes: ["🏛️ Hotéis Centro", "📚 Tours Históricos", "🍽️ Gastronomia Local", "🚌 Transfer"]
    },
    {
        id: 5,
        title: "Foz do Iguaçu Completo",
        description: "Cataratas, Itaipu e muito mais! Pacote familiar com hospedagem premium.",
        duration: "4 dias / 3 noites",
        price: 2599.00,
        category: "week",
        type: "family",
        badge: "popular",
        imageUrl: "https://images.unsplash.com/photo-1483729558449-99ef09a8c325?w=400",
        includes: ["🏨 Hotel 4★", "🌊 Cataratas", "⚡ Itaipu", "🍽️ Refeições", "✈️ Transfer Aeroporto"]
    },
    {
        id: 6,
        title: "Serra Gaúcha Romântica",
        description: "Roteiro especial para casais em Gramado e Canela com experiências exclusivas.",
        duration: "3 dias / 2 noites",
        price: 1999.00,
        category: "weekend",
        type: "relax",
        badge: null,
        imageUrl: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400",
        includes: ["🏩 Hotel Romântico", "🍷 Vinícola", "🎭 Espetáculos", "🍫 Chocolate"]
    }
];

document.addEventListener('DOMContentLoaded', function() {
    loadPackages();
});

function loadPackages() {
    // Simulando chamada à API
    allPackages = samplePackages;
    renderPackages();
}

function filterPackages(category) {
    currentFilter = category;
    
    // Atualizar botões ativos
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    event.target.classList.add('active');
    
    renderPackages();
}

function renderPackages() {
    const grid = document.getElementById('packagesGrid');
    
    let filtered = allPackages;
    if (currentFilter !== 'all') {
        filtered = allPackages.filter(pkg => 
            pkg.category === currentFilter || pkg.type === currentFilter
        );
    }
    
    if (filtered.length === 0) {
        grid.innerHTML = `
            <div class="no-packages">
                <h3>Nenhum pacote encontrado</h3>
                <p>Tente outro filtro ou volte mais tarde para novos pacotes!</p>
            </div>
        `;
        return;
    }
    
    grid.innerHTML = filtered.map(pkg => `
        <div class="package-card" onclick="viewPackageDetails(${pkg.id})">
            <div style="position: relative;">
                ${pkg.badge ? `<span class="package-badge ${pkg.badge === 'popular' ? 'popular' : pkg.badge === 'new' ? 'new' : ''}">${getBadgeLabel(pkg.badge)}</span>` : ''}
                <img src="${pkg.imageUrl}" alt="${pkg.title}" class="package-image">
            </div>
            
            <div class="package-content">
                <h3 class="package-title">${pkg.title}</h3>
                
                <div class="package-duration">
                    <span>⏰</span>
                    <span>${pkg.duration}</span>
                </div>
                
                <p class="package-description">${pkg.description}</p>
                
                <div class="package-includes">
                    <h4>O que está incluído:</h4>
                    <div class="includes-list">
                        ${pkg.includes.map(item => `
                            <span class="include-item">${item}</span>
                        `).join('')}
                    </div>
                </div>
                
                <div class="package-footer">
                    <div class="package-price">
                        <span class="price-label">A partir de</span>
                        <span class="price-value">R$ ${pkg.price.toFixed(2)}</span>
                    </div>
                    <button class="btn-buy-package" onclick="event.stopPropagation(); buyPackage(${pkg.id})">
                        Reservar
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function getBadgeLabel(badge) {
    const labels = {
        'popular': '🔥 Popular',
        'new': '✨ Novo',
        'hot': '💥 Oferta'
    };
    return labels[badge] || badge;
}

function viewPackageDetails(packageId) {
    // Redireciona para página de detalhes (pode ser modal ou nova página)
    const pkg = allPackages.find(p => p.id === packageId);
    if (pkg) {
        // Por enquanto, redireciona para reserva
        window.location.href = `reservar-pacote.html?packageId=${packageId}`;
    }
}

function buyPackage(packageId) {
    const user = JSON.parse(localStorage.getItem('user'));
    
    if (!user) {
        alert('Você precisa estar logado para reservar um pacote.');
        window.location.href = 'login.html?redirect=pacotes.html';
        return;
    }
    
    window.location.href = `reservar-pacote.html?packageId=${packageId}`;
}
