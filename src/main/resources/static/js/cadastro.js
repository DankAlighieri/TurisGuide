(function() {
    const accountType = document.getElementById('accountType');
    const userFields = document.getElementById('userFields');
    const providerFields = document.getElementById('providerFields');
    const form = document.getElementById('registerForm');

    function toggleFields() {
        const type = accountType.value;

        if (type === 'USER') {
            userFields.classList.remove('hidden');
            providerFields.classList.add('hidden');

            // Habilitar validação dos campos de usuário
            document.getElementById('login').required = true;
            document.getElementById('email_user').required = true;
            document.getElementById('password_user').required = true;
            document.getElementById('firstName').required = true;
            document.getElementById('address').required = true;
            document.getElementById('DOB').required = true;
            document.getElementById('role_user').required = true;

            // Desabilitar validação dos campos de prestador
            document.getElementById('provider_name').required = false;
            document.getElementById('cnpj').required = false;
            document.getElementById('email_provider').required = false;
            document.getElementById('password_provider').required = false;
            document.getElementById('services').required = false;

        } else if (type === 'PROVIDER') {
            providerFields.classList.remove('hidden');
            userFields.classList.add('hidden');

            // Habilitar validação dos campos de prestador
            document.getElementById('provider_name').required = true;
            document.getElementById('cnpj').required = true;
            document.getElementById('email_provider').required = true;
            document.getElementById('password_provider').required = true;
            document.getElementById('services').required = true;

            // Desabilitar validação dos campos de usuário
            document.getElementById('login').required = false;
            document.getElementById('email_user').required = false;
            document.getElementById('password_user').required = false;
            document.getElementById('firstName').required = false;
            document.getElementById('address').required = false;
            document.getElementById('DOB').required = false;
            document.getElementById('role_user').required = false;

        } else {
            userFields.classList.add('hidden');
            providerFields.classList.add('hidden');
        }
    }

    accountType.addEventListener('change', toggleFields);
    toggleFields();

    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const type = accountType.value;

        if (!type) {
            alert('Selecione o tipo de conta');
            return;
        }

        if (type === 'USER') {
            const dob = document.getElementById('DOB').value;
            const formattedDOB = dob ? formatDateToDDMMYYYY(dob) : null;

            const payload = {
                login: document.getElementById('login').value.trim(),
                password: document.getElementById('password_user').value,
                firstName: document.getElementById('firstName').value.trim(),
                lastName: document.getElementById('lastName').value.trim(),
                address: document.getElementById('address').value.trim(),
                email: document.getElementById('email_user').value.trim(),
                DOB: formattedDOB,
                role: document.getElementById('role_user').value
            };

            console.log('Payload Usuário:', payload);

            fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(response => {
                if (response.ok) {
                    alert('Cadastro realizado com sucesso!');
                    window.location.href = 'login.html';
                } else {
                    return response.text().then(text => {
                        alert('Erro no cadastro: ' + text);
                    });
                }
            })
            .catch(error => {
                console.error('Erro:', error);
                alert('Erro ao realizar cadastro');
            });

        } else if (type === 'PROVIDER') {
            const payload = {
                name: document.getElementById('provider_name').value.trim(),
                cnpj: document.getElementById('cnpj').value.trim(),
                email: document.getElementById('email_provider').value.trim(),
                password: document.getElementById('password_provider').value,
                services: document.getElementById('services').value.trim()
            };

            console.log('Payload Prestador:', payload);

            fetch('/api/auth/register/provider', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
            .then(response => {
                if (response.ok) {
                    alert('Cadastro realizado com sucesso!');
                    window.location.href = 'login.html';
                } else {
                    return response.text().then(text => {
                        alert('Erro no cadastro: ' + text);
                    });
                }
            })
            .catch(error => {
                console.error('Erro:', error);
                alert('Erro ao realizar cadastro');
            });
        }
    });

    function formatDateToDDMMYYYY(dateString) {
        const date = new Date(dateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    }
})();

