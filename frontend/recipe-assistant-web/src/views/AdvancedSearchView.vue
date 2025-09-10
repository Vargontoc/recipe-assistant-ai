<template>
  <div class="advanced-search-view">
    <!-- Header -->
    <div class="header-section bg-white shadow-sm">
      <div class="max-w-6xl mx-auto px-6 py-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Búsqueda Avanzada</h1>
        <p class="text-gray-600">Encuentra la receta perfecta con filtros específicos</p>
      </div>
    </div>

    <!-- Content -->
    <div class="max-w-6xl mx-auto px-6 py-8">
      <!-- Advanced Search Component -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-8">
        <AdvancedSearch @search="handleSearch" />
      </div>

      <!-- Results Section -->
      <div v-if="hasSearched">
        <!-- Loading State -->
        <LoadingState v-if="isLoading" message="Buscando recetas..." />

        <!-- Error State -->
        <ErrorState 
          v-else-if="error" 
          :message="error" 
          @retry="retrySearch" 
        />

        <!-- No Results State -->
        <EmptyState 
          v-else-if="!results.length"
          message="No se encontraron recetas"
          description="Intenta ajustar tus filtros de búsqueda para obtener más resultados"
          icon="🔍"
        >
          <template #action>
            <button
              @click="clearSearch"
              class="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              <svg class="w-5 h-5" viewBox="0 0 24 24" fill="currentColor">
                <path d="M4 6h16v2H4zm2 5h12v2H6zm3 5h6v2H9z"/>
              </svg>
              Limpiar filtros
            </button>
          </template>
        </EmptyState>

        <!-- Results Grid -->
        <div v-else>
          <!-- Results Header -->
          <div class="results-header mb-6">
            <div class="flex items-center justify-between">
              <div class="text-lg font-semibold text-gray-900">
                {{ totalResults }} {{ totalResults === 1 ? 'receta encontrada' : 'recetas encontradas' }}
              </div>
              
              <!-- Sort Options -->
              <div class="flex items-center gap-2">
                <label class="text-sm text-gray-600">Ordenar por:</label>
                <select
                  v-model="sortBy"
                  @change="handleSortChange"
                  class="text-sm border border-gray-300 rounded-md px-3 py-1 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="relevance">Relevancia</option>
                  <option value="title">Nombre</option>
                  <option value="cookingTime">Tiempo de cocción</option>
                  <option value="rating">Valoración</option>
                </select>
              </div>
            </div>
          </div>

          <!-- Recipe Grid -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
            <div
              v-for="recipe in results"
              :key="recipe.id"
              class="recipe-card bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow"
            >
              <div class="p-4">
                <div class="flex justify-between items-start mb-3">
                  <h3 class="text-lg font-semibold text-gray-900 line-clamp-2">
                    {{ recipe.title }}
                  </h3>
                  <FavoriteButton :recipe-id="recipe.id" />
                </div>

                <p v-if="recipe.summary" class="text-gray-600 text-sm mb-3 line-clamp-2">
                  {{ recipe.summary }}
                </p>

                <!-- Recipe Meta -->
                <div class="flex flex-wrap gap-3 text-xs text-gray-500 mb-4">
                  <div v-if="recipe.cookingTime" class="flex items-center gap-1">
                    <span>🕒</span>
                    <span>{{ recipe.cookingTime }}min</span>
                  </div>
                  <div v-if="recipe.servings" class="flex items-center gap-1">
                    <span>👥</span>
                    <span>{{ recipe.servings }}</span>
                  </div>
                  <div v-if="recipe.diet" class="flex items-center gap-1">
                    <span>🥗</span>
                    <span>{{ recipe.diet }}</span>
                  </div>
                  <div v-if="recipe.averageRating" class="flex items-center gap-1">
                    <span>⭐</span>
                    <span>{{ recipe.averageRating.toFixed(1) }}</span>
                  </div>
                </div>

                <!-- Matching Criteria -->
                <div v-if="recipe.matchingCriteria && recipe.matchingCriteria.length" class="mb-4">
                  <div class="text-xs text-gray-500 mb-1">Coincidencias:</div>
                  <div class="flex flex-wrap gap-1">
                    <span
                      v-for="criteria in recipe.matchingCriteria.slice(0, 3)"
                      :key="criteria"
                      class="px-2 py-1 bg-green-100 text-green-700 text-xs rounded-full"
                    >
                      {{ criteria }}
                    </span>
                    <span
                      v-if="recipe.matchingCriteria.length > 3"
                      class="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded-full"
                    >
                      +{{ recipe.matchingCriteria.length - 3 }} más
                    </span>
                  </div>
                </div>

                <!-- Actions -->
                <div class="flex gap-2">
                  <router-link
                    :to="`/recipes/${recipe.id}`"
                    class="flex-1 px-3 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700 transition-colors text-center"
                  >
                    Ver receta
                  </router-link>
                </div>
              </div>
            </div>
          </div>

          <!-- Pagination -->
          <div v-if="totalPages > 1" class="pagination flex justify-center items-center gap-2">
            <button
              @click="goToPage(currentPage - 1)"
              :disabled="currentPage <= 1"
              class="px-3 py-2 text-sm bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Anterior
            </button>

            <div class="flex gap-1">
              <button
                v-for="page in visiblePages"
                :key="page"
                @click="goToPage(page)"
                :class="[
                  'px-3 py-2 text-sm rounded-md',
                  page === currentPage
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                ]"
              >
                {{ page }}
              </button>
            </div>

            <button
              @click="goToPage(currentPage + 1)"
              :disabled="currentPage >= totalPages"
              class="px-3 py-2 text-sm bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Siguiente
            </button>
          </div>
        </div>
      </div>

      <!-- Initial State -->
      <div v-else class="text-center py-12">
        <div class="text-6xl mb-4">🔍</div>
        <h2 class="text-xl font-semibold text-gray-900 mb-2">
          Configura tus filtros de búsqueda
        </h2>
        <p class="text-gray-600">
          Usa los filtros de arriba para encontrar exactamente lo que buscas
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { searchService } from '../services';
import AdvancedSearch from '../components/AdvancedSearch.vue';
import LoadingState from '../components/LoadingState.vue';
import ErrorState from '../components/ErrorState.vue';
import EmptyState from '../components/EmptyState.vue';
import FavoriteButton from '../components/FavoriteButton.vue';

