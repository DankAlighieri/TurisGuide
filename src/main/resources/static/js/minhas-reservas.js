document.addEventListener('DOMContentLoaded', function() {
    const userId = JSON.parse(localStorage.getItem('userId'));

    if (!userId) window.location.href = 'login.html';
    
    loadBookings();
    
    function loadBookings() {
        fetch(`/booking/${userId}`, {
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('authToken') }
        })
        .then(res => res.json())
        .then(bookings => {
            const container = document.getElementById('bookingsList');
            container.innerHTML = '';
            
            if (bookings.length === 0) {
                container.innerHTML = '<p>Você não possui reservas ativas.</p>';
                return;
            }
            bookings.forEach(b => {
                const div = document.createElement('div');
                div.className = 'booking-card';
                div.innerHTML = `
                    <div class="booking-info">
                        <h3>${b.listing ? b.listing.titulo : 'Serviço indisponível'}</h3>
                        <p>Data: ${b.checkIn} - ${b.checkOut}</p>
                        <p>Hóspedes: ${b.guests}</p>
                        <small>ID Reserva: ${b.id}</small>
                    </div>
                    <div class="booking-actions">
                        <button class="btn-edit" onclick="alert('Funcionalidade de edição em desenvolvimento')">Editar Reserva</button>
                        <button class="btn-cancel" onclick="cancelBooking('${b.id}')">Cancelar Reserva</button>
                    </div>
                `;
                container.appendChild(div);
            });
        });
    }

    window.cancelBooking = function(id) {
        if(!confirm('Tem certeza que deseja cancelar esta reserva?')) return;
        
        fetch(`/booking/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + localStorage.getItem('authToken') }
        }).then(res => {
            if(res.ok) {
                alert('Reserva cancelada.');
                loadBookings();
            } else {
                alert('Erro ao cancelar.');
            }
        });
    };
});