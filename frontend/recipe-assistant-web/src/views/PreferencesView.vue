<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-4xl mx-auto">
      <!-- Header -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2 flex items-center gap-3">
          ⚙️ Preferencias
        </h1>
        <p class="text-gray-600">
          Configura tu experiencia personalizada en Recipe Assistant AI
        </p>
      </div>

      <!-- Main Content -->
      <div class="space-y-8">
        <!-- Dietary Preferences -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
          <div class="flex items-center gap-3 mb-6">
            <div class="p-3 bg-green-100 rounded-full">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4" />
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-semibold text-gray-900">Preferencias Dietéticas</h2>
              <p class="text-gray-600">Configura tu dieta y restricciones alimentarias</p>
            </div>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <!-- Diet Type -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-3">Tipo de Dieta</label>
              <div class="space-y-2">
                <label v-for="diet in dietOptions" :key="diet.value" class="flex items-center">
                  <input
                    type="radio"
                    :value="diet.value"
                    v-model="preferences.dietType"
                    class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300"
                  />
                  <span class="ml-3 text-gray-700">{{ diet.emoji }} {{ diet.label }}</span>
                </label>
              </div>
            </div>

            <!-- Allergies -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-3">Alergias e Intolerancias</label>
              <div class="space-y-2">
                <label v-for="allergy in allergyOptions" :key="allergy.value" class="flex items-center">
                  <input
                    type="checkbox"
                    :value="allergy.value"
                    v-model="preferences.allergies"
                    class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                  />
                  <span class="ml-3 text-gray-700">{{ allergy.emoji }} {{ allergy.label }}</span>
                </label>
              </div>
            </div>
          </div>
        </div>

        <!-- AI Preferences -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
          <div class="flex items-center gap-3 mb-6">
            <div class="p-3 bg-purple-100 rounded-full">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-semibold text-gray-900">Preferencias de IA</h2>
              <p class="text-gray-600">Personaliza cómo la IA genera sugerencias para ti</p>
            </div>
          </div>

          <div class="space-y-6">
            <!-- Complexity Level -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-3">
                Nivel de Complejidad Preferido
              </label>
              <div class="flex items-center space-x-4">
                <input
                  type="range"
                  min="1"
                  max="5"
                  v-model="preferences.complexityLevel"
                  class="flex-1 h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer slider"
                />
                <span class="text-sm font-medium text-gray-700 min-w-0">
                  {{ getComplexityLabel(preferences.complexityLevel) }}
                </span>
              </div>
              <div class="flex justify-between text-xs text-gray-500 mt-1">
                <span>Muy Simple</span>
                <span>Muy Complejo</span>
              </div>
            </div>

            <!-- Cooking Time Preference -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-3">
                Tiempo de Cocción Preferido (minutos)
              </label>
              <div class="flex items-center space-x-4">
                <input
                  type="range"
                  min="5"
                  max="120"
                  v-model="preferences.maxCookingTime"
                  class="flex-1 h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer slider"
                />
                <span class="text-sm font-medium text-gray-700 min-w-0">
                  {{ preferences.maxCookingTime }} min
                </span>
              </div>
            </div>

            <!-- Cuisine Preferences -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-3">Cocinas Favoritas</label>
              <div class="grid grid-cols-2 md:grid-cols-3 gap-3">
                <label v-for="cuisine in cuisineOptions" :key="cuisine.value" class="flex items-center">
                  <input
                    type="checkbox"
                    :value="cuisine.value"
                    v-model="preferences.favoriteCuisines"
                    class="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded"
                  />
                  <span class="ml-3 text-gray-700">{{ cuisine.emoji }} {{ cuisine.label }}</span>
                </label>
              </div>
            </div>
          </div>
        </div>

        <!-- Notification Preferences -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
          <div class="flex items-center gap-3 mb-6">
            <div class="p-3 bg-blue-100 rounded-full">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 17h5l-5 5v-5zM4 6h16l-7 6H4z" />
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-semibold text-gray-900">Notificaciones</h2>
              <p class="text-gray-600">Controla qué notificaciones quieres recibir</p>
            </div>
          </div>

          <div class="space-y-4">
            <div class="flex items-center justify-between">
              <div>
                <h3 class="text-sm font-medium text-gray-900">Nuevas recetas de IA</h3>
                <p class="text-sm text-gray-500">Recibe notificaciones sobre nuevas sugerencias personalizadas</p>
              </div>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="preferences.notifications.aiSuggestions" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
              </label>
            </div>

            <div class="flex items-center justify-between">
              <div>
                <h3 class="text-sm font-medium text-gray-900">Recordatorios de cocina</h3>
                <p class="text-sm text-gray-500">Recibe recordatorios para probar recetas guardadas</p>
              </div>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="preferences.notifications.cookingReminders" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
              </label>
            </div>

            <div class="flex items-center justify-between">
              <div>
                <h3 class="text-sm font-medium text-gray-900">Actualizaciones del sistema</h3>
                <p class="text-sm text-gray-500">Mantente informado sobre nuevas funciones y mejoras</p>
              </div>
              <label class="relative inline-flex items-center cursor-pointer">
                <input type="checkbox" v-model="preferences.notifications.systemUpdates" class="sr-only peer">
                <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
              </label>
            </div>
          </div>
        </div>

        <!-- Account Settings -->
        <div class="bg-white rounded-2xl shadow-sm border border-gray-200 p-8">
          <div class="flex items-center gap-3 mb-6">
            <div class="p-3 bg-orange-100 rounded-full">
              <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
            <div>
              <h2 class="text-xl font-semibold text-gray-900">Configuración de Cuenta</h2>
              <p class="text-gray-600">Gestiona tu perfil y privacidad</p>
            </div>
          </div>

          <div class="space-y-6">
            <!-- Language -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Idioma</label>
              <select
                v-model="preferences.language"
                class="w-full md:w-auto px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
              >
                <option value="es">🇪🇸 Español</option>
                <option value="en">🇺🇸 English</option>
                <option value="fr">🇫🇷 Français</option>
                <option value="de">🇩🇪 Deutsch</option>
              </select>
            </div>

            <!-- Privacy Settings -->
            <div>
              <h3 class="text-sm font-medium text-gray-900 mb-3">Configuración de Privacidad</h3>
              <div class="space-y-3">
                <div class="flex items-center justify-between">
                  <span class="text-sm text-gray-700">Hacer mi perfil público</span>
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input type="checkbox" v-model="preferences.privacy.publicProfile" class="sr-only peer">
                    <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-orange-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-orange-600"></div>
                  </label>
                </div>
                <div class="flex items-center justify-between">
                  <span class="text-sm text-gray-700">Compartir mis recetas favoritas</span>
                  <label class="relative inline-flex items-center cursor-pointer">
                    <input type="checkbox" v-model="preferences.privacy.shareRecipes" class="sr-only peer">
                    <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-orange-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-orange-600"></div>
                  </label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="flex flex-col sm:flex-row gap-4 justify-end">
          <button
            @click="resetToDefaults"
            class="px-6 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
          >
            Restaurar por defecto
          </button>
          <button
            @click="savePreferences"
            :disabled="isSaving"
            class="px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-lg hover:from-blue-700 hover:to-purple-700 transition-all disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ isSaving ? 'Guardando...' : 'Guardar Cambios' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Success Toast -->
    <div
      v-if="showSuccessToast"
      class="fixed bottom-6 right-6 bg-green-600 text-white px-6 py-3 rounded-lg shadow-lg transform transition-transform"
      :class="showSuccessToast ? 'translate-y-0' : 'translate-y-full'"
    >
      ✅ Preferencias guardadas correctamente
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'

