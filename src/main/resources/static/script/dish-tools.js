// filtrage des plats
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

document.addEventListener('DOMContentLoaded', () => {
    const btnPizza = document.getElementById('btnPizzaFilter');
    const btnDrink = document.getElementById('btnDrinkFilter');
    const btnDessert = document.getElementById('btnDessertFilter');

    btnPizza.addEventListener('click', () => {
        filterItems('pizza');
    });

    btnDrink.addEventListener('click', () => {
        filterItems('boisson');
    });

    btnDessert.addEventListener('click', () => {
        filterItems('dessert');
    });

    filterItems('pizza');
});