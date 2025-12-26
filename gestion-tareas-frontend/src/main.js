import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import App from './App.vue'
import router from './core/router'
import 'leaflet/dist/leaflet.css';

const app = createApp(App)

app.use(createPinia()) // Usar Pinia
app.use(router)        // Usar Router
app.mount('#app')