<template>
  <div class="group bg-white rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 overflow-hidden border border-gray-100">
    <!-- Header with AI Badge -->
    <div class="relative p-6 bg-gradient-to-br from-green-50 to-emerald-50">
      <div class="flex items-start justify-between mb-4">
        <div class="flex items-center gap-2 text-xs font-semibold text-green-700 bg-green-100 px-3 py-1 rounded-full">
          <span class="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
          Generado por IA
        </div>
        <div class="flex gap-2">
          <button
            @click="toggleFavorite"
            class="p-2 rounded-full hover:bg-white/50 transition-colors"
            :class="{ 'text-red-500': isFavorited, 'text-gray-400': !isFavorited }"
          >
            <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
              <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
            </svg>
          </button>
          <button
            @click="shareRecipe"
            class="p-2 rounded-full hover:bg-white/50 transition-colors text-gray-400 hover:text-gray-600"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8.684 13.342C8.886 12.938 9 12.482 9 12c0-.482-.114-.938-.316-1.342m0 2.684a3 3 0 110-2.684m0 2.684l6.632 3.316m-6.632-6l6.632-3.316m0 0a3 3 0 105.367-2.684 3 3 0 00-5.367 2.684zm0 9.316a3 3 0 105.367 2.684 3 3 0 00-5.367-2.684z" />
            </svg>
          </button>
        </div>
      </div>
      
      <h3 class="text-xl font-bold text-gray-900 mb-2 group-hover:text-green-700 transition-colors">
        {{ recipe.title }}
      </h3>
      
      <p class="text-gray-600 text-sm leading-relaxed">
        {{ recipe.summary }}
      </p>
    </div>

    <!-- Tags -->
    <div v-if="recipe.tags && recipe.tags.length > 0" class="px-6 py-3 bg-gray-50">
      <div class="flex flex-wrap gap-2">
        <span
          v-for="tag in recipe.tags.slice(0, 3)"
          :key="tag"
          class="text-xs px-2 py-1 bg-blue-100 text-blue-700 rounded-full"
        >
          #{{ tag }}
        </span>
        <span v-if="recipe.tags.length > 3" class="text-xs text-gray-500">
          +{{ recipe.tags.length - 3 }} más
        </span>
      </div>
    </div>

    <!-- Preview Steps -->
    <div class="px-6 py-4">
      <div class="flex items-center justify-between mb-3">
        <h4 class="font-semibold text-gray-900 text-sm">Pasos de preparación</h4>
        <span class="text-xs text-gray-500">{{ recipe.steps?.length || 0 }} pasos</span>
      </div>
      
      <div class="space-y-2">
        <div
          v-for="(step, index) in (recipe.steps || []).slice(0, 2)"
          :key="index"
          class="flex gap-3 text-sm"
        >
          <div class="flex-shrink-0 w-5 h-5 bg-green-500 text-white rounded-full flex items-center justify-center text-xs font-bold">
            {{ index + 1 }}
          </div>
          <p class="text-gray-600 line-clamp-2">{{ step }}</p>
        </div>
        
        <div v-if="(recipe.steps?.length || 0) > 2" class="text-center">
          <button
            @click="showDetails"
            class="text-xs text-green-600 hover:text-green-700 font-medium"
          >
            Ver {{ recipe.steps.length - 2 }} pasos más...
          </button>
        </div>
      </div>
    </div>

    <!-- Actions Footer -->
    <div class="px-6 py-4 bg-gray-50 flex items-center justify-between">
      <div class="flex items-center gap-2 text-xs text-gray-500">
        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <span>Generado {{ formatTimeAgo(recipe.createdAt) }}</span>
      </div>
      
      <div class="flex gap-2">
        <button
          @click="saveRecipe"
          class="px-3 py-1 text-xs bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
        >
          💾 Guardar
        </button>
        <button
          @click="showDetails"
          class="px-3 py-1 text-xs border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors"
        >
          👁️ Ver completa
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface AIRecipe {
  id?: string
  title: string
  summary: string
  steps: string[]
  tags: string[]
  createdAt?: string
}

interface Props {
  recipe: AIRecipe
  favorited?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  favorited: false
})

const emit = defineEmits<{
  favoriteToggled: [isFavorited: boolean]
  save: [recipe: AIRecipe]
  showDetails: [recipe: AIRecipe]
  share: [recipe: AIRecipe]
}>()

const isFavorited = ref(props.favorited)

const toggleFavorite = () => {
  isFavorited.value = !isFavorited.value
  emit('favoriteToggled', isFavorited.value)
}

const saveRecipe = () => {
  emit('save', props.recipe)
}

const showDetails = () => {
  emit('showDetails', props.recipe)
}

const shareRecipe = () => {
  emit('share', props.recipe)
}

const formatTimeAgo = (dateString?: string) => {
  if (!dateString) return 'hace poco'
  
  const date = new Date(dateString)
  const now = new Date()
  const diffInMinutes = Math.floor((now.getTime() - date.getTime()) / (1000 * 60))
  
  if (diffInMinutes < 1) return 'hace poco'
  if (diffInMinutes < 60) return `hace ${diffInMinutes} min`
  
  const diffInHours = Math.floor(diffInMinutes / 60)
  if (diffInHours < 24) return `hace ${diffInHours}h`
  
  const diffInDays = Math.floor(diffInHours / 24)
  return `hace ${diffInDays}d`
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
