<template>
  <div class="user-stats-widget bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-semibold text-gray-900 mb-4">Tu Actividad</h3>
    
    <!-- Loading State -->
    <LoadingState v-if="isLoading" message="Cargando estadísticas..." />
    
    <!-- Error State -->
    <div v-else-if="error" class="text-center py-4">
      <div class="text-red-500 text-sm">{{ error }}</div>
      <button
        @click="loadStats"
        class="mt-2 text-blue-600 hover:text-blue-700 text-sm font-medium"
      >
        Reintentar
      </button>
    </div>
    
    <!-- Stats Grid -->
    <div v-else class="grid grid-cols-2 gap-4">
      <!-- Favorites Count -->
      <div class="stat-card text-center p-4 bg-red-50 rounded-lg">
        <div class="text-2xl font-bold text-red-600">{{ stats.favoritesCount || 0 }}</div>
        <div class="text-sm text-gray-600">Favoritos</div>
        <router-link
          to="/favorites"
          class="text-xs text-red-500 hover:text-red-600 font-medium mt-1 block"
        >
          Ver todos
        </router-link>
      </div>
      
      <!-- Ratings Count -->
      <div class="stat-card text-center p-4 bg-yellow-50 rounded-lg">
        <div class="text-2xl font-bold text-yellow-600">{{ stats.ratingsCount || 0 }}</div>
        <div class="text-sm text-gray-600">Valoraciones</div>
        <div class="text-xs text-gray-500 mt-1">dadas</div>
      </div>
      
      <!-- Search History Count -->
      <div class="stat-card text-center p-4 bg-blue-50 rounded-lg">
        <div class="text-2xl font-bold text-blue-600">{{ stats.searchesCount || 0 }}</div>
        <div class="text-sm text-gray-600">Búsquedas</div>
        <router-link
          to="/history"
          class="text-xs text-blue-500 hover:text-blue-600 font-medium mt-1 block"
        >
          Ver historial
        </router-link>
      </div>
      
      <!-- Average Rating Given -->
      <div class="stat-card text-center p-4 bg-green-50 rounded-lg">
        <div class="text-2xl font-bold text-green-600">
          {{ stats.averageRatingGiven ? stats.averageRatingGiven.toFixed(1) : '0.0' }}
        </div>
        <div class="text-sm text-gray-600">Promedio</div>
        <div class="text-xs text-gray-500 mt-1">valoración</div>
      </div>
    </div>
    
    <!-- Recent Activity -->
    <div v-if="recentActivity.length" class="mt-6">
      <h4 class="text-sm font-semibold text-gray-700 mb-3">Actividad Reciente</h4>
      <div class="space-y-2">
        <div
          v-for="activity in recentActivity.slice(0, 3)"
          :key="activity.id"
          class="flex items-center gap-3 text-sm p-2 bg-gray-50 rounded-lg"
        >
          <span class="text-lg">{{ getActivityIcon(activity.type) }}</span>
          <div class="flex-1">
            <div class="text-gray-900">{{ activity.description }}</div>
            <div class="text-gray-500 text-xs">{{ formatDate(activity.date) }}</div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Quick Actions -->
    <div class="mt-6 pt-4 border-t border-gray-200">
      <div class="grid grid-cols-2 gap-2">
        <router-link
          to="/search"
          class="text-center py-2 px-3 bg-blue-100 text-blue-700 rounded-lg text-sm font-medium hover:bg-blue-200 transition-colors"
        >
          🔍 Buscar
        </router-link>
        <router-link
          to="/suggest"
          class="text-center py-2 px-3 bg-green-100 text-green-700 rounded-lg text-sm font-medium hover:bg-green-200 transition-colors"
        >
          ✨ Sugerir
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { favoritesService, searchHistoryService } from '../services';
import LoadingState from './LoadingState.vue';

// Types
interface Activity {
  id: string;
  type: string;
  description: string;
  date: string;
}

// Reactive state
const stats = ref({
  favoritesCount: 0,
  ratingsCount: 0,
  searchesCount: 0,
  averageRatingGiven: 0
});

const recentActivity = ref<Activity[]>([]);
const isLoading = ref(true);
const error = ref<string | null>(null);

// Methods
const loadStats = async () => {
  isLoading.value = true;
  error.value = null;
  
  try {
    const [favoritesResponse, searchHistoryResponse] = await Promise.all([
      favoritesService.getUserFavorites(),
      searchHistoryService.getUserSearchHistory()
    ]);
    
    stats.value.favoritesCount = favoritesResponse.data.length;
    stats.value.searchesCount = searchHistoryResponse.data.length;
    
    // Build recent activity from available data
    const activities: Activity[] = [];
    
    // Add recent favorites
    favoritesResponse.data.slice(0, 2).forEach((fav: any) => {
      activities.push({
        id: `fav-${fav.id}`,
        type: 'favorite',
        description: `Agregaste "${fav.recipe.title}" a favoritos`,
        date: fav.createdAt
      });
    });
    
    // Add recent searches
    searchHistoryResponse.data.slice(0, 2).forEach((search: any) => {
      activities.push({
        id: `search-${search.id}`,
        type: 'search',
        description: `Buscaste "${search.query}"`,
        date: search.createdAt
      });
    });
    
    // Sort by date and take recent ones
    recentActivity.value = activities
      .sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
      .slice(0, 5);
      
  } catch (err: any) {
    console.error('Error loading user stats:', err);
    error.value = 'Error al cargar las estadísticas';
  } finally {
    isLoading.value = false;
  }
};

const getActivityIcon = (type: string) => {
  switch (type) {
    case 'favorite': return '💝';
    case 'rating': return '⭐';
    case 'search': return '🔍';
    default: return '📱';
  }
};

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  const now = new Date();
  const diffMs = now.getTime() - date.getTime();
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
  const diffDays = Math.floor(diffHours / 24);
  
  if (diffHours < 1) return 'Hace un momento';
  if (diffHours < 24) return `Hace ${diffHours}h`;
  if (diffDays < 7) return `Hace ${diffDays}d`;
  
  return date.toLocaleDateString('es-ES', {
    month: 'short',
    day: 'numeric'
  });
};

// Load stats on mount
onMounted(() => {
  loadStats();
});
</script>

<style scoped>
.stat-card {
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}
</style>