// Types
interface UserPreferences {
  dietType: string
  allergies: string[]
  complexityLevel: number
  maxCookingTime: number
  favoriteCuisines: string[]
  notifications: {
    aiSuggestions: boolean
    cookingReminders: boolean
    systemUpdates: boolean
  }
  language: string
  privacy: {
    publicProfile: boolean
    shareRecipes: boolean
  }
}

// Reactive data
const preferences = reactive<UserPreferences>({
  dietType: 'none',
  allergies: [],
  complexityLevel: 3,
  maxCookingTime: 60,
  favoriteCuisines: [],
  notifications: {
    aiSuggestions: true,
    cookingReminders: true,
    systemUpdates: false
  },
  language: 'es',
  privacy: {
    publicProfile: false,
    shareRecipes: true
  }
})

const isSaving = ref(false)
const showSuccessToast = ref(false)

// Options
const dietOptions = [
  { value: 'none', label: 'Sin restricciones', emoji: '🍽️' },
  { value: 'vegetarian', label: 'Vegetariana', emoji: '🥬' },
  { value: 'vegan', label: 'Vegana', emoji: '🌱' },
  { value: 'pescatarian', label: 'Pescetariana', emoji: '🐟' },
  { value: 'keto', label: 'Cetogénica', emoji: '🥑' },
  { value: 'paleo', label: 'Paleo', emoji: '🍖' },
  { value: 'mediterranean', label: 'Mediterránea', emoji: '🫒' },
  { value: 'lowCarb', label: 'Baja en carbohidratos', emoji: '🥩' }
]

