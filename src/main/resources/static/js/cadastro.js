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

        } else {
            userFields.classList.add('hidden');
            providerFields.classList.add('hidden');
        }
    }

    if (!accountType || !form) {
        return;
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
            // Captura segura dos elementos de usuário
            const dobInput = document.getElementById('DOB');
            const loginInput = document.getElementById('login');
            const passwordUserInput = document.getElementById('password_user');
            const firstNameInput = document.getElementById('firstName');
            const lastNameInput = document.getElementById('lastName');
            const addressInput = document.getElementById('address');
            const emailUserInput = document.getElementById('email_user');

            if (!dobInput || !loginInput || !passwordUserInput || !firstNameInput || !lastNameInput || !addressInput || !emailUserInput) {
                console.error('Algum campo de usuário não foi encontrado no DOM.', {
                    dobInput,
                    loginInput,
                    passwordUserInput,
                    firstNameInput,
                    lastNameInput,
                    addressInput,
                    emailUserInput
                });
                alert('Erro interno na tela de cadastro: campo de usuário não encontrado.');
                return;
            }

            const dobRaw = dobInput.value; // yyyy-MM-dd vindo do input type="date"
            let dobFormatted = null;

            if (dobRaw) {
                const [year, month, day] = dobRaw.split('-');
                dobFormatted = `${day}/${month}/${year}`; // dd/MM/yyyy
            }

            const payload = {
                login: loginInput.value.trim(),
                password: passwordUserInput.value,
                firstName: firstNameInput.value.trim(),
                lastName: lastNameInput.value.trim(),
                address: addressInput.value.trim(),
                email: emailUserInput.value.trim(),
                DOB: dobFormatted,
                // Você pode ajustar o role se quiser permitir escolha dinâmica.
                // Aqui vou definir como USER por padrão; altere para 'ADMIN' se realmente quiser isso fixo.
                role: 'USER'
            };

            console.log('Payload Usuário:', payload);

            fetch('/auth/register', {
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

            fetch('/provider/register', {
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
})();
