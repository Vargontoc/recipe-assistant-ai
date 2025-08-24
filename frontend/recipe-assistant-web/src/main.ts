import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import './assets/tailwind.css'
import { createPinia } from 'pinia'
import router from './router'

createApp(App).use(createPinia()).use(router).mount('#app')
