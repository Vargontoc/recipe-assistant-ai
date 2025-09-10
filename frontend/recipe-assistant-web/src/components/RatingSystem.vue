<template>
  <div class="rating-system">
    <!-- Rating Display -->
    <div v-if="!isEditing" class="rating-display">
      <div class="flex items-center gap-2 mb-2">
        <div class="flex items-center">
          <svg 
            v-for="star in 5" 
            :key="star"
            :class="[
              'w-5 h-5 cursor-pointer transition-colors',
              star <= (userRating?.rating || 0) ? 'text-yellow-500 fill-current' : 'text-gray-300'
            ]"
            @click="startRating(star)"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
          </svg>
        </div>
        <span v-if="userRating" class="text-sm text-gray-600">
          Tu calificación: {{ userRating.rating }}/5
        </span>
        <span v-else class="text-sm text-gray-500">
          Califica esta receta
        </span>
      </div>
      
      <!-- User's comment if exists -->
      <div v-if="userRating?.comment" class="mb-3 p-2 bg-blue-50 rounded-lg">
        <p class="text-sm text-gray-700">{{ userRating.comment }}</p>
        <button 
          @click="startEditing"
          class="text-xs text-blue-600 hover:text-blue-800 mt-1"
        >
          Editar reseña
        </button>
      </div>

      <!-- Rating Statistics -->
      <div v-if="stats" class="rating-stats mb-4">
        <div class="flex items-center gap-4 mb-2">
          <div class="flex items-center gap-1">
            <svg class="w-4 h-4 text-yellow-500 fill-current" viewBox="0 0 24 24">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
            </svg>
            <span class="font-medium">{{ stats.averageRating.toFixed(1) }}</span>
          </div>
          <span class="text-sm text-gray-600">
            ({{ stats.totalRatings }} reseña{{ stats.totalRatings !== 1 ? 's' : '' }})
          </span>
        </div>
        
        <!-- Rating Distribution -->
        <div class="space-y-1">
          <div 
            v-for="(count, rating) in stats.ratingDistribution" 
            :key="rating"
            class="flex items-center gap-2 text-xs"
          >
            <span class="w-8">{{ rating }}★</span>
            <div class="flex-1 bg-gray-200 rounded-full h-2">
              <div 
                class="bg-yellow-500 h-2 rounded-full transition-all"
                :style="{ width: `${(count / stats.totalRatings) * 100}%` }"
              ></div>
            </div>
            <span class="w-8 text-gray-600">{{ count }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Rating Form -->
    <div v-if="isEditing" class="rating-form">
      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          Tu calificación
        </label>
        <div class="flex items-center gap-1">
          <svg 
            v-for="star in 5" 
            :key="star"
            :class="[
              'w-6 h-6 cursor-pointer transition-colors',
              star <= currentRating ? 'text-yellow-500 fill-current' : 'text-gray-300'
            ]"
            @click="currentRating = star"
            @mouseover="hoverRating = star"
            @mouseleave="hoverRating = 0"
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
          </svg>
        </div>
      </div>

      <div class="mb-4">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          Comentario (opcional)
        </label>
        <textarea
          v-model="currentComment"
          rows="3"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          placeholder="Comparte tu experiencia con esta receta..."
        ></textarea>
      </div>

      <div class="flex gap-2">
        <button
          @click="submitRating"
          :disabled="currentRating === 0 || isSubmitting"
          class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {{ isSubmitting ? 'Guardando...' : (userRating ? 'Actualizar' : 'Enviar') }}
        </button>
        <button
          @click="cancelEdit"
          class="px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400"
        >
          Cancelar
        </button>
        <button
          v-if="userRating"
          @click="deleteRating"
          :disabled="isSubmitting"
          class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 disabled:opacity-50"
        >
          Eliminar
        </button>
      </div>
    </div>

    <!-- All Ratings -->
    <div v-if="showAllRatings && allRatings?.content?.length" class="all-ratings mt-6">
      <h3 class="text-lg font-semibold mb-4">Todas las reseñas</h3>
      <div class="space-y-4">
        <div 
          v-for="rating in allRatings.content" 
          :key="rating.id"
          class="border-b border-gray-200 pb-4"
        >
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <div class="flex items-center gap-2 mb-1">
                <div class="flex items-center">
                  <svg 
                    v-for="star in 5" 
                    :key="star"
                    :class="[
                      'w-4 h-4',
                      star <= rating.rating ? 'text-yellow-500 fill-current' : 'text-gray-300'
                    ]"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                  >
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                  </svg>
                </div>
                <span class="text-sm text-gray-600">
                  {{ rating.isAnonymous ? 'Usuario anónimo' : rating.userIdentifier }}
                </span>
              </div>
              <p v-if="rating.comment" class="text-gray-700">{{ rating.comment }}</p>
            </div>
            <span class="text-xs text-gray-500">
              {{ formatDate(rating.createdAt) }}
            </span>
          </div>
        </div>
      </div>
      
      <!-- Load More -->
      <button
        v-if="allRatings.hasNext"
        @click="loadMoreRatings"
        class="mt-4 text-blue-600 hover:text-blue-800 text-sm"
      >
        Ver más reseñas
      </button>
    </div>

    <button
      v-if="!showAllRatings && stats && stats.totalRatings > 0"
      @click="loadAllRatings"
      class="mt-4 text-blue-600 hover:text-blue-800 text-sm"
    >
      Ver todas las reseñas ({{ stats.totalRatings }})
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
// Using simple SVG instead of heroicons
import { ratingService, type RecipeRating, type RatingStats } from '../services';

