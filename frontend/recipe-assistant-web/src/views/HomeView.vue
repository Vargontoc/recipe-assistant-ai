<template>
  <div class="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-cyan-50">
    <!-- Hero Section -->
    <section class="relative py-20 overflow-hidden">
      <div class="absolute inset-0 bg-gradient-to-r from-indigo-600 to-purple-600 opacity-10"></div>
      <div class="relative max-w-7xl mx-auto px-6">
        <div class="text-center">
          <h1 class="text-5xl md:text-6xl font-bold text-gray-900 mb-6">
            <span class="text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600">
              Recipe Assistant AI
            </span>
            <div class="text-4xl md:text-5xl mt-2">🤖👨‍🍳✨</div>
          </h1>
          <p class="text-xl md:text-2xl text-gray-600 mb-8 max-w-3xl mx-auto">
            Tu asistente culinario inteligente que transforma ingredientes simples en 
            <span class="font-semibold text-indigo-600">experiencias gastronómicas extraordinarias</span>
          </p>
          
          <!-- Main CTA -->
          <div class="flex flex-col sm:flex-row gap-4 justify-center items-center mb-12">
            <router-link
              to="/suggest"
              class="group relative inline-flex items-center gap-3 px-8 py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-2xl font-semibold text-lg shadow-lg hover:shadow-xl transform hover:scale-105 transition-all duration-300"
            >
              <span class="text-2xl">🎯</span>
              Crear Receta con IA
              <svg class="w-5 h-5 group-hover:translate-x-1 transition-transform" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
              </svg>
            </router-link>
            
            <router-link
              to="/search"
              class="inline-flex items-center gap-2 px-6 py-3 border-2 border-indigo-600 text-indigo-600 rounded-xl font-semibold hover:bg-indigo-50 transition-colors"
            >
              <span>🔍</span>
              Explorar Recetas
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-6">
        <div class="text-center mb-16">
          <h2 class="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
            ¿Qué hace especial a nuestro asistente?
          </h2>
          <p class="text-xl text-gray-600">
            Tecnología de inteligencia artificial para llevarte de ingredientes a obra maestra
          </p>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <!-- AI Recipe Generation -->
          <div class="group p-8 bg-gradient-to-br from-green-50 to-emerald-50 rounded-2xl border border-green-100 hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
            <div class="text-4xl mb-4 group-hover:scale-110 transition-transform">🤖</div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Generación Inteligente</h3>
            <p class="text-gray-600 mb-4">
              Ingresa tus ingredientes disponibles y nuestra IA creará recetas personalizadas al instante
            </p>
            <router-link to="/suggest" class="text-green-600 font-semibold hover:text-green-700">
              Probar ahora →
            </router-link>
          </div>

          <!-- Smart Search -->
          <div class="group p-8 bg-gradient-to-br from-blue-50 to-cyan-50 rounded-2xl border border-blue-100 hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
            <div class="text-4xl mb-4 group-hover:scale-110 transition-transform">🎯</div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Búsqueda Inteligente</h3>
            <p class="text-gray-600 mb-4">
              Encuentra recetas con filtros avanzados: dietas, alergias, tiempo de cocción y más
            </p>
            <router-link to="/search" class="text-blue-600 font-semibold hover:text-blue-700">
              Explorar →
            </router-link>
          </div>

          <!-- Personalization -->
          <div class="group p-8 bg-gradient-to-br from-purple-50 to-pink-50 rounded-2xl border border-purple-100 hover:shadow-lg transition-all duration-300 hover:-translate-y-1">
            <div class="text-4xl mb-4 group-hover:scale-110 transition-transform">💝</div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Personalización Total</h3>
            <p class="text-gray-600 mb-4">
              Guarda favoritos, califica recetas y obtén recomendaciones basadas en tus gustos
            </p>
            <router-link to="/favorites" class="text-purple-600 font-semibold hover:text-purple-700">
              Ver favoritos →
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- How it Works -->
    <section class="py-16 bg-gray-50">
      <div class="max-w-7xl mx-auto px-6">
        <div class="text-center mb-16">
          <h2 class="text-3xl md:text-4xl font-bold text-gray-900 mb-4">
            Cómo funciona la magia
          </h2>
          <p class="text-xl text-gray-600">
            Solo 3 pasos para tu próxima comida perfecta
          </p>
        </div>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div class="text-center">
            <div class="w-20 h-20 bg-gradient-to-r from-indigo-600 to-purple-600 rounded-full flex items-center justify-center text-white text-2xl font-bold mx-auto mb-6">
              1
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">Ingredientes</h3>
            <p class="text-gray-600">
              Simplemente dinos qué tienes en tu nevera o despensa
            </p>
          </div>
          
          <div class="text-center">
            <div class="w-20 h-20 bg-gradient-to-r from-green-600 to-teal-600 rounded-full flex items-center justify-center text-white text-2xl font-bold mx-auto mb-6">
              2
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">IA Trabaja</h3>
            <p class="text-gray-600">
              Nuestra inteligencia artificial analiza y crea recetas únicas
            </p>
          </div>
          
          <div class="text-center">
            <div class="w-20 h-20 bg-gradient-to-r from-orange-600 to-red-600 rounded-full flex items-center justify-center text-white text-2xl font-bold mx-auto mb-6">
              3
            </div>
            <h3 class="text-xl font-bold text-gray-900 mb-3">¡A Cocinar!</h3>
            <p class="text-gray-600">
              Recibe instrucciones paso a paso para crear tu obra maestra
            </p>
          </div>
        </div>
        
        <div class="text-center mt-12">
          <router-link
            to="/suggest"
            class="inline-flex items-center gap-2 px-8 py-4 bg-gradient-to-r from-indigo-600 to-purple-600 text-white rounded-xl font-semibold text-lg hover:shadow-lg transform hover:scale-105 transition-all duration-300"
          >
            <span class="text-xl">🚀</span>
            Empezar Ahora
          </router-link>
        </div>
      </div>
    </section>

    <!-- Recent Recipes Section -->
    <section class="py-16 bg-white">
      <div class="max-w-7xl mx-auto px-6">
        <div class="flex items-center justify-between mb-8">
          <div>
            <h2 class="text-3xl font-bold text-gray-900 mb-2">Recetas Populares</h2>
            <p class="text-gray-600">Descubre las creaciones más valoradas por nuestra comunidad</p>
          </div>
          <router-link
            to="/search"
            class="hidden md:inline-flex items-center gap-2 px-6 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
          >
            Ver todas
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </router-link>
        </div>

        <!-- Loading State -->
        <LoadingState v-if="loading" message="Cargando recetas populares..." />

        <!-- Recipes Grid -->
        <div v-else-if="items.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <RecipeCard
            v-for="recipe in items.slice(0, 6)"
            :key="recipe.id"
            :recipe="recipe"
            @favorite-toggled="handleFavoriteToggle"
          />
        </div>

        <!-- Empty State -->
        <div v-else class="text-center py-12">
          <div class="text-6xl mb-4">🍳</div>
          <h3 class="text-xl font-semibold text-gray-900 mb-2">
            ¡Sé el primero en crear una receta!
          </h3>
          <p class="text-gray-600 mb-6">
            Usa nuestro asistente IA para crear la primera receta de la comunidad
          </p>
          <router-link
            to="/suggest"
            class="inline-flex items-center gap-2 px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition-colors"
          >
            <span>✨</span>
            Crear Primera Receta
          </router-link>
        </div>
      </div>
    </section>

    <!-- Stats Section -->
    <section class="py-16 bg-gradient-to-r from-indigo-600 to-purple-600 text-white">
      <div class="max-w-7xl mx-auto px-6">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
          <div>
            <div class="text-3xl md:text-4xl font-bold mb-2">{{ userStats.totalFavorites || 0 }}</div>
            <div class="text-indigo-200">Favoritos Guardados</div>
          </div>
          <div>
            <div class="text-3xl md:text-4xl font-bold mb-2">{{ userStats.totalSearches || 0 }}</div>
            <div class="text-indigo-200">Búsquedas Realizadas</div>
          </div>
          <div>
            <div class="text-3xl md:text-4xl font-bold mb-2">{{ totalRecipes }}</div>
            <div class="text-indigo-200">Recetas Disponibles</div>
          </div>
          <div>
            <div class="text-3xl md:text-4xl font-bold mb-2">∞</div>
            <div class="text-indigo-200">Posibilidades IA</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import RecipeCard from '../components/RecipeCard.vue'
