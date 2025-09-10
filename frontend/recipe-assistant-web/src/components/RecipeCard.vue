<template>
<article class="rounded-2xl bg-white border border-gray-200 p-6 shadow-sm hover:shadow-md transition-all duration-300 hover:-translate-y-1">
    <header class="mb-4">
        <h3 class="font-semibold text-lg line-clamp-1 text-gray-900 mb-2">{{ recipe.title }}</h3>
        <p v-if="recipe.summary" class="text-sm text-gray-600 line-clamp-2 leading-relaxed">{{ recipe.summary }}</p>
    </header>
    
    <div v-if="recipe.tags?.length" class="mb-4">
        <ul class="flex flex-wrap gap-2">
            <li v-for="tag in recipe.tags" :key="tag" 
                class="text-xs bg-blue-50 text-blue-700 px-3 py-1 rounded-full border border-blue-200 hover:bg-blue-100 transition-colors">
                {{ tag }}
            </li>
        </ul>
    </div>

    <!-- Placeholder for recipe image -->
    <div class="mb-4 h-32 bg-gradient-to-br from-orange-100 to-red-100 rounded-lg flex items-center justify-center">
        <svg class="w-12 h-12 text-orange-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                  d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4"></path>
        </svg>
    </div>
    
    <footer class="flex justify-between items-center">
        <RouterLink :to="`/recipe/${recipe.id}`" 
                    class="text-sm text-blue-600 hover:text-blue-800 font-medium underline decoration-2 underline-offset-2 hover:decoration-blue-800 transition-colors">
            Ver detalle
        </RouterLink>
        <span class="text-xs text-gray-400">{{ formatDate(recipe.createdAt) }}</span>
    </footer>
</article>
</template>
<script lang="ts" setup>
interface Recipe {
    id: string;
    title: string;
    summary?: string;
    tags?: string[];
    createdAt?: string;
}

defineProps<{
    recipe: Recipe
}>()

// Utility function to format dates
const formatDate = (dateStr?: string) => {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleDateString('es-ES', {
        day: '2-digit',
        month: 'short'
    });
}
</script>