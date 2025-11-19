document.addEventListener('DOMContentLoaded', function() {
    checkProviderAuth();
    setupForm();
});

function checkProviderAuth() {
    const user = JSON.parse(localStorage.getItem('user') || 'null');
    
    if (!user || user.tipo !== 'PRESTADOR') {
        alert('Acesso negado.');
        window.location.href = 'login.html';
        return;
    }
}

function setupForm() {
    document.getElementById('servicoForm').addEventListener('submit', function(e) {
        e.preventDefault();
        cadastrarServico();
    });
}

function cadastrarServico() {
    const user = JSON.parse(localStorage.getItem('user'));
    
    const servico = {
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        tipo: document.getElementById('tipoServico').value,
        preco: parseFloat(document.getElementById('preco').value),
        capacidade: parseInt(document.getElementById('capacidade').value),
        localizacao: document.getElementById('localizacao').value,
        endereco: document.getElementById('endereco').value || '',
        imagem: document.getElementById('imagem').value || '',
        comodidades: document.getElementById('comodidades').value || '',
        provedorId: user.id
    };
    
    // Validação básica
    if (!servico.nome || !servico.descricao || !servico.tipo || !servico.preco || !servico.capacidade || !servico.localizacao) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }
    
    if (servico.preco <= 0) {
        alert('O preço deve ser maior que zero.');
        return;
    }
    
    if (servico.capacidade <= 0) {
        alert('A capacidade deve ser maior que zero.');
        return;
    }
    
    // Mostra loading
    const submitBtn = document.querySelector('.btn-submit');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Cadastrando...';
    submitBtn.disabled = true;
    
    fetch('/api/catalog', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(servico)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 404) {
            throw new Error('Endpoint não encontrado. Verifique se o backend está rodando.');
        } else {
            throw new Error('Erro ao cadastrar serviço');
        }
    })
    .then(data => {
        alert('Serviço cadastrado com sucesso!');
        window.location.href = 'provedor-dashboard.html';
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao cadastrar serviço: ' + error.message);
        submitBtn.textContent = originalText;
        submitBtn.disabled = false;
    });
}
