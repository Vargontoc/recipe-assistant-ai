<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-6xl mx-auto">
      <!-- Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">📚 Mi Historial</h1>
        <p class="text-gray-600">
          Revisa tus búsquedas anteriores y las recetas generadas por IA
        </p>
      </div>

      <!-- Tabs -->
      <div class="mb-8">
        <div class="border-b border-gray-200">
          <nav class="flex space-x-8">
            <button
              @click="activeTab = 'suggestions'"
              :class="[
                'py-2 px-1 border-b-2 font-medium text-sm',
                activeTab === 'suggestions'
                  ? 'border-green-500 text-green-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              ]"
            >
              <span class="flex items-center gap-2">
                🤖 Sugerencias IA
                <span v-if="suggestionStats.total" class="bg-green-100 text-green-800 text-xs px-2 py-0.5 rounded-full">
                  {{ suggestionStats.total }}
                </span>
              </span>
            </button>
            <button
              @click="activeTab = 'searches'"
              :class="[
                'py-2 px-1 border-b-2 font-medium text-sm',
                activeTab === 'searches'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              ]"
            >
              <span class="flex items-center gap-2">
                🔍 Búsquedas
                <span v-if="searchStats.totalSearches" class="bg-blue-100 text-blue-800 text-xs px-2 py-0.5 rounded-full">
                  {{ searchStats.totalSearches }}
                </span>
              </span>
            </button>
          </nav>
        </div>
      </div>

      <!-- AI Suggestions Tab -->
      <div v-if="activeTab === 'suggestions'" class="space-y-6">
        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-600">Recetas Generadas</p>
                <p class="text-2xl font-bold text-green-600">{{ suggestionStats.total }}</p>
              </div>
              <div class="p-3 bg-green-100 rounded-full">
                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                </svg>
              </div>
            </div>
          </div>
          
          <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-600">Esta Semana</p>
                <p class="text-2xl font-bold text-blue-600">{{ suggestionStats.thisWeek }}</p>
              </div>
              <div class="p-3 bg-blue-100 rounded-full">
                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
            </div>
          </div>
          
          <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-600">Recetas Guardadas</p>
                <p class="text-2xl font-bold text-purple-600">{{ suggestionStats.saved }}</p>
              </div>
              <div class="p-3 bg-purple-100 rounded-full">
                <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 5a2 2 0 012-2h10a2 2 0 012 2v16l-7-3.5L5 21V5z" />
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- AI Suggestions List -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-semibold text-gray-900">Recetas Generadas por IA</h3>
          </div>
          
          <div v-if="loadingSuggestions" class="p-8 text-center">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-green-600 mx-auto"></div>
            <p class="text-gray-500 mt-2">Cargando sugerencias...</p>
          </div>
          
          <div v-else-if="suggestions.length === 0" class="p-8 text-center">
            <div class="text-6xl mb-4">🤖</div>
            <h3 class="text-lg font-semibold text-gray-900 mb-2">
              Aún no has usado el asistente IA
            </h3>
            <p class="text-gray-600 mb-6">
              Empieza a generar recetas personalizadas con nuestro asistente culinario
            </p>
            <router-link
              to="/suggest"
              class="inline-flex items-center gap-2 px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
            >
              ✨ Crear Mi Primera Receta
            </router-link>
          </div>
          
          <div v-else class="divide-y divide-gray-200">
            <div
              v-for="suggestion in suggestions"
              :key="suggestion.id"
              class="p-6 hover:bg-gray-50 transition-colors"
            >
              <div class="flex items-start justify-between">
                <div class="flex-1">
                  <div class="flex items-center gap-2 mb-2">
                    <h4 class="text-lg font-semibold text-gray-900">
                      {{ suggestion.generatedRecipe.title }}
                    </h4>
                    <span class="px-2 py-1 bg-green-100 text-green-700 text-xs rounded-full">
                      IA
                    </span>
                  </div>
                  
                  <p class="text-gray-600 mb-3">{{ suggestion.generatedRecipe.summary }}</p>
                  
                  <div class="flex items-center gap-4 text-sm text-gray-500">
                    <span class="flex items-center gap-1">
                      🥕 {{ suggestion.requestIngredients.length }} ingredientes
                    </span>
                    <span class="flex items-center gap-1">
                      📝 {{ suggestion.generatedRecipe.steps.length }} pasos
                    </span>
                    <span class="flex items-center gap-1">
                      🏷️ {{ suggestion.generatedRecipe.tags.length }} etiquetas
                    </span>
                  </div>
                  
                  <div class="mt-3 flex flex-wrap gap-1">
                    <span
                      v-for="ingredient in suggestion.requestIngredients.slice(0, 4)"
                      :key="ingredient"
                      class="text-xs px-2 py-1 bg-gray-100 text-gray-600 rounded"
                    >
                      {{ ingredient }}
                    </span>
                    <span 
                      v-if="suggestion.requestIngredients.length > 4"
                      class="text-xs text-gray-500"
                    >
                      +{{ suggestion.requestIngredients.length - 4 }} más
                    </span>
                  </div>
                </div>
                
                <div class="ml-6 flex flex-col items-end gap-2">
                  <span class="text-sm text-gray-500">
                    {{ formatDate(suggestion.createdAt) }}
                  </span>
                  <div class="flex gap-2">
                    <button
                      @click="viewSuggestion(suggestion)"
                      class="px-3 py-1 text-sm bg-green-600 text-white rounded hover:bg-green-700 transition-colors"
                    >
                      Ver Receta
                    </button>
                    <button
                      @click="recreateRecipe(suggestion)"
                      class="px-3 py-1 text-sm border border-gray-300 text-gray-700 rounded hover:bg-gray-50 transition-colors"
                    >
                      Recrear
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Search History Tab -->
      <div v-if="activeTab === 'searches'" class="space-y-6">
        <!-- Search Stats -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-600">Total de Búsquedas</p>
                <p class="text-2xl font-bold text-blue-600">{{ searchStats.totalSearches }}</p>
              </div>
              <div class="p-3 bg-blue-100 rounded-full">
                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
              </div>
            </div>
          </div>
          
          <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
            <div class="flex items-center justify-between">
              <div>
                <p class="text-sm font-medium text-gray-600">Promedio de Resultados</p>
                <p class="text-2xl font-bold text-indigo-600">{{ Math.round(searchStats.averageResults) }}</p>
              </div>
              <div class="p-3 bg-indigo-100 rounded-full">
                <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2-2V7a2 2 0 012-2h2a2 2 0 002 2v2a2 2 0 002 2h2a2 2 0 002-2V7a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 00-2 2h-2a2 2 0 00-2 2v6a2 2 0 01-2 2H9a2 2 0 01-2-2z" />
                </svg>
              </div>
            </div>
          </div>
        </div>

        <!-- Search History List -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200">
          <div class="px-6 py-4 border-b border-gray-200">
            <h3 class="text-lg font-semibold text-gray-900">Historial de Búsquedas</h3>
          </div>
          
          <div v-if="loadingSearches" class="p-8 text-center">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p class="text-gray-500 mt-2">Cargando historial...</p>
          </div>
          
          <div v-else-if="searchHistory.length === 0" class="p-8 text-center">
            <div class="text-6xl mb-4">🔍</div>
            <h3 class="text-lg font-semibold text-gray-900 mb-2">
              Aún no has realizado búsquedas
            </h3>
            <p class="text-gray-600 mb-6">
              Empieza a explorar nuestra colección de recetas
            </p>
            <router-link
              to="/search"
              class="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              🔍 Buscar Recetas
            </router-link>
          </div>
          
          <div v-else class="divide-y divide-gray-200">
            <div
              v-for="search in searchHistory"
              :key="search.id"
              class="p-6 hover:bg-gray-50 transition-colors cursor-pointer"
              @click="repeatSearch(search)"
            >
              <div class="flex items-center justify-between">
                <div class="flex-1">
                  <div class="flex items-center gap-2 mb-1">
                    <h4 class="font-medium text-gray-900">
                      "{{ search.searchQuery }}"
                    </h4>
                    <span class="px-2 py-1 bg-blue-100 text-blue-700 text-xs rounded-full">
                      {{ search.searchType }}
                    </span>
                  </div>
                  <p class="text-sm text-gray-600">
                    {{ search.resultsCount }} resultados encontrados
                  </p>
                </div>
                
                <div class="flex items-center gap-3">
                  <span class="text-sm text-gray-500">
                    {{ formatDate(search.createdAt) }}
                  </span>
                  <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                  </svg>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { suggestService, searchHistoryService, type RecipeSuggestion, type SearchHistoryItem } from '../services'

