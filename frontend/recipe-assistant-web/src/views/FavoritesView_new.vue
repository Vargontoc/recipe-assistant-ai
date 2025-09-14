<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-6xl mx-auto">
      <!-- Header -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900 mb-2 flex items-center gap-3">
              💝 Mis Favoritos
            </h1>
            <p class="text-gray-600">
              Tu colección personal de recetas favoritas
            </p>
          </div>
          
          <!-- Quick Actions -->
          <div class="flex gap-3">
            <router-link
              to="/search"
              class="inline-flex items-center gap-2 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
              Explorar más recetas
            </router-link>
            <router-link
              to="/suggest"
              class="inline-flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
            >
              ✨ Crear con IA
            </router-link>
          </div>
        </div>
      </div>

      <!-- Stats -->
      <div v-if="favorites.length > 0" class="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Total Favoritos</p>
              <p class="text-2xl font-bold text-red-600">{{ favorites.length }}</p>
            </div>
            <div class="p-3 bg-red-100 rounded-full">
              <svg class="w-6 h-6 text-red-600" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
              </svg>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Esta Semana</p>
              <p class="text-2xl font-bold text-blue-600">{{ getThisWeekCount() }}</p>
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
              <p class="text-sm font-medium text-gray-600">Más Valoradas</p>
              <p class="text-2xl font-bold text-yellow-600">{{ getHighRatedCount() }}</p>
            </div>
            <div class="p-3 bg-yellow-100 rounded-full">
              <svg class="w-6 h-6 text-yellow-600" fill="currentColor" viewBox="0 0 24 24">
                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
              </svg>
            </div>
          </div>
        </div>
        
        <div class="bg-white rounded-lg p-6 shadow-sm border border-gray-200">
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm font-medium text-gray-600">Categorías</p>
              <p class="text-2xl font-bold text-purple-600">{{ getUniqueTagsCount() }}</p>
            </div>
            <div class="p-3 bg-purple-100 rounded-full">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <!-- Filters and Sort -->
      <div v-if="favorites.length > 0" class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8">
        <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div class="flex items-center gap-4">
            <h3 class="font-semibold text-gray-900">Filtrar y Ordenar</h3>
            <select
              v-model="sortBy"
              class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="recent">Más recientes</option>
              <option value="oldest">Más antiguos</option>
              <option value="rating">Mejor valoradas</option>
              <option value="name">Por nombre (A-Z)</option>
            </select>
          </div>
          
          <div class="flex items-center gap-3">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Buscar en favoritos..."
              class="border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <button
              v-if="searchQuery"
              @click="searchQuery = ''"
              class="p-2 text-gray-400 hover:text-gray-600"
            >
              <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading" class="flex justify-center items-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-red-600"></div>
        <p class="ml-4 text-gray-600">Cargando tus favoritos...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="text-center py-12">
        <div class="text-red-500 text-6xl mb-4">⚠️</div>
        <h3 class="text-xl font-semibold text-gray-900 mb-2">Error al cargar favoritos</h3>
        <p class="text-gray-600 mb-6">{{ error }}</p>
        <button
          @click="loadFavorites"
          class="px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
        >
          Reintentar
        </button>
      </div>

      <!-- Empty State -->
      <div v-else-if="favorites.length === 0" class="text-center py-16">
        <div class="text-8xl mb-6">💝</div>
        <h3 class="text-2xl font-bold text-gray-900 mb-3">
          Tu colección de favoritos está vacía
        </h3>
        <p class="text-gray-600 mb-8 max-w-md mx-auto">
          Empieza a explorar recetas y guarda las que más te gusten haciendo clic en el corazón
        </p>
        
        <div class="flex flex-col sm:flex-row gap-4 justify-center">
          <router-link
            to="/search"
            class="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
          >
            🔍 Explorar Recetas
          </router-link>
          <router-link
            to="/suggest"
            class="inline-flex items-center gap-2 px-6 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
          >
            ✨ Crear con IA
          </router-link>
        </div>
      </div>

      <!-- Favorites Grid -->
      <div v-else>
        <!-- No results for search -->
        <div v-if="filteredFavorites.length === 0" class="text-center py-12">
          <div class="text-6xl mb-4">🔍</div>
          <h3 class="text-xl font-semibold text-gray-900 mb-2">
            No se encontraron recetas
          </h3>
          <p class="text-gray-600 mb-4">
            No hay favoritos que coincidan con "{{ searchQuery }}"
          </p>
          <button
            @click="searchQuery = ''"
            class="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700 transition-colors"
          >
            Limpiar búsqueda
          </button>
        </div>

        <!-- Recipe Cards Grid -->
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="favorite in filteredFavorites"
            :key="favorite.id"
            class="group bg-white rounded-2xl shadow-sm hover:shadow-lg transition-all duration-300 overflow-hidden border border-gray-200"
          >
            <!-- Recipe Header -->
            <div class="p-6">
              <div class="flex items-start justify-between mb-4">
                <div class="flex-1">
                  <h3 class="text-lg font-semibold text-gray-900 mb-2 group-hover:text-blue-600 transition-colors">
                    {{ favorite.recipe.title }}
                  </h3>
                  <p class="text-gray-600 text-sm line-clamp-2">
                    {{ favorite.recipe.summary || 'Deliciosa receta para disfrutar' }}
                  </p>
                </div>
                <button
                  @click="removeFavorite(favorite.id)"
                  class="p-2 text-red-500 hover:bg-red-50 rounded-full transition-colors"
                  title="Quitar de favoritos"
                >
                  <svg class="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                  </svg>
                </button>
              </div>

              <!-- Recipe Info -->
              <div class="flex items-center gap-4 text-sm text-gray-500 mb-4">
                <span v-if="favorite.recipe.averageRating" class="flex items-center gap-1">
                  ⭐ {{ favorite.recipe.averageRating.toFixed(1) }}
                </span>
                <span v-if="favorite.recipe.totalIngredients" class="flex items-center gap-1">
                  🥕 {{ favorite.recipe.totalIngredients }} ingredientes
                </span>
                <span class="flex items-center gap-1">
                  📅 {{ formatDate(favorite.createdAt) }}
                </span>
              </div>

              <!-- Tags -->
              <div v-if="favorite.recipe.tags && favorite.recipe.tags.length > 0" class="mb-4">
                <div class="flex flex-wrap gap-2">
                  <span
                    v-for="tag in favorite.recipe.tags.slice(0, 3)"
                    :key="tag"
                    class="text-xs px-2 py-1 bg-blue-100 text-blue-700 rounded-full"
                  >
                    #{{ tag }}
                  </span>
                  <span v-if="favorite.recipe.tags.length > 3" class="text-xs text-gray-500">
                    +{{ favorite.recipe.tags.length - 3 }} más
                  </span>
                </div>
              </div>
            </div>

            <!-- Recipe Actions -->
            <div class="px-6 py-4 bg-gray-50 border-t border-gray-100">
              <div class="flex items-center justify-between">
                <div class="text-xs text-gray-500">
                  Añadido {{ formatTimeAgo(favorite.createdAt) }}
                </div>
                <div class="flex gap-2">
                  <router-link
                    :to="`/recipes/${favorite.recipe.id}`"
                    class="px-3 py-1 text-xs bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors"
                  >
                    Ver receta
                  </router-link>
                  <button
                    @click="shareRecipe(favorite.recipe)"
                    class="px-3 py-1 text-xs border border-gray-300 text-gray-700 rounded hover:bg-gray-100 transition-colors"
                  >
                    Compartir
                  </button>
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
import { ref, computed, onMounted } from 'vue'
import { favoritesService, type RecipeFavorite } from '../services'