const allergyOptions = [
  { value: 'gluten', label: 'Gluten', emoji: '🌾' },
  { value: 'dairy', label: 'Lácteos', emoji: '🥛' },
  { value: 'nuts', label: 'Frutos secos', emoji: '🥜' },
  { value: 'eggs', label: 'Huevos', emoji: '🥚' },
  { value: 'soy', label: 'Soja', emoji: '🫘' },
  { value: 'fish', label: 'Pescado', emoji: '🐠' },
  { value: 'shellfish', label: 'Mariscos', emoji: '🦐' },
  { value: 'sesame', label: 'Sésamo', emoji: '🫘' }
]

const cuisineOptions = [
  { value: 'mediterranean', label: 'Mediterránea', emoji: '🫒' },
  { value: 'italian', label: 'Italiana', emoji: '🍝' },
  { value: 'mexican', label: 'Mexicana', emoji: '🌮' },
  { value: 'asian', label: 'Asiática', emoji: '🍜' },
  { value: 'indian', label: 'India', emoji: '🍛' },
  { value: 'japanese', label: 'Japonesa', emoji: '🍣' },
  { value: 'french', label: 'Francesa', emoji: '🥖' },
  { value: 'american', label: 'Americana', emoji: '🍔' },
  { value: 'spanish', label: 'Española', emoji: '🥘' }
]

// Methods
const getComplexityLabel = (level: number): string => {
  const labels = {
    1: 'Muy Simple',
    2: 'Simple',
    3: 'Moderado',
    4: 'Complejo',
    5: 'Muy Complejo'
  }
  return labels[level as keyof typeof labels] || 'Moderado'
}

const savePreferences = async () => {
  isSaving.value = true
  
  try {
    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // Save to localStorage for persistence
    localStorage.setItem('userPreferences', JSON.stringify(preferences))
    
    // Show success toast
    showSuccessToast.value = true
    setTimeout(() => {
      showSuccessToast.value = false
    }, 3000)
    
  } catch (error) {
    console.error('Error saving preferences:', error)
    alert('Error al guardar las preferencias. Inténtalo de nuevo.')
  } finally {
    isSaving.value = false
  }
}

const resetToDefaults = () => {
  if (confirm('¿Estás seguro de que quieres restaurar todas las preferencias por defecto?')) {
    Object.assign(preferences, {
      dietType: 'none',
      allergies: [],
      complexityLevel: 3,
      maxCookingTime: 60,
      favoriteCuisines: [],
      notifications: {
        aiSuggestions: true,
        cookingReminders: true,
        systemUpdates: false
      },
      language: 'es',
      privacy: {
        publicProfile: false,
        shareRecipes: true
      }
    })
  }
}

const loadPreferences = () => {
  try {
    const saved = localStorage.getItem('userPreferences')
    if (saved) {
      Object.assign(preferences, JSON.parse(saved))
    }
  } catch (error) {
    console.error('Error loading preferences:', error)
  }
}

// Lifecycle
onMounted(() => {
  loadPreferences()
})
</script>

<style scoped>
.slider::-webkit-slider-thumb {
  appearance: none;
  height: 20px;
  width: 20px;
  border-radius: 50%;
  background: #3b82f6;
  cursor: pointer;
}

.slider::-moz-range-thumb {
  height: 20px;
  width: 20px;
  border-radius: 50%;
  background: #3b82f6;
  cursor: pointer;
  border: none;
}
</style>