document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const listingId = params.get('id');
    const user = JSON.parse(localStorage.getItem('user'));

    if (!user) {
        alert('Você precisa estar logado para reservar.');
        window.location.href = 'login.html';
        return;
    }

    let currentListing = null;

    // 1. Carregar detalhes do serviço principal
    fetch('/listings') // Idealmente seria /listings/{id}
        .then(res => res.json())
        .then(listings => {
            currentListing = listings.find(l => l.id == listingId);
            if (!currentListing) {
                alert('Serviço não encontrado');
                return;
            }
            renderService(currentListing);
            loadSuggestions(listings, currentListing);
        });

    function renderService(item) {
        document.getElementById('serviceTitle').textContent = item.titulo;
        document.getElementById('serviceLoc').textContent = `📍 ${item.localizacao} | ${item.tipo}`;
        document.getElementById('servicePrice').textContent = `R$ ${item.valor.toFixed(2)}`;
    }

    // 2. Carregar sugestões (Lógica da imagem: Sugestão de Hoteis/Passeios)
    function loadSuggestions(allListings, current) {
        const container = document.getElementById('suggestionsList');
        // Se estou vendo um Hotel, sugira Passeios. Se Passeio, sugira Hoteis.
        const targetType = current.tipo === 'HOSPEDAGEM' ? 'PASSEIO' : 'HOSPEDAGEM';
        
        const suggestions = allListings.filter(l => 
            l.tipo === targetType && 
            l.localizacao.includes(current.localizacao.split(',')[0]) // Tenta match por cidade
        ).slice(0, 3); // Pega 3

        if (suggestions.length === 0) {
            container.innerHTML = '<p>Sem sugestões para esta região.</p>';
            return;
        }

        suggestions.forEach(s => {
            const div = document.createElement('div');
            div.className = 'suggestion-card';
            div.innerHTML = `
                <strong>${s.titulo}</strong><br>
                <small>R$ ${s.valor}</small>
            `;
            div.onclick = () => window.location.href = `reservar.html?id=${s.id}`;
            container.appendChild(div);
        });
    }

    // 3. Processar Pagamento e Reserva
    document.getElementById('bookingForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        const checkIn = document.getElementById('checkIn').value;
        const checkOut = document.getElementById('checkOut').value;
        
        // Formatar datas para dd/MM/yyyy (formato esperado pelo DTO)
        const formatDate = (dateStr) => {
            if(!dateStr) return null;
            const [y, m, d] = dateStr.split('-');
            return `${d}/${m}/${y}`;
        };

        const payload = {
            userId: user.id,
            listingId: listingId,
            checkIn: formatDate(checkIn),
            checkOut: formatDate(checkOut) || formatDate(checkIn), // Fallback
            guests: parseInt(document.getElementById('guests').value),
            paymentMethod: document.getElementById('paymentMethod').value
        };

        fetch('/booking', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('authToken')
            },
            body: JSON.stringify(payload)
        })
        .then(async res => {
            if (res.ok) {
                alert('Reserva realizada e pagamento processado com sucesso!');
                window.location.href = 'minhas-reservas.html';
            } else {
                const txt = await res.text();
                alert('Erro na reserva: ' + txt);
            }
        })
        .catch(err => console.error(err));
    });
});
