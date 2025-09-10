<template>
  <div class="favorites-view">
    <!-- Header -->
    <div class="header-section bg-white shadow-sm">
      <div class="max-w-6xl mx-auto px-6 py-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Mis Recetas Favoritas</h1>
        <p class="text-gray-600">Encuentra todas tus recetas guardadas en un solo lugar</p>
      </div>
    </div>

    <!-- Content -->
    <div class="max-w-6xl mx-auto px-6 py-8">
      <!-- Loading State -->
      <LoadingState v-if="isLoading" message="Cargando favoritos..." />

      <!-- Error State -->
      <ErrorState 
        v-else-if="error" 
        :message="error" 
        @retry="loadFavorites" 
      />

      <!-- No Favorites State -->
      <EmptyState 
        v-else-if="!favorites.length"
        message="Aún no tienes recetas favoritas"
        description="Explora recetas y guarda las que más te gusten usando el botón de corazón"
        icon="💝"
      >
        <template #action>
          <router-link
            to="/search"
            class="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
              <path d="M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z"/>
            </svg>
            Buscar recetas
          </router-link>
        </template>
      </EmptyState>

      <!-- Favorites Grid -->
      <div v-else>
        <!-- Stats Bar -->
        <div class="stats-bar mb-6 p-4 bg-blue-50 rounded-lg">
          <div class="flex items-center justify-between">
            <div class="flex items-center gap-4">
              <div class="text-sm text-gray-600">
                <span class="font-semibold text-gray-900">{{ favorites.length }}</span> 
                {{ favorites.length === 1 ? 'receta favorita' : 'recetas favoritas' }}
              </div>
            </div>
            
            <!-- Clear All Button -->
            <button
              @click="showClearAllModal = true"
              class="text-sm text-red-600 hover:text-red-700 font-medium"
            >
              Limpiar todo
            </button>
          </div>
        </div>

        <!-- Recipe Grid -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="favorite in favorites"
            :key="favorite.id"
            class="recipe-card bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow"
          >
            <!-- Recipe Content -->
            <div class="p-4">
              <div class="flex justify-between items-start mb-3">
                <h3 class="text-lg font-semibold text-gray-900 line-clamp-2">
                  {{ favorite.recipe.title }}
                </h3>
                <FavoriteButton 
                  :recipe-id="favorite.recipe.id" 
                  :is-favorite="true"
                  @favoriteChanged="handleFavoriteChanged"
                />
              </div>

              <p v-if="favorite.recipe.summary" class="text-gray-600 text-sm mb-3 line-clamp-2">
                {{ favorite.recipe.summary }}
              </p>

              <!-- Recipe Meta -->
              <div class="flex flex-wrap gap-3 text-xs text-gray-500 mb-4">
                <div v-if="favorite.recipe.cookingTime" class="flex items-center gap-1">
                  <span>🕒</span>
                  <span>{{ favorite.recipe.cookingTime }}min</span>
                </div>
                <div v-if="favorite.recipe.servings" class="flex items-center gap-1">
                  <span>👥</span>
                  <span>{{ favorite.recipe.servings }}</span>
                </div>
                <div v-if="favorite.recipe.diet" class="flex items-center gap-1">
                  <span>🥗</span>
                  <span>{{ favorite.recipe.diet }}</span>
                </div>
              </div>

              <!-- Favorite Date -->
              <div class="text-xs text-gray-400 mb-4">
                Guardado el {{ formatDate(favorite.createdAt) }}
              </div>

              <!-- Actions -->
              <div class="flex gap-2">
                <router-link
                  :to="`/recipes/${favorite.recipe.id}`"
                  class="flex-1 px-3 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700 transition-colors text-center"
                >
                  Ver receta
                </router-link>
                <button
                  @click="removeFavorite(favorite.recipe.id)"
                  class="px-3 py-2 bg-red-100 text-red-600 text-sm rounded-md hover:bg-red-200 transition-colors"
                  :disabled="removingFavorites.has(favorite.recipe.id)"
                >
                  {{ removingFavorites.has(favorite.recipe.id) ? '...' : 'Quitar' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Clear All Confirmation Modal -->
    <div
      v-if="showClearAllModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      @click.self="showClearAllModal = false"
    >
      <div class="bg-white rounded-lg p-6 max-w-md mx-4">
        <h3 class="text-lg font-semibold text-gray-900 mb-2">
          Confirmar eliminación
        </h3>
        <p class="text-gray-600 mb-6">
          ¿Estás seguro de que quieres eliminar todas tus recetas favoritas? Esta acción no se puede deshacer.
        </p>
        <div class="flex gap-3 justify-end">
          <button
            @click="showClearAllModal = false"
            class="px-4 py-2 text-gray-600 hover:text-gray-700"
          >
            Cancelar
          </button>
          <button
            @click="clearAllFavorites"
            class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
            :disabled="isClearingAll"
          >
            {{ isClearingAll ? 'Eliminando...' : 'Eliminar todo' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { favoritesService } from '../services';
import LoadingState from '../components/LoadingState.vue';
import ErrorState from '../components/ErrorState.vue';
import EmptyState from '../components/EmptyState.vue';
import FavoriteButton from '../components/FavoriteButton.vue';

// Reactive state
const favorites = ref<any[]>([]);
const isLoading = ref(true);
const error = ref<string | null>(null);
const removingFavorites = ref(new Set<number>());
const showClearAllModal = ref(false);
const isClearingAll = ref(false);

// Methods
const loadFavorites = async () => {
  isLoading.value = true;
  error.value = null;

  try {
    const response = await favoritesService.getUserFavorites();
    favorites.value = response.data;
  } catch (err: any) {
    console.error('Error loading favorites:', err);
    error.value = err.response?.data?.message || 'Error al cargar los favoritos';
  } finally {
    isLoading.value = false;
  }
};

const removeFavorite = async (recipeId: number) => {
  removingFavorites.value.add(recipeId);

  try {
    await favoritesService.removeFavorite(recipeId.toString());
    // Remove from local list
    favorites.value = favorites.value.filter(fav => fav.recipe.id !== recipeId);
  } catch (err: any) {
    console.error('Error removing favorite:', err);
    // Could show a toast notification here
  } finally {
    removingFavorites.value.delete(recipeId);
  }
};

const clearAllFavorites = async () => {
  isClearingAll.value = true;

  try {
    // Remove all favorites
    await Promise.all(
      favorites.value.map(fav => favoritesService.removeFavorite(fav.recipe.id.toString()))
    );
    favorites.value = [];
    showClearAllModal.value = false;
  } catch (err: any) {
    console.error('Error clearing all favorites:', err);
    error.value = 'Error al eliminar los favoritos';
  } finally {
    isClearingAll.value = false;
  }
};

const handleFavoriteChanged = (recipeId: number, isFavorite: boolean) => {
  if (!isFavorite) {
    // Remove from local list when unfavorited
    favorites.value = favorites.value.filter(fav => fav.recipe.id !== recipeId);
  }
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

// Load favorites on mount
onMounted(() => {
  loadFavorites();
});
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
