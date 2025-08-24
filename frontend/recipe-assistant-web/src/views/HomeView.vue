<template>
  <section class="p-6 space-y-4">
    <div class="flex flex-col sm:flex-row sm:items-center gap-3">
      <h1 class="text-2x1 font-bold flex-1">Recetas</h1>
      <div class="flex items-center gap-2 w-full sm:w-80">
        <input v-model="search" @keyup.enter="applySearch" type="text" placeholder="Buscar por titulo o tag..." class="w-full border rounded-lg px-3 py2" />
        <button class="px-3 py-2 border rounded-lg" @click="applySearch">Buscar</button>
      </div>
    </div>

    <div v-if="loading" class="text-sm text-gray-500">Cargando recetas...</div>
    <div v-else>
      <div v-if="items.length === 0" class="text-sm text-gray-500">No hay recetas.</div>
      <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <recipe-card v-for="r  in items" :key="r.id" :recipe="r"></recipe-card>
      </div>
      <div class="mt-6" v-if="totalPages > 1">
        <pagination-component :page="page" :total-pages="totalPages" @change="goTo"></pagination-component>
      </div>
    </div>
  </section>
</template>
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';
import { useRecipesStore } from '../stores/RecipeStore';
import { storeToRefs } from 'pinia';
import RecipeCard from '../components/RecipeCard.vue';
import PaginationComponent from '../components/PaginationComponent.vue';
const store = useRecipesStore();
const search = ref(store.q);

const applySearch = () => store.setQuery(search.value || '')
const goTo = (p:number) => store.goTo(p)

onMounted(() => {
  if(!store.items.length) store.fetch()
})

watch(() => store.size, () => store.goTo(0))
const { items, loading, totalPages, page} = storeToRefs(store)
</script>