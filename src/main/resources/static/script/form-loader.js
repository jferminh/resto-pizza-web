document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('sendForm');
    const info = document.getElementById('formInfoMessage');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        info.style.display = 'block';

        // PUT/PATCH/POST
        const method = form.dataset.method || form.method;
        const action = form.action;

        const formData = new FormData(form);
        const body = Object.fromEntries(formData.entries());

        try {
            const res = await fetch(action, {
                method: method.toUpperCase(),
                body: formData,
                redirect: 'manual'
            });

            if (!res.ok && !res.redirected) {
                throw new Error('Erreur de réseau');
            }

            if (res.redirected) {
                window.location.href = res.url;
            }
        } catch (err) {
            // or redirect?
            info.textContent = 'Error: ' + err; // set text
        } finally {
            info.style.display = 'none';
            // redirect?
        }
    });
});