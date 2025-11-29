// Lógica de navbar: mostra botão de login quando não logado
// e nome do usuário com menu dropdown quando logado.
(function() {
    const loginNavItem = document.getElementById('loginNavItem');
    const welcomeNavItem = document.getElementById('welcomeNavItem');
    const userName = document.getElementById('userName');
    const userDropdown = document.getElementById('userDropdown');
    const logoutBtn = document.getElementById('logoutBtn');

    if (!loginNavItem || !welcomeNavItem) {
        return;
    }

    // Recupera o token salvo no login
    const token = localStorage.getItem('authToken');

    if (!token) {
        // Não logado: mostra botão de login, esconde saudação
        loginNavItem.style.display = '';
        welcomeNavItem.style.display = 'none';
        if (userName) userName.textContent = '';
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

    // Logado: esconde botão de login e mostra nome do usuário
    loginNavItem.style.display = 'none';
    welcomeNavItem.style.display = '';
    if (userName) {
        userName.textContent = displayName;
    }

    // Toggle dropdown ao clicar no nome
    if (userName && userDropdown) {
        userName.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            userDropdown.classList.toggle('show');
        });

        // Fecha o dropdown ao clicar fora
        document.addEventListener('click', function(e) {
            if (!welcomeNavItem.contains(e.target)) {
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
})();



