<template>
<article class="rounded-2x1 bg-white border p-4 shadow-sm hover:shadow transition">
    <header class="mb-2">
        <h3 class="font-semibold text-lg line-clamp-1">{{ recipe.title }}</h3>
        <p v-if="recipe.summary" class="text-sm text-gray-500 line-clamp-2">{{ recipe.summary }}</p>
    </header>
    <ul v-if="recipe.tags?.length" class="flex flex-wrap gap-2 mt-2">
        <li v-for="t in recipe.tags" :key="t" class="text-xs bg-gray-100 px-2 py-1 rounded-full">{{ t }}</li>
    </ul>
    <footer class="mt-4">
        <RouterLink :to="`/recipe/${recipe.id}`" class="text-sm underline">Ver detalle</RouterLink>
    </footer>
</article>
</template>
<script lang="ts" setup>
defineProps<{
    recipe: { id:string; title: string; summary?:string; tags?:string[]}
}>()
</script>