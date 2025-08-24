<template>
    <div class="flex items-center justify-between gap-2">
        <button class="px-3 py-1 border rounded disabled:opacity-50" :disabled="page <= 0" @click="$emit('change', page - 1)"><< Anterior</button>
        <span class="text-sm">Página {{ page + 1 }} / {{ totalPages }}</span>
        <button class="px-3 py-1 border rounded disabled:opacity-50" :disabled="page >= totalPages-1" @click="$emit('change', page+1)">Siguiente >> </button>
    </div>
</template>

<script setup lang="ts">
defineProps<{ page: number; totalPages: number}>()
defineEmits<{ (e: 'change', page: number):void}>()
</script>