// Props
interface Props {
  recipeId: string;
}
const props = defineProps<Props>();

// Reactive state
const userRating = ref<RecipeRating | null>(null);
const stats = ref<RatingStats | null>(null);
const allRatings = ref<any>(null);
const isEditing = ref(false);
const isSubmitting = ref(false);
const showAllRatings = ref(false);
const currentRating = ref(0);
const currentComment = ref('');
const hoverRating = ref(0);

// Methods
const startRating = (rating: number) => {
  currentRating.value = rating;
  currentComment.value = userRating.value?.comment || '';
  isEditing.value = true;
};

const startEditing = () => {
  if (userRating.value) {
    currentRating.value = userRating.value.rating;
    currentComment.value = userRating.value.comment || '';
    isEditing.value = true;
  }
};

const cancelEdit = () => {
  isEditing.value = false;
  currentRating.value = 0;
  currentComment.value = '';
};

const submitRating = async () => {
  if (currentRating.value === 0) return;
  
  isSubmitting.value = true;
  try {
    const request = {
      recipeId: props.recipeId,
      rating: currentRating.value,
      comment: currentComment.value.trim() || undefined
    };

    if (userRating.value) {
      // Update existing rating
      userRating.value = await ratingService.updateRating(userRating.value.id, request);
    } else {
      // Create new rating
      userRating.value = await ratingService.createRating(request);
    }

    // Refresh stats
    await loadStats();
    isEditing.value = false;
  } catch (error) {
    console.error('Error submitting rating:', error);
    alert('Error al enviar la calificación. Inténtalo de nuevo.');
  } finally {
    isSubmitting.value = false;
  }
};

const deleteRating = async () => {
  if (!userRating.value || !confirm('¿Estás seguro de que quieres eliminar tu calificación?')) {
    return;
  }

  isSubmitting.value = true;
  try {
    await ratingService.deleteRating(props.recipeId, userRating.value.id);
    userRating.value = null;
    await loadStats();
    isEditing.value = false;
  } catch (error) {
    console.error('Error deleting rating:', error);
    alert('Error al eliminar la calificación. Inténtalo de nuevo.');
  } finally {
    isSubmitting.value = false;
  }
};

const loadUserRating = async () => {
  try {
    userRating.value = await ratingService.getUserRating(props.recipeId);
  } catch (error) {
    console.error('Error loading user rating:', error);
  }
};

const loadStats = async () => {
  try {
    stats.value = await ratingService.getRatingStats(props.recipeId);
  } catch (error) {
    console.error('Error loading rating stats:', error);
  }
};

const loadAllRatings = async () => {
  try {
    allRatings.value = await ratingService.getRecipeRatings(props.recipeId, 0, 10);
    showAllRatings.value = true;
  } catch (error) {
    console.error('Error loading all ratings:', error);
  }
};

const loadMoreRatings = async () => {
  if (!allRatings.value || !allRatings.value.hasNext) return;
  
  try {
    const nextPage = await ratingService.getRecipeRatings(
      props.recipeId, 
      allRatings.value.number + 1, 
      10
    );
    allRatings.value.content.push(...nextPage.content);
    allRatings.value.hasNext = nextPage.hasNext;
    allRatings.value.number = nextPage.number;
  } catch (error) {
    console.error('Error loading more ratings:', error);
  }
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

// Load data on mount
onMounted(async () => {
  await Promise.all([
    loadUserRating(),
    loadStats()
  ]);
});
</script>

<style scoped>
.rating-system {
  background-color: white;
  border-radius: 0.5rem;
  border: 1px solid #e5e7eb;
  padding: 1rem;
}
</style>
