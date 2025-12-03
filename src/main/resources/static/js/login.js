// Adiciona comportamento de login chamando o back-end (/auth/login)
(function() {
    const form = document.getElementById('loginForm');
    if (!form) return;

    // Campos: o primeiro input de texto é login/email, o segundo é senha
    const providerCheckbox = form.querySelector('input[type="checkbox"]')
    const loginInput = form.querySelector('input[type="text"]');
    const passwordInput = form.querySelector('input[type="password"]');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

       if (!loginInput || !passwordInput) {
            alert('Campos de formulário não encontrados.');
            return;
        }

        const isProvider = providerCheckbox ? providerCheckbox.checked : false;
        const login = loginInput.value.trim();
        const password = passwordInput.value;
        
        if (isProvider) {
            const payload = {
                cnpj: login,
                password: password
            };

            fetch('/provider/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            })
            .then(async response => {
                const responseText = await response.text();
                let data;
                try {
                    data = responseText ? JSON.parse(responseText) : null;
                } catch(err) {
                    console.error('Erro ao parsear resposta:', err, responseText);
                }

                if (response.ok) {
                    // Esperando que o back-end retorne { token: "jwt...", provider: {...} }
                    const token = data && (data.token || data.accessToken || data.jwt);
                    const providerId = data && data.providerId;

                    if (token) {
                        // Armazena o token para uso nas próximas requisições
                        localStorage.setItem('authToken', token);
                    }


                    if (providerId) {
                        // Armazena os dados do provedor
                        localStorage.setItem('providerCNPJ', JSON.stringify(payload.cnpj));
                        localStorage.setItem('providerId', JSON.stringify(providerId));
                    }

                    alert('Login realizado com sucesso!');

                    
                    window.location.href = 'provedor-dashboard.html';
                    
                } else if (response.status === 401 || response.status === 403) {
                    alert('Credenciais inválidas. Verifique seu login e senha.');
                } else {
                    alert('Erro ao realizar login: ' + (text || 'Erro desconhecido'));
                }
            })
        } else {

            const payload = {
                login: login,
                password: password
            };

            fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            })
            .then(async response => {
                const text = await response.text();
                let data;
                try {
                    data = text ? JSON.parse(text) : null;
                } catch (err) {
                    console.error('Erro ao parsear resposta:', err, text);
                }

                if (response.ok) {
                    const token = data && (data.token || data.accessToken || data.jwt);
                    const userId = data && data.userId;

                    if (token) {
                        // Armazena o token para uso nas próximas requisições
                        localStorage.setItem('authToken', token);
                    }
                    
                    localStorage.setItem('user', JSON.stringify(payload.login));

                    if (userId) {
                        // Armazena os dados do usuário
                        localStorage.setItem('userId', JSON.stringify(userId));
                    }
                    
                    alert('Login realizado com sucesso!');

                    window.location.href = 'index.html';

                } else if (response.status === 401 || response.status === 403) {
                    alert('Credenciais inválidas. Verifique seu login e senha.');
                } else {
                    alert('Erro ao realizar login: ' + (text || 'Erro desconhecido'));
                }
            })
            .catch(error => {
                console.error('Erro na requisição de login:', error);
                alert('Erro de conexão com o servidor.');
            });
        }
    });
})();