import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SuggestView from '../views/SuggestView.vue'
import HistoryView from '../views/HistoryView.vue'
import PreferencesView from '../views/PreferencesView.vue'



const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/suggest', name: 'suggest', component: SuggestView },
    { path: '/history', name: 'history', component: HistoryView },
    { path: '/preferences', name: 'preferences', component: PreferencesView },
  ],
})

export default router