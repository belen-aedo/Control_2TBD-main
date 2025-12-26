import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAuthStore = defineStore('auth', () => {
    const token = ref(localStorage.getItem('token'));
    const usuario = ref(JSON.parse(localStorage.getItem('usuario')));

    function setAuth(newToken, newUsuario) {
        token.value = newToken;
        usuario.value = newUsuario;
        localStorage.setItem('token', newToken);
        localStorage.setItem('usuario', JSON.stringify(newUsuario));
    }

    function logout() {
        token.value = null;
        usuario.value = null;
        localStorage.removeItem('token');
        localStorage.removeItem('usuario');
        // Redirigir al login
        window.location.href = '/login';
    }

    return { token, usuario, setAuth, logout };
});