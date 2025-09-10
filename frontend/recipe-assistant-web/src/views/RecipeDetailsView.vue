<template>
  <div class="recipe-details">
    <!-- Loading State -->
    <LoadingState v-if="isLoading" message="Cargando receta..." />
    
    <!-- Error State -->
    <ErrorState 
      v-else-if="error" 
      :message="error" 
      @retry="loadRecipe" 
    />
    
    <!-- Recipe Content -->
    <div v-else-if="recipe" class="max-w-4xl mx-auto p-6">
      <!-- Header -->
      <div class="recipe-header mb-6">
        <div class="flex justify-between items-start mb-4">
          <div class="flex-1">
            <h1 class="text-3xl font-bold text-gray-900 mb-2">{{ recipe.title }}</h1>
            <p v-if="recipe.summary" class="text-gray-600 text-lg">{{ recipe.summary }}</p>
          </div>
          
          <!-- Favorite Button -->
          <div class="ml-4">
            <FavoriteButton :recipe-id="recipe.id" :show-count="true" />
          </div>
        </div>
        
        <!-- Recipe Meta Info -->
        <div class="flex flex-wrap gap-4 text-sm text-gray-600 mb-4">
          <div v-if="recipe.cookingTime" class="flex items-center gap-1">
            <span>🕒</span>
            <span>{{ recipe.cookingTime }} minutos</span>
          </div>
          <div v-if="recipe.servings" class="flex items-center gap-1">
            <span>👥</span>
            <span>{{ recipe.servings }} porciones</span>
          </div>
          <div v-if="recipe.diet" class="flex items-center gap-1">
            <span>🥗</span>
            <span>{{ recipe.diet }}</span>
          </div>
        </div>
        
        <!-- Tags -->
        <div v-if="recipe.tags && recipe.tags.length" class="flex flex-wrap gap-2 mb-4">
          <span
            v-for="tag in recipe.tags"
            :key="tag"
            class="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full"
          >
            {{ tag }}
          </span>
        </div>
      </div>

      <!-- Main Content -->
      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <!-- Left Column: Recipe Info -->
        <div class="lg:col-span-2 space-y-6">
          <!-- Ingredients -->
          <div class="ingredients-section">
            <h2 class="text-xl font-semibold mb-3">Ingredientes</h2>
            <div v-if="recipe.ingredients && recipe.ingredients.length" class="space-y-2">
              <div
                v-for="ingredient in recipe.ingredients"
                :key="ingredient.id"
                class="flex items-center gap-3 p-2 bg-gray-50 rounded-lg"
              >
                <span class="w-4 h-4 bg-blue-500 rounded-full flex-shrink-0"></span>
                <span class="flex-1">{{ ingredient.name }}</span>
                <span v-if="ingredient.quantity" class="text-gray-600 font-medium">
                  {{ ingredient.quantity }}
                </span>
              </div>
            </div>
            <EmptyState 
              v-else 
              message="No hay ingredientes especificados" 
              icon="🥘"
            />
          </div>

          <!-- Instructions -->
          <div class="instructions-section">
            <h2 class="text-xl font-semibold mb-3">Instrucciones</h2>
            <div v-if="recipe.steps && recipe.steps.length" class="space-y-3">
              <div
                v-for="(step, index) in recipe.steps"
                :key="index"
                class="flex gap-3"
              >
                <div class="flex-shrink-0 w-8 h-8 bg-blue-600 text-white rounded-full flex items-center justify-center text-sm font-bold">
                  {{ index + 1 }}
                </div>
                <p class="flex-1 text-gray-700 leading-relaxed">{{ step }}</p>
              </div>
            </div>
            <EmptyState 
              v-else 
              message="No hay instrucciones disponibles" 
              icon="📝"
            />
          </div>
        </div>

        <!-- Right Column: Rating System -->
        <div class="lg:col-span-1">
          <RatingSystem :recipe-id="recipe.id" />
        </div>
      </div>

      <!-- Back Button -->
      <div class="mt-8">
        <button
          @click="goBack"
          class="inline-flex items-center gap-2 px-4 py-2 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition-colors"
        >
          <svg class="w-4 h-4" viewBox="0 0 24 24" fill="currentColor">
            <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.42-1.41L7.83 13H20v-2z"/>
          </svg>
          Volver
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { http } from '../services';
import LoadingState from '../components/LoadingState.vue';
import ErrorState from '../components/ErrorState.vue';
import EmptyState from '../components/EmptyState.vue';
import RatingSystem from '../components/RatingSystem.vue';
import FavoriteButton from '../components/FavoriteButton.vue';

// Router
const route = useRoute();
const router = useRouter();

// Reactive state
const recipe = ref<any>(null);
const isLoading = ref(true);
const error = ref<string | null>(null);

// Get recipe ID from route
const recipeId = route.params.id as string;

// Methods
const loadRecipe = async () => {
  if (!recipeId) {
    error.value = 'ID de receta no válido';
    isLoading.value = false;
    return;
  }

  isLoading.value = true;
  error.value = null;

  try {
    const response = await http.get(`/recipes/${recipeId}`);
    recipe.value = response.data;
  } catch (err: any) {
    console.error('Error loading recipe:', err);
    error.value = err.response?.data?.message || 'Error al cargar la receta';
  } finally {
    isLoading.value = false;
  }
};

const goBack = () => {
  router.back();
};

// Load recipe on mount
onMounted(() => {
  loadRecipe();
});
</script>