<template>
  <div class="flex items-center justify-center p-8 min-h-[300px]">
    <div class="max-w-md w-full text-center">
      <!-- Error Icon -->
      <div class="mx-auto w-16 h-16 mb-4">
        <svg class="w-full h-full text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L4.232 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
        </svg>
      </div>
      
      <!-- Error Content -->
      <div class="space-y-4">
        <h3 class="text-xl font-semibold text-gray-900">{{ title }}</h3>
        <p class="text-gray-600 leading-relaxed">{{ message }}</p>
        
        <!-- Error Details (collapsible) -->
        <div v-if="details" class="mt-4">
          <button 
            @click="showDetails = !showDetails"
            class="text-sm text-gray-500 hover:text-gray-700 transition-colors underline"
          >
            {{ showDetails ? 'Ocultar detalles' : 'Ver detalles técnicos' }}
          </button>
          
          <div v-if="showDetails" class="mt-2 p-3 bg-gray-50 rounded-lg text-left">
            <pre class="text-xs text-gray-700 font-mono whitespace-pre-wrap">{{ details }}</pre>
          </div>
        </div>
        
        <!-- Action Buttons -->
        <div class="flex flex-col sm:flex-row gap-3 justify-center mt-6">
          <button 
            @click="$emit('retry')"
            class="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors font-medium"
          >
            {{ retryText }}
          </button>
          
          <button 
            v-if="showGoHome"
            @click="$emit('goHome')"
            class="px-6 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors font-medium"
          >
            Ir al inicio
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

interface Props {
  title?: string;
  message?: string;
  details?: string;
  retryText?: string;
  showGoHome?: boolean;
}

withDefaults(defineProps<Props>(), {
  title: 'Error inesperado',
  message: 'Ha ocurrido un error. Por favor, inténtalo de nuevo.',
  details: '',
  retryText: 'Reintentar',
  showGoHome: true
});

defineEmits<{
  retry: [];
  goHome: [];
}>();

const showDetails = ref(false);
</script>
