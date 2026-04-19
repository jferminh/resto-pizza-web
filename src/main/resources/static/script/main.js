// FILTER (your original — unchanged)
function filterItems(category) {
    const cards = document.querySelectorAll('.card');

    cards.forEach(card => {
        if (category === 'all') {
            card.style.display = 'block';
        } else {
            if (card.classList.contains(category)) {
                card.style.display = 'block';
            } else {
                card.style.display = 'none';
            }
        }
    });
}

// CART SYSTEM
let cart = [];

function addToCart(name, price) {
    const existing = cart.find(item => item.name === name);

    if (existing) {
        existing.quantity++;
    } else {
        cart.push({ name, price, quantity: 1 });
    }

    renderCart();
}

function renderCart() {
    const container = document.getElementById("cart-items");
    if (!container) return; // prevents crash

    container.innerHTML = "";

    let subtotal = 0;

    cart.forEach((item, index) => {
        subtotal += item.price * item.quantity;

        container.innerHTML += `
            <div class="item">
                <div>
                    <div>${item.name}</div>
                    <div class="price">${item.price.toFixed(2)} €</div>
                </div>
                <div class="quantity">
                    <button onclick="decrease(${index})">-</button>
                    <span>${item.quantity}</span>
                    <button onclick="increase(${index})">+</button>
                </div>
            </div>
        `;
    });

    const tax = subtotal * 0.10;
    const total = subtotal + tax;

    document.getElementById("subtotal").innerText = subtotal.toFixed(2) + " €";
    document.getElementById("tax").innerText = tax.toFixed(2) + " €";
    document.getElementById("total").innerText = total.toFixed(2) + " €";
}

function increase(index) {
    cart[index].quantity++;
    renderCart();
}

function decrease(index) {
    cart[index].quantity--;

    if (cart[index].quantity <= 0) {
        cart.splice(index, 1);
    }

    renderCart();
}

function clearCart() {
    cart = [];
    renderCart();
}

// INIT
window.onload = function () {
    filterItems('pizza');
};