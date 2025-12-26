import apiClient from '@/core/api/axios';

export default {
    login(credentials) {
        return apiClient.post('/auth/login', credentials);
    },
    registro(userData) {
        return apiClient.post('/auth/registro', userData);
    }
};