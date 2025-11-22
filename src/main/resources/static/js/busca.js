document.addEventListener('DOMContentLoaded', function() {
            const params = new URLSearchParams(window.location.search);
            const query = params.get('q') || '';
            document.getElementById('searchTerm').textContent = query || 'Todos os destinos';

            fetch('/listings')
                .then(res => res.json())
                .then(listings => {
                    const container = document.getElementById('resultsList');
                    container.innerHTML = '';
                    
                    // Filtro simples no front (idealmente seria no back)
                    const filtered = listings.filter(l => 
                        !query || l.localizacao.toLowerCase().includes(query.toLowerCase()) || 
                        l.titulo.toLowerCase().includes(query.toLowerCase())
                    );

                    if(filtered.length === 0) {
                        container.innerHTML = '<p>Nenhum serviço encontrado para este destino.</p>';
                        return;
                    }

                    filtered.forEach(item => {
                        const div = document.createElement('div');
                        div.className = 'listing-card';
                        div.onclick = () => window.location.href = `reservar.html?id=${item.id}`;
                        div.innerHTML = `
                            <img src="${item.imagem || 'https://via.placeholder.com/300x200'}" class="listing-img">
                            <div class="listing-content">
                                <div class="listing-header">
                                    <div>
                                        <span class="tag">${item.tipo}</span>
                                        <h3>${item.titulo}</h3>
                                        <p>📍 ${item.localizacao}</p>
                                    </div>
                                    <div class="listing-price">R$ ${item.valor.toFixed(2)}</div>
                                </div>
                                <p>${item.descricao}</p>
                            </div>
                        `;
                        container.appendChild(div);
                    });
                });
        });