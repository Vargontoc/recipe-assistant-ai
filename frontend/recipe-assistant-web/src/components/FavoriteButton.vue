<template>
  <div class="favorites-button">
    <button
      @click="toggleFavorite"
      :disabled="isToggling"
      :class="[
        'inline-flex items-center gap-2 px-4 py-2 rounded-lg border transition-all duration-200',
        isFavorited 
          ? 'bg-red-50 border-red-200 text-red-700 hover:bg-red-100' 
          : 'bg-gray-50 border-gray-200 text-gray-700 hover:bg-gray-100',
        isToggling ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'
      ]"
    >
      <!-- Heart Icon -->
      <svg 
        :class="[
          'w-5 h-5 transition-all duration-200',
          isFavorited ? 'text-red-500 fill-current' : 'text-gray-400'
        ]"
        viewBox="0 0 24 24"
        fill="currentColor"
      >
        <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
      </svg>
      
      <span class="text-sm font-medium">
        {{ isToggling ? 'Guardando...' : (isFavorited ? 'Favorito' : 'Agregar a favoritos') }}
      </span>
      
      <!-- Favorite count if showing -->
      <span v-if="showCount && favoriteCount > 0" class="text-xs bg-gray-200 px-2 py-1 rounded-full">
        {{ favoriteCount }}
      </span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { favoritesService } from '../services';

// Props
interface Props {
  recipeId: string;
  showCount?: boolean;
}
const props = withDefaults(defineProps<Props>(), {
  showCount: false
});

// Reactive state
const isFavorited = ref(false);
const favoriteCount = ref(0);
const isToggling = ref(false);

// Methods
const toggleFavorite = async () => {
  if (isToggling.value) return;
  
  isToggling.value = true;
  try {
    if (isFavorited.value) {
      // Remove from favorites
      await favoritesService.removeFavorite(props.recipeId);
      isFavorited.value = false;
    } else {
      // Add to favorites
      await favoritesService.addFavorite({ recipeId: props.recipeId });
      isFavorited.value = true;
    }
    
    // Update count if shown
    if (props.showCount) {
      await loadFavoriteCount();
    }
  } catch (error) {
    console.error('Error toggling favorite:', error);
    alert('Error al actualizar favoritos. Inténtalo de nuevo.');
  } finally {
    isToggling.value = false;
  }
};

const loadFavoriteStatus = async () => {
  try {
    isFavorited.value = await favoritesService.isFavorited(props.recipeId);
  } catch (error) {
    console.error('Error loading favorite status:', error);
  }
};

const loadFavoriteCount = async () => {
  if (!props.showCount) return;
  
  try {
    favoriteCount.value = await favoritesService.getFavoriteCount(props.recipeId);
  } catch (error) {
    console.error('Error loading favorite count:', error);
  }
};

// Load data on mount
onMounted(async () => {
  await Promise.all([
    loadFavoriteStatus(),
    loadFavoriteCount()
  ]);
});
</script>
