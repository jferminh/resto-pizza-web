/**
 * Ouvre la modale et configure l'URL de suppression
 */
function openDeleteModal(id, name, endpoint) {
    document.getElementById('modalName').textContent = name;

    const btn = document.getElementById('confirmDeleteBtn');

    btn.onclick = async function () {
        try {
            const response = await fetch(endpoint + id, {
                method: 'DELETE'
            });

            if (response.ok) {
                closeDeleteModal();
                window.location.reload();
            } else {
                console.error('Delete failed');
            }
        } catch (err) {
            console.error('Error:', err);
        }
    };

    document.getElementById('deleteModal').style.display = 'flex';
}

function openDeleteModalClient(buttonElement) {
    const id   = buttonElement.getAttribute('data-id');
    const name = buttonElement.getAttribute('data-name');
    openDeleteModal(id, name, 'clients/');
}

function openDeleteModalDish(button) {
    const id   = button.getAttribute('data-id');
    const name = button.getAttribute('data-name');

    document.getElementById('modalDishName').textContent = name;
    document.getElementById('deleteDishForm').action = '/dishes/delete/' + id;

    document.getElementById('deleteModalDish').style.display = 'flex';
}


function openDeleteModalOrder(buttonElement) {
    const id = buttonElement.getAttribute('data-id');
    openDeleteModal(id, '', 'orders/');
}

/**
 * Ferme la modale
 */
function closeDeleteModal() {
    document.getElementById('deleteModal').style.display = 'none';
}

function closeDeleteModalDish() {
    document.getElementById('deleteModalDish').style.display = 'none';
}

// Fermer en cliquant sur le fond
document.getElementById('deleteModalDish').addEventListener('click', function(e) {
    if (e.target === this) closeDeleteModalDish();
});