// Lógica de navbar: mostra botão de login quando não logado
// e saudação "Bem-vindo, <firstName>" quando logado.
(function() {
    const loginNavItem = document.getElementById('loginNavItem');
    const welcomeNavItem = document.getElementById('welcomeNavItem');
    const myBookingsNavItem = document.getElementById('myBookingsNavItem');

    if (!loginNavItem || !welcomeNavItem) {
        return;
    }

    // Recupera o token salvo no login
    const token = localStorage.getItem('authToken');

    if (!token) {
        // Não logado: mostra botão de login, esconde saudação e minhas reservas
        loginNavItem.style.display = '';
        welcomeNavItem.style.display = 'none';
        welcomeNavItem.textContent = '';
        if(myBookingsNavItem) myBookingsNavItem.style.display = 'none';
        return;
    }

    // Tenta decodificar o JWT para extrair o firstName (se estiver no payload)
    let firstName = null;
    try {
        const payloadBase64 = token.split('.')[1];
        if (payloadBase64) {
            const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
            const payload = JSON.parse(payloadJson);

            // Ajuste aqui conforme o que o back coloca no token (ex.: "firstName", "nome", "sub", etc.)
            firstName = payload.firstName || payload.nome || payload.sub || null;
        }
    } catch (e) {
        console.warn('Não foi possível decodificar o token JWT:', e);
    }

    // Se não conseguir extrair o nome, mostra uma saudação genérica
    const displayName = firstName || 'usuário';

    // Logado: esconde botão de login e mostra saudação e minhas reservas
    loginNavItem.style.display = 'none';
    welcomeNavItem.style.display = '';
    welcomeNavItem.textContent = `Bem-vindo, ${displayName}`;
    if(myBookingsNavItem) myBookingsNavItem.style.display = '';

    // Lógica de Busca
    const searchBtn = document.getElementById('searchBtn');
    if(searchBtn) {
        searchBtn.addEventListener('click', function() {
            const destination = document.getElementById('searchInput').value;
            const start = document.getElementById('startDate').value;
            const end = document.getElementById('endDate').value;
            
            // Redireciona para a tela de seleção de destino
            window.location.href = `busca.html?q=${encodeURIComponent(destination)}&start=${start}&end=${end}`;
        });
    }
})();

