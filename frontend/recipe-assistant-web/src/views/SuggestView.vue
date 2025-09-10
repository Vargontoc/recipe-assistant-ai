<template>
  <div class="min-h-screen bg-gradient-to-br from-green-50 to-blue-50 p-6">
    <div class="max-w-6xl mx-auto">
      <!-- Header -->
      <div class="text-center mb-8">
        <h1 class="text-4xl font-bold text-gray-800 mb-4">
          🤖 Asistente Culinario IA
        </h1>
        <p class="text-lg text-gray-600 max-w-2xl mx-auto">
          Ingresa tus ingredientes disponibles y déjanos crear una receta personalizada para ti
        </p>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <!-- Main Form Panel -->
        <div class="lg:col-span-2">
          <div class="bg-white rounded-2xl shadow-lg p-8">
            <!-- Ingredients Input -->
            <div class="mb-8">
              <h2 class="text-2xl font-semibold text-gray-800 mb-4 flex items-center gap-2">
                🥕 Ingredientes Disponibles
              </h2>
              <div class="space-y-4">
                <div class="flex gap-3">
                  <input
                    v-model="newIngredient"
                    @keyup.enter="addIngredient"
                    type="text"
                    placeholder="Ej: tomate, pollo, cebolla..."
                    class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  />
                  <button
                    @click="addIngredient"
                    :disabled="!newIngredient.trim()"
                    class="px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
                  >
                    Agregar
                  </button>
                </div>
                
                <!-- Ingredients List -->
                <div v-if="ingredients.length > 0" class="flex flex-wrap gap-2 p-4 bg-gray-50 rounded-lg">
                  <span
                    v-for="(ingredient, index) in ingredients"
                    :key="index"
                    class="inline-flex items-center gap-2 px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm"
                  >
                    {{ ingredient }}
                    <button
                      @click="removeIngredient(index)"
                      class="text-green-600 hover:text-green-800"
                    >
                      ×
                    </button>
                  </span>
                </div>
                <p v-else class="text-gray-500 text-sm">
                  Agrega al menos 2 ingredientes para generar una receta
                </p>
              </div>
            </div>

            <!-- Preferences -->
            <div class="mb-8">
              <h3 class="text-xl font-semibold text-gray-800 mb-4 flex items-center gap-2">
                ⚙️ Preferencias (Opcional)
              </h3>
              
              <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                <!-- Diet Preference -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Tipo de Dieta
                  </label>
                  <select
                    v-model="preferences.diet"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">Sin preferencia</option>
                    <option value="VEGETARIAN">Vegetariana</option>
                    <option value="VEGAN">Vegana</option>
                    <option value="KETO">Keto</option>
                    <option value="PALEO">Paleo</option>
                    <option value="MEDITERRANEAN">Mediterránea</option>
                  </select>
                </div>

                <!-- Allergens -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">
                    Alergias/Intolerancias
                  </label>
                  <div class="space-y-2">
                    <label v-for="allergen in commonAllergens" :key="allergen" class="flex items-center">
                      <input
                        v-model="preferences.allergens"
                        :value="allergen"
                        type="checkbox"
                        class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                      />
                      <span class="ml-2 text-sm text-gray-700">{{ allergen }}</span>
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <!-- Generate Button -->
            <div class="text-center">
              <div class="flex flex-col sm:flex-row gap-4 justify-center items-center">
                <button
                  @click="generateRecipe"
                  :disabled="ingredients.length < 2 || isGenerating"
                  class="inline-flex items-center gap-2 px-8 py-4 bg-gradient-to-r from-green-600 to-blue-600 text-white rounded-xl font-semibold text-lg hover:from-green-700 hover:to-blue-700 disabled:from-gray-400 disabled:to-gray-400 disabled:cursor-not-allowed transition-all duration-300 transform hover:scale-105"
                >
                  <svg v-if="isGenerating" class="animate-spin h-5 w-5" fill="none" viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                  <span v-else>✨</span>
                  {{ isGenerating ? 'Generando receta...' : 'Generar Receta con IA' }}
                </button>
                
                <button
                  v-if="ingredients.length > 0 || generatedRecipe"
                  @click="clearAll"
                  class="inline-flex items-center gap-2 px-6 py-3 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition-colors"
                >
                  🗑️ Limpiar Todo
                </button>
              </div>
              <p class="text-xs text-gray-500 mt-2">
                Mínimo 2 ingredientes requeridos
              </p>
            </div>
          </div>
        </div>

        <!-- Sidebar -->
        <div class="space-y-6">
          <!-- Quick Suggestions -->
          <div class="bg-white rounded-2xl shadow-lg p-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">
              💡 Sugerencias Rápidas
            </h3>
            <div class="space-y-3">
              <button
                v-for="suggestion in quickSuggestions"
                :key="suggestion.name"
                @click="useQuickSuggestion(suggestion.ingredients)"
                class="w-full text-left p-3 bg-gray-50 hover:bg-green-50 rounded-lg transition-colors"
              >
                <div class="font-medium text-gray-800">{{ suggestion.name }}</div>
                <div class="text-sm text-gray-600">{{ suggestion.ingredients.join(', ') }}</div>
              </button>
            </div>
          </div>

          <!-- Recent Suggestions -->
          <div v-if="recentSuggestions.length > 0" class="bg-white rounded-2xl shadow-lg p-6">
            <h3 class="text-lg font-semibold text-gray-800 mb-4 flex items-center gap-2">
              🕒 Recetas Recientes
            </h3>
            <div class="space-y-3">
              <div
                v-for="recent in recentSuggestions"
                :key="recent.id"
                class="p-3 bg-gray-50 rounded-lg"
              >
                <div class="font-medium text-gray-800 text-sm">{{ recent.generatedRecipe.title }}</div>
                <div class="text-xs text-gray-500 mt-1">
                  {{ formatDate(recent.createdAt) }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Generated Recipe Display -->
      <div v-if="generatedRecipe" class="mt-8">
        <AIRecipeCard
          :recipe="generatedRecipe"
          @save="handleSaveRecipe"
          @share="handleShareRecipe"
          @show-details="showFullRecipe = true"
          @favorite-toggled="handleFavoriteToggle"
        />
      </div>

      <!-- Full Recipe Modal -->
      <div v-if="showFullRecipe && generatedRecipe" class="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4">
        <div class="bg-white rounded-2xl shadow-2xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
          <div class="sticky top-0 bg-white border-b px-6 py-4 flex items-center justify-between">
            <h2 class="text-2xl font-bold text-gray-900">{{ generatedRecipe.title }}</h2>
            <button
              @click="showFullRecipe = false"
              class="p-2 hover:bg-gray-100 rounded-full transition-colors"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          
          <div class="p-6">
            <p class="text-lg text-gray-600 mb-6">{{ generatedRecipe.summary }}</p>
            
            <!-- Tags -->
            <div v-if="generatedRecipe.tags.length > 0" class="mb-6">
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="tag in generatedRecipe.tags"
                  :key="tag"
                  class="px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm"
                >
                  #{{ tag }}
                </span>
              </div>
            </div>

            <!-- Instructions -->
            <div>
              <h3 class="text-xl font-semibold text-gray-800 mb-4">📝 Instrucciones Detalladas</h3>
              <div class="space-y-4">
                <div
                  v-for="(step, index) in generatedRecipe.steps"
                  :key="index"
                  class="flex gap-4 p-4 bg-gray-50 rounded-lg"
                >
                  <div class="flex-shrink-0 w-8 h-8 bg-green-600 text-white rounded-full flex items-center justify-center font-semibold">
                    {{ index + 1 }}
                  </div>
                  <div class="flex-1 text-gray-700">{{ step }}</div>
                </div>
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="mt-8 flex gap-4 justify-center">
              <button
                @click="handleSaveRecipe(generatedRecipe)"
                class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
              >
                � Guardar Receta
              </button>
              <button
                @click="handleShareRecipe(generatedRecipe)"
                class="px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
              >
                � Compartir
              </button>
              <button
                @click="generateAnother"
                class="px-6 py-3 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
              >
                🔄 Generar Otra
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Error State -->
      <div v-if="error" class="mt-8">
        <div class="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
          <div class="text-red-600 text-lg font-medium mb-2">
            ⚠️ Error al generar la receta
          </div>
          <p class="text-red-600 mb-4">{{ error }}</p>
          <button
            @click="error = null"
            class="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
          >
            Cerrar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { suggestService, type SuggestRequest, type SuggestResponse, type RecipeSuggestion } from '../services'
import AIRecipeCard from '../components/AIRecipeCard.vue'

// Reactive data
const ingredients = ref<string[]>([])
const newIngredient = ref('')
const preferences = ref({
  diet: '',
  allergens: [] as string[]
})
const isGenerating = ref(false)
const generatedRecipe = ref<SuggestResponse | null>(null)
const error = ref<string | null>(null)
const recentSuggestions = ref<RecipeSuggestion[]>([])
const showFullRecipe = ref(false)

const commonAllergens = [
  'Gluten', 'Lácteos', 'Huevos', 'Frutos secos', 
  'Mariscos', 'Soja', 'Sesamo'
]

const quickSuggestions = [
  {
    name: 'Pasta Rápida',
    ingredients: ['pasta', 'tomate', 'ajo', 'aceite de oliva']
  },
  {
    name: 'Ensalada Fresca',
    ingredients: ['lechuga', 'tomate', 'pepino', 'aceite de oliva']
  },
  {
    name: 'Pollo al Horno',
    ingredients: ['pollo', 'limón', 'ajo', 'romero']
  },
  {
    name: 'Tortilla Española',
    ingredients: ['huevos', 'patatas', 'cebolla', 'aceite']
  }
]

// Methods
const addIngredient = () => {
  const ingredient = newIngredient.value.trim()
  if (ingredient && !ingredients.value.includes(ingredient)) {
    ingredients.value.push(ingredient)
    newIngredient.value = ''
  }
}

const removeIngredient = (index: number) => {
  ingredients.value.splice(index, 1)
}

const useQuickSuggestion = (suggestionIngredients: string[]) => {
  ingredients.value = [...suggestionIngredients]
}

const generateRecipe = async () => {
  if (ingredients.value.length < 2) return
  
  isGenerating.value = true
  error.value = null
  
  try {
    const request: SuggestRequest = {
      ingredients: ingredients.value,
      preferences: {
        diet: preferences.value.diet || undefined,
        allergens: preferences.value.allergens.length > 0 ? preferences.value.allergens : undefined
      }
    }
    
    generatedRecipe.value = await suggestService.getSuggestions(request)
    
    // Load recent suggestions after generating
    loadRecentSuggestions()
    
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Error al conectar con el servicio de IA'
    console.error('Error generating recipe:', err)
  } finally {
    isGenerating.value = false
  }
}

const handleSaveRecipe = async (recipe: SuggestResponse) => {
  try {
    await suggestService.saveSuggestion(recipe)
    // Show success message
    alert('¡Receta guardada exitosamente!')
  } catch (err) {
    console.error('Error saving recipe:', err)
    alert('Error al guardar la receta')
  }
}

const handleShareRecipe = (recipe: SuggestResponse) => {
  const text = `🍳 ${recipe.title}\n\n${recipe.summary}\n\nGenerado con Recipe Assistant AI`
  
  if (navigator.share) {
    navigator.share({
      title: recipe.title,
      text: text
    })
  } else {
    navigator.clipboard.writeText(text)
    alert('Receta copiada al portapapeles!')
  }
}

const handleFavoriteToggle = (isFavorited: boolean) => {
  // Handle favorite toggle logic here
  console.log('Recipe favorited:', isFavorited)
}

const generateAnother = () => {
  generatedRecipe.value = null
  showFullRecipe.value = false
  // Keep ingredients and preferences for convenience
}

const clearAll = () => {
  ingredients.value = []
  newIngredient.value = ''
  preferences.value = { diet: '', allergens: [] }
  generatedRecipe.value = null
  showFullRecipe.value = false
  error.value = null
}

const loadRecentSuggestions = async () => {
  try {
    recentSuggestions.value = await suggestService.getRecentSuggestions(3)
  } catch (err) {
    console.error('Error loading recent suggestions:', err)
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'short',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// Load recent suggestions on component mount
onMounted(() => {
  loadRecentSuggestions()
})
</script>