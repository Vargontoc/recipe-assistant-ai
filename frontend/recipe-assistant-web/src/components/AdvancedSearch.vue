<template>
  <div class="advanced-search">
    <div class="search-header">
      <h2 class="text-xl font-semibold mb-4">Búsqueda Avanzada</h2>
      <button
        @click="resetForm"
        class="text-sm text-blue-600 hover:text-blue-800"
      >
        Limpiar filtros
      </button>
    </div>

    <form @submit.prevent="performSearch" class="space-y-4">
      <!-- Basic Search Query -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Búsqueda general
        </label>
        <input
          v-model="searchForm.query"
          type="text"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="Buscar por título, descripción o etiquetas..."
        />
      </div>

      <!-- Ingredients Section -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- Included Ingredients -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Ingredientes incluidos
          </label>
          <div class="ingredient-input">
            <input
              v-model="includedIngredientInput"
              @keyup.enter="addIncludedIngredient"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Agregar ingrediente..."
            />
            <div v-if="searchForm.includedIngredients && searchForm.includedIngredients.length" class="flex flex-wrap gap-1 mt-2">
              <span
                v-for="(ingredient, index) in searchForm.includedIngredients"
                :key="index"
                class="inline-flex items-center gap-1 px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full"
              >
                {{ ingredient }}
                <button
                  @click="removeIncludedIngredient(index)"
                  class="text-green-600 hover:text-green-800"
                >
                  ✕
                </button>
              </span>
            </div>
          </div>
        </div>

        <!-- Excluded Ingredients -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Ingredientes excluidos
          </label>
          <div class="ingredient-input">
            <input
              v-model="excludedIngredientInput"
              @keyup.enter="addExcludedIngredient"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Excluir ingrediente..."
            />
            <div v-if="searchForm.excludedIngredients && searchForm.excludedIngredients.length" class="flex flex-wrap gap-1 mt-2">
              <span
                v-for="(ingredient, index) in searchForm.excludedIngredients"
                :key="index"
                class="inline-flex items-center gap-1 px-2 py-1 bg-red-100 text-red-800 text-xs rounded-full"
              >
                {{ ingredient }}
                <button
                  @click="removeExcludedIngredient(index)"
                  class="text-red-600 hover:text-red-800"
                >
                  ✕
                </button>
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- Diet and Allergens -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- Diet -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Dietas
          </label>
          <select
            v-model="selectedDiet"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">Todas las dietas</option>
            <option v-for="diet in filters?.availableDiets" :key="diet" :value="diet">
              {{ diet }}
            </option>
          </select>
        </div>

        <!-- Allergens -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Alérgenos a evitar
          </label>
          <div class="space-y-1 max-h-24 overflow-y-auto">
            <label
              v-for="allergen in filters?.availableAllergens"
              :key="allergen"
              class="flex items-center text-sm"
            >
              <input
                v-model="searchForm.allergens"
                :value="allergen"
                type="checkbox"
                class="mr-2 rounded"
              />
              {{ allergen }}
            </label>
          </div>
        </div>
      </div>

      <!-- Time and Servings -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- Cooking Time -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Tiempo de cocción (minutos)
          </label>
          <div class="flex gap-2 items-center">
            <input
              v-model.number="searchForm.minCookingTime"
              type="number"
              :min="filters?.minCookingTime || 0"
              :max="filters?.maxCookingTime || 500"
              class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Min"
            />
            <span class="text-gray-500">-</span>
            <input
              v-model.number="searchForm.maxCookingTime"
              type="number"
              :min="filters?.minCookingTime || 0"
              :max="filters?.maxCookingTime || 500"
              class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Max"
            />
          </div>
        </div>

        <!-- Servings -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Porciones
          </label>
          <div class="flex gap-2 items-center">
            <input
              v-model.number="searchForm.minServings"
              type="number"
              :min="filters?.minServings || 1"
              :max="filters?.maxServings || 20"
              class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Min"
            />
            <span class="text-gray-500">-</span>
            <input
              v-model.number="searchForm.maxServings"
              type="number"
              :min="filters?.minServings || 1"
              :max="filters?.maxServings || 20"
              class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Max"
            />
          </div>
        </div>
      </div>

      <!-- Rating -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">
          Calificación mínima
        </label>
        <div class="flex items-center gap-2">
          <input
            v-model.number="searchForm.minRating"
            type="range"
            min="0"
            max="5"
            step="0.5"
            class="flex-1"
          />
          <span class="text-sm text-gray-600 w-12">
            {{ searchForm.minRating || 0 }}⭐
          </span>
        </div>
      </div>

      <!-- Sort Options -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Ordenar por
          </label>
          <select
            v-model="searchForm.sortBy"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="rating">Calificación</option>
            <option value="title">Título</option>
            <option value="cookingTime">Tiempo de cocción</option>
            <option value="createdAt">Fecha de creación</option>
          </select>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">
            Dirección
          </label>
          <select
            v-model="searchForm.sortDirection"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="desc">Descendente</option>
            <option value="asc">Ascendente</option>
          </select>
        </div>
      </div>

      <!-- Search Button -->
      <div class="flex gap-2">
        <button
          type="submit"
          :disabled="isSearching"
          class="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {{ isSearching ? 'Buscando...' : 'Buscar Recetas' }}
        </button>
        <button
          @click="toggleCollapsed"
          type="button"
          class="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300"
        >
          {{ isCollapsed ? 'Expandir' : 'Colapsar' }}
        </button>
      </div>
    </form>

    <!-- Search Results -->
    <div v-if="searchResults" class="search-results mt-6">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-lg font-semibold">
          Resultados ({{ searchResults.totalElements }} recetas)
        </h3>
        <button
          @click="clearResults"
          class="text-sm text-gray-600 hover:text-gray-800"
        >
          Limpiar resultados
        </button>
      </div>

      <!-- Results Grid -->
      <div v-if="searchResults.content.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        <div
          v-for="recipe in searchResults.content"
          :key="recipe.id"
          class="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
        >
          <h4 class="font-medium text-lg mb-2">{{ recipe.title }}</h4>
          <p class="text-gray-600 text-sm mb-2 line-clamp-2">{{ recipe.summary }}</p>
          <div class="flex justify-between items-center text-xs text-gray-500">
            <span>🕒 {{ recipe.cookingTime || 'N/A' }} min</span>
            <span>👥 {{ recipe.servings || 'N/A' }} porciones</span>
          </div>
          <router-link
            :to="`/recipe/${recipe.id}`"
            class="inline-block mt-2 text-blue-600 hover:text-blue-800 text-sm"
          >
            Ver receta →
          </router-link>
        </div>
      </div>

      <!-- No Results -->
      <div v-else class="text-center py-8 text-gray-500">
        <p>No se encontraron recetas con estos criterios.</p>
        <button
          @click="resetForm"
          class="mt-2 text-blue-600 hover:text-blue-800"
        >
          Limpiar filtros e intentar de nuevo
        </button>
      </div>

      <!-- Pagination -->
      <div v-if="searchResults.totalPages > 1" class="flex justify-center mt-6">
        <div class="flex gap-2">
          <button
            v-for="page in Math.min(searchResults.totalPages, 5)"
            :key="page"
            @click="searchPage(page - 1)"
            :class="[
              'px-3 py-1 rounded',
              page - 1 === currentPage
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            ]"
          >
            {{ page }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { searchService, type AdvancedSearchRequest, type SearchFilters } from '../services';

// Reactive state
const filters = ref<SearchFilters | null>(null);
const searchResults = ref<any>(null);
const isSearching = ref(false);
const isCollapsed = ref(false);
const currentPage = ref(0);

// Form inputs
const includedIngredientInput = ref('');
const excludedIngredientInput = ref('');
const selectedDiet = ref('');

// Search form
const searchForm = reactive<AdvancedSearchRequest>({
  query: '',
  includedIngredients: [],
  excludedIngredients: [],
  diets: [],
  allergens: [],
  minCookingTime: undefined,
  maxCookingTime: undefined,
  minServings: undefined,
  maxServings: undefined,
  minRating: 0,
  sortBy: 'rating',
  sortDirection: 'desc'
});

// Methods
const loadFilters = async () => {
  try {
    filters.value = await searchService.getSearchFilters();
  } catch (error) {
    console.error('Error loading search filters:', error);
  }
};

const addIncludedIngredient = () => {
  const ingredient = includedIngredientInput.value.trim();
  if (ingredient && searchForm.includedIngredients && !searchForm.includedIngredients.includes(ingredient)) {
    searchForm.includedIngredients.push(ingredient);
    includedIngredientInput.value = '';
  }
};

const addExcludedIngredient = () => {
  const ingredient = excludedIngredientInput.value.trim();
  if (ingredient && searchForm.excludedIngredients && !searchForm.excludedIngredients.includes(ingredient)) {
    searchForm.excludedIngredients.push(ingredient);
    excludedIngredientInput.value = '';
  }
};

const removeIncludedIngredient = (index: number) => {
  if (searchForm.includedIngredients) {
    searchForm.includedIngredients.splice(index, 1);
  }
};

const removeExcludedIngredient = (index: number) => {
  if (searchForm.excludedIngredients) {
    searchForm.excludedIngredients.splice(index, 1);
  }
};

const performSearch = async () => {
  isSearching.value = true;
  currentPage.value = 0;
  
  try {
    // Prepare search request
    const request: AdvancedSearchRequest = {
      ...searchForm,
      diets: selectedDiet.value ? [selectedDiet.value] : []
    };
    
    searchResults.value = await searchService.advancedSearch(request, 0, 12);
  } catch (error) {
    console.error('Error performing search:', error);
    alert('Error al realizar la búsqueda. Inténtalo de nuevo.');
  } finally {
    isSearching.value = false;
  }
};

const searchPage = async (page: number) => {
  if (page === currentPage.value) return;
  
  isSearching.value = true;
  currentPage.value = page;
  
  try {
    const request: AdvancedSearchRequest = {
      ...searchForm,
      diets: selectedDiet.value ? [selectedDiet.value] : []
    };
    
    searchResults.value = await searchService.advancedSearch(request, page, 12);
  } catch (error) {
    console.error('Error loading page:', error);
  } finally {
    isSearching.value = false;
  }
};

const resetForm = () => {
  Object.assign(searchForm, {
    query: '',
    includedIngredients: [],
    excludedIngredients: [],
    diets: [],
    allergens: [],
    minCookingTime: undefined,
    maxCookingTime: undefined,
    minServings: undefined,
    maxServings: undefined,
    minRating: 0,
    sortBy: 'rating',
    sortDirection: 'desc'
  });
  selectedDiet.value = '';
  includedIngredientInput.value = '';
  excludedIngredientInput.value = '';
};

const clearResults = () => {
  searchResults.value = null;
  currentPage.value = 0;
};

const toggleCollapsed = () => {
  isCollapsed.value = !isCollapsed.value;
};

// Load filters on mount
onMounted(() => {
  loadFilters();
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
