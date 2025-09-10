<template>
  <div class="flex items-center justify-center p-8" :class="containerClass">
    <div class="flex flex-col items-center space-y-4">
      <!-- Spinner -->
      <div class="relative">
        <div class="w-12 h-12 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin"></div>
        <div class="absolute inset-0 flex items-center justify-center">
          <svg class="w-6 h-6 text-blue-600 opacity-75" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                  d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4"></path>
          </svg>
        </div>
      </div>
      
      <!-- Loading text -->
      <div class="text-center">
        <p class="text-gray-700 font-medium">{{ message }}</p>
        <p v-if="subtitle" class="text-sm text-gray-500 mt-1">{{ subtitle }}</p>
      </div>
      
      <!-- Progress bar (optional) -->
      <div v-if="showProgress" class="w-64 bg-gray-200 rounded-full h-2">
        <div 
          class="bg-blue-600 h-2 rounded-full transition-all duration-300 ease-out"
          :style="{ width: `${progress}%` }"
        ></div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue'

interface Props {
  message?: string;
  subtitle?: string;
  showProgress?: boolean;
  progress?: number;
  size?: 'sm' | 'md' | 'lg';
}

const props = withDefaults(defineProps<Props>(), {
  message: 'Cargando...',
  subtitle: '',
  showProgress: false,
  progress: 0,
  size: 'md'
});

const containerClass = computed(() => {
  const sizeClasses = {
    sm: 'min-h-[200px]',
    md: 'min-h-[300px]',
    lg: 'min-h-[400px]'
  };
  return sizeClasses[props.size];
});
</script>