// Reactive state
const results = ref<any[]>([]);
const isLoading = ref(false);
const error = ref<string | null>(null);
const hasSearched = ref(false);
const currentPage = ref(1);
const totalResults = ref(0);
const totalPages = ref(0);
const sortBy = ref('relevance');
const lastSearchParams = ref<any>(null);

// Computed
const visiblePages = computed(() => {
  const pages = [];
  const start = Math.max(1, currentPage.value - 2);
  const end = Math.min(totalPages.value, currentPage.value + 2);
  
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  
  return pages;
});

// Methods
const handleSearch = async (searchParams: any) => {
  lastSearchParams.value = { ...searchParams, page: 1, sortBy: sortBy.value };
  currentPage.value = 1;
  await performSearch();
};

const performSearch = async () => {
  if (!lastSearchParams.value) return;

  isLoading.value = true;
  error.value = null;
  hasSearched.value = true;

  try {
    const params = {
      ...lastSearchParams.value,
      page: currentPage.value - 1, // Backend expects 0-based pagination
      sortBy: sortBy.value
    };

    const response = await searchService.advancedSearch(params);
    
    results.value = response.data.content || [];
    totalResults.value = response.data.totalElements || 0;
    totalPages.value = response.data.totalPages || 0;
  } catch (err: any) {
    console.error('Error performing search:', err);
    error.value = err.response?.data?.message || 'Error al realizar la búsqueda';
    results.value = [];
    totalResults.value = 0;
    totalPages.value = 0;
  } finally {
    isLoading.value = false;
  }
};

const retrySearch = () => {
  if (lastSearchParams.value) {
    performSearch();
  }
};

const clearSearch = () => {
  results.value = [];
  hasSearched.value = false;
  error.value = null;
  currentPage.value = 1;
  totalResults.value = 0;
  totalPages.value = 0;
  lastSearchParams.value = null;
  sortBy.value = 'relevance';
};

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value && page !== currentPage.value) {
    currentPage.value = page;
    performSearch();
  }
};

const handleSortChange = () => {
  if (lastSearchParams.value) {
    lastSearchParams.value.sortBy = sortBy.value;
    currentPage.value = 1;
    performSearch();
  }
};
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