const router = useRouter()

// Reactive data
const activeTab = ref<'suggestions' | 'searches'>('suggestions')
const suggestions = ref<RecipeSuggestion[]>([])
const searchHistory = ref<SearchHistoryItem[]>([])
const loadingSuggestions = ref(true)
const loadingSearches = ref(true)

const suggestionStats = ref({
  total: 0,
  thisWeek: 0,
  saved: 0
})

const searchStats = ref({
  totalSearches: 0,
  averageResults: 0,
  lastSearchAt: ''
})

// Methods
const loadSuggestionHistory = async () => {
  try {
    loadingSuggestions.value = true
    const response = await suggestService.getSuggestionHistory(0, 20)
    suggestions.value = response.content || []
    
    // Calculate stats
    suggestionStats.value.total = suggestions.value.length
    
    const oneWeekAgo = new Date()
    oneWeekAgo.setDate(oneWeekAgo.getDate() - 7)
    
    suggestionStats.value.thisWeek = suggestions.value.filter(s => 
      new Date(s.createdAt) > oneWeekAgo
    ).length
    
    // Note: saved count would need backend support
    suggestionStats.value.saved = 0
    
  } catch (error) {
    console.error('Error loading suggestion history:', error)
  } finally {
    loadingSuggestions.value = false
  }
}

const loadSearchHistory = async () => {
  try {
    loadingSearches.value = true
    const [historyResponse, statsResponse] = await Promise.all([
      searchHistoryService.getUserSearchHistory(0, 20),
      searchHistoryService.getUserSearchStats()
    ])
    
    searchHistory.value = historyResponse.content || []
    searchStats.value = {
      totalSearches: statsResponse.totalSearches,
      averageResults: statsResponse.averageResults,
      lastSearchAt: statsResponse.lastSearchAt || ''
    }
    
  } catch (error) {
    console.error('Error loading search history:', error)
  } finally {
    loadingSearches.value = false
  }
}

const viewSuggestion = (suggestion: RecipeSuggestion) => {
  // Navigate to a detailed view or show modal
  console.log('View suggestion:', suggestion)
  // Could implement a modal or navigation to detailed view
}

const recreateRecipe = (suggestion: RecipeSuggestion) => {
  // Navigate to suggest page with pre-filled ingredients
  router.push({
    path: '/suggest',
    query: {
      ingredients: suggestion.requestIngredients.join(',')
    }
  })
}

const repeatSearch = (search: SearchHistoryItem) => {
  // Navigate to search page with the previous query
  router.push({
    path: '/search',
    query: {
      q: search.searchQuery
    }
  })
}

const formatDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// Lifecycle
onMounted(() => {
  loadSuggestionHistory()
  loadSearchHistory()
})
</script>