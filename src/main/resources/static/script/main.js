// =====================
// DISH NEW
// =====================

// Save dish from create form
function saveDish(event) {
    event.preventDefault();

    const newDish = {
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: parseFloat(document.getElementById("price").value),
        type: document.getElementById("type").value
    };

    let dishes = JSON.parse(localStorage.getItem("dishes")) || [];
    let editDish = JSON.parse(localStorage.getItem("editDish"));

    if (editDish) {
        // UPDATE
        dishes = dishes.map(d => {
            if (d.name === editDish.name) {
                return newDish;
            }
            return d;
        });
        localStorage.removeItem("editDish");
    } else {
        // CREATE
        dishes.push(newDish);
    }

    localStorage.setItem("dishes", JSON.stringify(dishes));

    window.location.href = "list.html";
}

// =====================
// Delete
// =====================
function deleteDish(name) {
    const confirmDelete = confirm("Êtes-vous sûr de vouloir supprimer cet article ?");

    if (!confirmDelete) return;

    let dishes = JSON.parse(localStorage.getItem("dishes")) || [];

    dishes = dishes.filter(d => d.name !== name);

    localStorage.setItem("dishes", JSON.stringify(dishes));

    location.reload();
}

function loadDishForEdit() {
    const dish = JSON.parse(localStorage.getItem("editDish"));

    if (!dish) return;

    // Fill form
    document.getElementById("name").value = dish.name;
    document.getElementById("description").value = dish.description;
    document.getElementById("price").value = dish.price;
    document.getElementById("type").value = dish.type;

    // CHANGE BUTTON TEXT
    document.querySelector("button[type='submit']").innerText = "Modifier";
}

function editDish(name) {
    let dishes = JSON.parse(localStorage.getItem("dishes")) || [];

    const dish = dishes.find(d => d.name === name);

    if (!dish) {
        alert("Ce plat n'est pas modifiable (pas encore enregistré).");
        return;
    }

    localStorage.setItem("editDish", JSON.stringify(dish));

    window.location.href = "form_remove.html";
}

function deleteOrder(id) {
    const confirmDelete = confirm("Êtes-vous sûr de vouloir supprimer cette commande ?");

    if (!confirmDelete) return;

    console.log("Commande supprimée:", id);

    // later → backend delete
}

// =====================
// MODAL (CONFIRM DELETE)
// =====================

function showConfirmModal(message, onConfirm) {
    document.getElementById("confirmMessage").innerText = message;

    const modal = document.getElementById("confirmModal");
    modal.style.display = "flex";

    document.getElementById("confirmYes").onclick = function () {
        onConfirm();
        closeModal();
    };
}

function closeModal() {
    document.getElementById("confirmModal").style.display = "none";
}

function editOrder(id) {
    window.location.href = "/temp/order/form_remove.html?id=" + id;
}