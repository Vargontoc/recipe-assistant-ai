<template>
  <div class="flex items-center justify-center p-8 min-h-[300px]">
    <div class="max-w-md w-full text-center">
      <!-- Empty State Icon -->
      <div class="mx-auto w-20 h-20 mb-6">
        <svg class="w-full h-full text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" 
                d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
        </svg>
      </div>
      
      <!-- Content -->
      <div class="space-y-4">
        <h3 class="text-xl font-semibold text-gray-900">{{ displayTitle }}</h3>
        <p class="text-gray-600 leading-relaxed">{{ displayMessage }}</p>
        
        <!-- Action Button -->
        <div v-if="displayActionText" class="mt-6">
          <button 
            @click="$emit('action')"
            class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium shadow-sm"
          >
            {{ displayActionText }}
          </button>
        </div>
        
        <!-- Secondary actions -->
        <div v-if="displaySuggestions.length" class="mt-6">
          <p class="text-sm text-gray-500 mb-3">{{ suggestionsTitle }}</p>
          <div class="flex flex-wrap gap-2 justify-center">
            <button 
              v-for="suggestion in displaySuggestions"
              :key="suggestion"
              @click="$emit('suggestion', suggestion)"
              class="px-3 py-1 text-sm bg-gray-100 text-gray-700 rounded-full hover:bg-gray-200 transition-colors"
            >
              {{ suggestion }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'

interface Props {
  type?: 'recipes' | 'search' | 'favorites' | 'general';
  title?: string;
  message?: string;
  actionText?: string;
  suggestions?: string[];
  suggestionsTitle?: string;
}

const props = withDefaults(defineProps<Props>(), {
  type: 'general',
  title: '',
  message: '',
  actionText: '',
  suggestions: () => [],
  suggestionsTitle: 'Prueba con:'
});

defineEmits<{
  action: [];
  suggestion: [value: string];
}>();

// Default content based on type
const defaultContent = computed(() => {
  const defaults = {
    recipes: {
      title: 'No hay recetas',
      message: 'Aún no tienes recetas guardadas. ¡Crea tu primera receta!',
      actionText: 'Crear receta',
      suggestions: [] as string[]
    },
    search: {
      title: 'Sin resultados',
      message: 'No encontramos recetas que coincidan con tu búsqueda.',
      actionText: 'Limpiar búsqueda',
      suggestions: ['Pasta', 'Pollo', 'Vegetariano', 'Postre']
    },
    favorites: {
      title: 'Sin favoritos',
      message: 'No has guardado ninguna receta como favorita aún.',
      actionText: 'Explorar recetas',
      suggestions: [] as string[]
    },
    general: {
      title: 'Sin contenido',
      message: 'No hay contenido disponible en este momento.',
      actionText: '',
      suggestions: [] as string[]
    }
  };
  
  return defaults[props.type];
});

// Computed properties for display
const displayTitle = computed(() => props.title || defaultContent.value.title);
const displayMessage = computed(() => props.message || defaultContent.value.message);
const displayActionText = computed(() => props.actionText || defaultContent.value.actionText);
const displaySuggestions = computed(() => 
  props.suggestions.length > 0 ? props.suggestions : defaultContent.value.suggestions
);
</script>
