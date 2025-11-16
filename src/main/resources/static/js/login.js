// Adiciona comportamento de login chamando o back-end (/auth/login)
(function() {
    const form = document.getElementById('loginForm');
    if (!form) return;

    // Campos: o primeiro input de texto é login/email, o segundo é senha
    const loginInput = form.querySelector('input[type="text"]');
    const passwordInput = form.querySelector('input[type="password"]');

    form.addEventListener('submit', function(e) {
        e.preventDefault();

        const login = loginInput.value.trim();
        const password = passwordInput.value;

        if (!login || !password) {
            alert('Preencha login e senha.');
            return;
        }

        const payload = {
            login: login,
            password: password
        };

        console.log('Payload de login:', payload);

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
                // Esperando que o back-end retorne { token: "jwt..." }
                const token = data && (data.token || data.accessToken || data.jwt);
                if (token) {
                    // Armazena o token para uso nas próximas requisições
                    localStorage.setItem('authToken', token);
                }
                alert('Login realizado com sucesso!');
                // Redirecionar para a página inicial ou dashboard
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
    });
})();