import LoadingState from '../components/LoadingState.vue'
import { http, favoritesService, searchHistoryService } from '../services'

const router = useRouter()

// Reactive data
const loading = ref(true)
const items = ref<any[]>([])
const totalRecipes = ref(0)
const userStats = ref({
  totalFavorites: 0,
  totalSearches: 0
})

// Methods
const loadPopularRecipes = async () => {
  try {
    loading.value = true
    const response = await http.get('/recipes', {
      params: { page: 0, size: 6, sort: 'averageRating,desc' }
    })
    items.value = response.data.content || []
    totalRecipes.value = response.data.totalElements || 0
  } catch (error) {
    console.error('Error loading recipes:', error)
    items.value = []
  } finally {
    loading.value = false
  }
}

const loadUserStats = async () => {
  try {
    const [favoritesCount, searchStats] = await Promise.all([
      favoritesService.getUserFavoriteCount(),
      searchHistoryService.getUserSearchStats()
    ])
    
    userStats.value = {
      totalFavorites: favoritesCount,
      totalSearches: searchStats.totalSearches
    }
  } catch (error) {
    console.error('Error loading user stats:', error)
  }
}

const handleFavoriteToggle = (recipeId: string, isFavorited: boolean) => {
  // Update local stats
  if (isFavorited) {
    userStats.value.totalFavorites++
  } else {
    userStats.value.totalFavorites--
  }
}

// Lifecycle
onMounted(() => {
  loadPopularRecipes()
  loadUserStats()
})
</script>