// =====================
// CART SYSTEM
// =====================
let cart = new Map();

document.addEventListener("DOMContentLoaded", function () {
    if (existingItems && existingItems.length > 0) {
        existingItems.forEach(i => {
            cart.set(String(i.dish.id), {
                name: i.dish.name,
                price: i.dish.price,
                quantity: i.quantity
            });
        });

        updateCart();
    }
});

function addToCart(el) { // name, price
    const id = String(el.dataset.id);

    if (cart.has(id)) {
        cart.get(id).quantity++;
    } else {
        cart.set(id, {
            name: el.dataset.name,
            price: Number(el.dataset.price),
            quantity: 1
        });
    }

    updateCart();
}

function updateCart() {
    const container = document.getElementById("cartItems");
    const inputs = document.getElementById("orderInputs");
    if (!inputs || !container) {
        return;
    }

    container.innerHTML = "";
    inputs.innerHTML = "";

    let total = 0;
    let index = 0;

    cart.forEach((item, id) => {
        // console.log(id + ": " + item.price + " euro " + item.quantity + " ")

        total += item.price * item.quantity;

        container.innerHTML += `
            <div class="item">
                <div>
                    <div>${item.name}</div>
                    <div class="price">${item.price} €</div>
                </div>
                
                <div>
                    <button type="button" class="btn" onclick="decrease(${id})">-</button>
                    <span>${item.quantity}</span>
                    <button type="button" class="btn" onclick="increase(${id})">+</button>
                </div>
            </div>
        `;

        inputs.innerHTML += `
            <input type="hidden" name="items[${index}].dish.id" value="${id}">
            <input type="hidden" name="items[${index}].quantity" value="${item.quantity}">
        `;

        index++;
    });

    const totalPrice = document.getElementById("totalPrice");

    if (totalPrice) {
        totalPrice.innerText = total.toFixed(2) + " €";
    }
}

function increase(id) {
    const item = cart.get(String(id));
    item.quantity++;
    updateCart();
}

function decrease(id) {
    const item = cart.get(String(id));
    item.quantity--;

    if (item.quantity <= 0) {
        cart.delete(String(id));
    }

    updateCart();
}

function clearCart() {
    cart.clear();
    updateCart();
}