// Reactive data
const favorites = ref<RecipeFavorite[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)
const searchQuery = ref('')
const sortBy = ref('recent')

// Computed
const filteredFavorites = computed(() => {
  let filtered = favorites.value

  // Filter by search query
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(favorite =>
      favorite.recipe.title.toLowerCase().includes(query) ||
      (favorite.recipe.tags && favorite.recipe.tags.some((tag: string) => tag.toLowerCase().includes(query)))
    )
  }

  // Sort
  switch (sortBy.value) {
    case 'recent':
      return filtered.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
    case 'oldest':
      return filtered.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime())
    case 'rating':
      return filtered.sort((a, b) => (b.recipe.averageRating || 0) - (a.recipe.averageRating || 0))
    case 'name':
      return filtered.sort((a, b) => a.recipe.title.localeCompare(b.recipe.title))
    default:
      return filtered
  }
})

// Methods
const loadFavorites = async () => {
  try {
    isLoading.value = true
    error.value = null
    const response = await favoritesService.getUserFavorites(0, 50)
    favorites.value = response.content || []
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Error al cargar favoritos'
    console.error('Error loading favorites:', err)
  } finally {
    isLoading.value = false
  }
}

const removeFavorite = async (favoriteId: string) => {
  if (!confirm('¿Estás seguro de que quieres quitar esta receta de tus favoritos?')) {
    return
  }

  try {
    // Find the recipe ID from the favorite
    const favorite = favorites.value.find(f => f.id === favoriteId)
    if (!favorite) return

    await favoritesService.removeFavorite(favorite.recipe.id)
    
    // Remove from local array
    favorites.value = favorites.value.filter(f => f.id !== favoriteId)
    
  } catch (err: any) {
    console.error('Error removing favorite:', err)
    alert('Error al quitar de favoritos')
  }
}

const shareRecipe = (recipe: any) => {
  const text = `🍳 ${recipe.title}\n\nDescubre esta deliciosa receta en Recipe Assistant AI`
  
  if (navigator.share) {
    navigator.share({
      title: recipe.title,
      text: text,
      url: `${window.location.origin}/recipes/${recipe.id}`
    })
  } else {
    navigator.clipboard.writeText(`${text}\n${window.location.origin}/recipes/${recipe.id}`)
    alert('Enlace copiado al portapapeles!')
  }
}

const getThisWeekCount = () => {
  const oneWeekAgo = new Date()
  oneWeekAgo.setDate(oneWeekAgo.getDate() - 7)
  
  return favorites.value.filter(favorite => 
    new Date(favorite.createdAt) > oneWeekAgo
  ).length
}

const getHighRatedCount = () => {
  return favorites.value.filter(favorite => 
    (favorite.recipe.averageRating || 0) >= 4.0
  ).length
}

const getUniqueTagsCount = () => {
  const allTags = new Set<string>()
  favorites.value.forEach(favorite => {
    if (favorite.recipe.tags) {
      favorite.recipe.tags.forEach((tag: string) => allTags.add(tag))
    }
  })
  return allTags.size
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric'
  })
}

const formatTimeAgo = (dateString: string) => {
  const date = new Date(dateString)
  const now = new Date()
  const diffInHours = Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60))
  
  if (diffInHours < 24) {
    return `hace ${diffInHours}h`
  }
  
  const diffInDays = Math.floor(diffInHours / 24)
  if (diffInDays < 7) {
    return `hace ${diffInDays}d`
  }
  
  return formatDate(dateString)
}

// Lifecycle
onMounted(() => {
  loadFavorites()
})
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
