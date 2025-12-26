import apiClient from '@/core/api/axios';

export default {
    obtenerTodos() {
        return apiClient.get('/sectores');
    },
    obtenerPorId(id) {
        return apiClient.get(`/sectores/${id}`);
    }
};