// Helpers compartidos por las paginas de la banca en linea
const Bank = {
    // Spring Security exige el token CSRF en cada POST/DELETE hecho con la sesion del usuario
    csrfHeaders() {
        const token = document.querySelector('meta[name="_csrf"]');
        const header = document.querySelector('meta[name="_csrf_header"]');
        return token && header ? { [header.content]: token.content } : {};
    },

    async api(method, url, body) {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json', ...this.csrfHeaders() },
            body: body === undefined ? undefined : JSON.stringify(body)
        });
        if (response.status === 401) {
            window.location.href = '/login';
            throw new Error('Session expired');
        }
        const data = response.status === 204 ? null : await response.json().catch(() => null);
        if (!response.ok) {
            throw new Error(data && data.error ? data.error : 'Unexpected error (' + response.status + ')');
        }
        return data;
    },

    money(value) {
        return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(Number(value));
    },

    dateTime(value) {
        return new Date(value).toLocaleString('en-US', { dateStyle: 'medium', timeStyle: 'short' });
    },

    showMessage(element, text, type) {
        element.className = 'alert alert-' + type + ' mt-3';
        element.textContent = text;
    },

    escape(text) {
        const div = document.createElement('div');
        div.textContent = text == null ? '' : String(text);
        return div.innerHTML;
    }
};
