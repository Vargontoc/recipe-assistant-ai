<template>
  <div class="min-h-screen bg-gray-50 text-gray-800">
    <header class="bg-white border-b sticky top-0 z-10">
      <nav class="max-w-5xl mx-auto px-4 h-14 flex items-center justify-between">
        <RouterLink to="/" class="font-semibold">Personal Recipe AI</RouterLink>
        <ul class="flex items-center gap-4 text-sm">
          <li><RouterLink to="/" class="hover:underline">Recetas</RouterLink></li>
          <li><RouterLink to="/suggest" class="hover:underline">Sugerir</RouterLink></li>
          <li><RouterLink to="/history" class="hover:underline">Historial</RouterLink></li>
          <li><RouterLink to="/preferences" class="hover:underline">Preferencias</RouterLink></li>
        </ul>
      </nav>
    </header>
    <main class="max-w-5xl mx-auto px-4">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
</script>
