document.addEventListener('DOMContentLoaded', function() {
    setupForm();
});

function setupForm() {
    document.getElementById('servicoForm').addEventListener('submit', function(e) {
        e.preventDefault();
        cadastrarServico();
    });
}

function cadastrarServico() {
    const providerId = JSON.parse(localStorage.getItem('providerId'));
    
    const servico = {
        titulo: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        tipo: document.getElementById('tipoServico').value,
        valor: parseFloat(document.getElementById('preco').value),
        localizacao: document.getElementById('localizacao').value,
        endereco: document.getElementById('endereco').value || '',
        imagem: document.getElementById('imagem').value || '',
        providerId: providerId
    };
    
    if (!servico.titulo || !servico.descricao || !servico.tipo || !servico.valor || !servico.localizacao) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }
    
    if (servico.valor <= 0) {
        alert('O preço deve ser maior que zero.');
        return;
    }
    
    // Mostra loading
    const submitBtn = document.querySelector('.btn-submit');
    const originalText = submitBtn.textContent;
    submitBtn.textContent = 'Cadastrando...';
    submitBtn.disabled = true;
    
    fetch('/listings', {
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
    .then(() => {
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
