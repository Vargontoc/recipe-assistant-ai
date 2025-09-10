import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SuggestView from '../views/SuggestView.vue'
import HistoryView from '../views/HistoryView.vue'
import PreferencesView from '../views/PreferencesView.vue'
import RecipeDetailsView from '../views/RecipeDetailsView.vue'
import FavoritesView from '../views/FavoritesView.vue'
import AdvancedSearchView from '../views/AdvancedSearchView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/recipes/:id', name: "recipe-detail", component: RecipeDetailsView},
    { path: '/suggest', name: 'suggest', component: SuggestView },
    { path: '/search', name: 'search', component: AdvancedSearchView },
    { path: '/favorites', name: 'favorites', component: FavoritesView },
    { path: '/history', name: 'history', component: HistoryView },
    { path: '/preferences', name: 'preferences', component: PreferencesView },
    // Legacy route redirect
    { path: '/recipe/:id', redirect: to => `/recipes/${to.params.id}` },
  ],
})

export default router