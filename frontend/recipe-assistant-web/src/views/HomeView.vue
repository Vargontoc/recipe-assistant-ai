<template>
  <div class="home-view">
    <!-- Hero Section -->
    <div class="hero-section bg-gradient-to-r from-blue-600 to-blue-800 text-white py-12 mb-8 rounded-lg">
      <div class="text-center">
        <h1 class="text-4xl font-bold mb-4">¡Bienvenido a Recipe Assistant AI!</h1>
        <p class="text-xl mb-6 text-blue-100">
          Descubre, guarda y califica tus recetas favoritas
        </p>
        
        <!-- Quick Actions -->
        <div class="flex flex-wrap justify-center gap-4 max-w-2xl mx-auto">
          <router-link
            to="/search"
            class="inline-flex items-center gap-2 px-6 py-3 bg-white text-blue-600 rounded-lg font-semibold hover:bg-blue-50 transition-colors"
          >
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
              <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
            </svg>
            Búsqueda Avanzada
          </router-link>
          
          <router-link
            to="/suggest"
            class="inline-flex items-center gap-2 px-6 py-3 bg-green-600 text-white rounded-lg font-semibold hover:bg-green-700 transition-colors"
          >
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
              <path d="M9 11H7v2h2v-2zm4 0h-2v2h2v-2zm4 0h-2v2h2v-2zm2-7h-1V2h-2v2H8V2H6v2H5c-1.1 0-1.99.9-1.99 2L3 20c0 1.1.89 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H5V9h14v11z"/>
            </svg>
            Sugerir Receta
          </router-link>
          
          <router-link
            to="/favorites"
            class="inline-flex items-center gap-2 px-6 py-3 bg-red-500 text-white rounded-lg font-semibold hover:bg-red-600 transition-colors"
          >
            <span class="text-lg">💝</span>
            Mis Favoritos
          </router-link>
        </div>
      </div>
    </div>

    <!-- Main Content -->
    <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
      <!-- Main Content -->
      <section class="lg:col-span-3 space-y-6">
        <!-- Search Bar -->
        <div class="search-section bg-white p-6 rounded-lg shadow-md">
          <div class="flex flex-col sm:flex-row sm:items-center gap-3">
            <h2 class="text-2xl font-bold flex-1">Explorar Recetas</h2>
            <div class="flex items-center gap-2 w-full sm:w-80">
              <input 
                v-model="search" 
                @keyup.enter="applySearch" 
                type="text" 
                placeholder="Buscar por título o tag..." 
                class="w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500" 
              />
              <button 
                @click="applySearch"
                class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                Buscar
              </button>
            </div>
          </div>
        </div>

        <!-- Loading State -->
        <LoadingState v-if="loading" message="Cargando recetas..." />

        <!-- Results -->
        <div v-else>
          <!-- No Results -->
          <EmptyState 
            v-if="items.length === 0"
            message="No se encontraron recetas"
            description="Intenta con otros términos de búsqueda o explora nuestras sugerencias"
            icon="🔍"
          >
            <template #action>
              <div class="flex gap-3">
                <router-link
                  to="/search"
                  class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Búsqueda avanzada
                </router-link>
                <router-link
                  to="/suggest"
                  class="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
                >
                  Sugerir receta
                </router-link>
              </div>
            </template>
          </EmptyState>

          <!-- Recipe Grid -->
          <div v-else class="space-y-6">
            <!-- Results Info -->
            <div class="flex items-center justify-between">
              <div class="text-sm text-gray-600">
                Mostrando {{ items.length }} recetas
                <span v-if="search">para "<span class="font-semibold">{{ search }}</span>"</span>
              </div>
              <button
                v-if="search"
                @click="clearSearch"
                class="text-sm text-blue-600 hover:text-blue-700 font-medium"
              >
                Limpiar búsqueda
              </button>
            </div>

            <!-- Grid -->
            <div class="grid gap-6 sm:grid-cols-2 xl:grid-cols-3">
              <recipe-card 
                v-for="recipe in items" 
                :key="recipe.id" 
                :recipe="recipe"
                class="hover:shadow-lg transition-shadow"
              />
            </div>

            <!-- Pagination -->
            <div class="mt-8" v-if="totalPages > 1">
              <pagination-component 
                :page="page" 
                :total-pages="totalPages" 
                @change="goTo"
              />
            </div>
          </div>
        </div>
      </section>

      <!-- Sidebar -->
      <aside class="lg:col-span-1">
        <div class="sticky top-24">
          <UserStatsWidget />
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRecipesStore } from '../stores/RecipeStore';
import { storeToRefs } from 'pinia';
import RecipeCard from '../components/RecipeCard.vue';
import PaginationComponent from '../components/PaginationComponent.vue';
import LoadingState from '../components/LoadingState.vue';
import EmptyState from '../components/EmptyState.vue';
import UserStatsWidget from '../components/UserStatsWidget.vue';

const store = useRecipesStore();
const search = ref(store.q);

const applySearch = () => store.setQuery(search.value || '');
const clearSearch = () => {
  search.value = '';
  store.setQuery('');
};
const goTo = (p: number) => store.goTo(p);

onMounted(() => {
  if (!store.items.length) store.fetch();
});

watch(() => store.size, () => store.goTo(0));

const { items, loading, totalPages, page } = storeToRefs(store);
</script>