<template>
  <div class="min-h-screen bg-gray-50 text-gray-800">
    <header class="bg-white border-b sticky top-0 z-10 shadow-sm">
      <nav class="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
        <RouterLink to="/" class="font-bold text-xl text-blue-600 hover:text-blue-700">
          🍳 Recipe Assistant AI
        </RouterLink>
        <ul class="flex items-center gap-6 text-sm font-medium">
          <li>
            <RouterLink 
              to="/" 
              class="text-gray-600 hover:text-blue-600 transition-colors"
              active-class="text-blue-600 font-semibold"
            >
              Recetas
            </RouterLink>
          </li>
          <li>
            <RouterLink 
              to="/search" 
              class="text-gray-600 hover:text-blue-600 transition-colors"
              active-class="text-blue-600 font-semibold"
            >
              Buscar
            </RouterLink>
          </li>
          <li>
            <RouterLink 
              to="/suggest" 
              class="text-gray-600 hover:text-blue-600 transition-colors"
              active-class="text-blue-600 font-semibold"
            >
              Sugerir
            </RouterLink>
          </li>
          <li>
            <RouterLink 
              to="/favorites" 
              class="text-gray-600 hover:text-blue-600 transition-colors flex items-center gap-1"
              active-class="text-blue-600 font-semibold"
            >
              <span>💝</span>
              Favoritos
            </RouterLink>
          </li>
          <li>
            <RouterLink 
              to="/history" 
              class="text-gray-600 hover:text-blue-600 transition-colors"
              active-class="text-blue-600 font-semibold"
            >
              Historial
            </RouterLink>
          </li>
          <li>
            <RouterLink 
              to="/preferences" 
              class="text-gray-600 hover:text-blue-600 transition-colors"
              active-class="text-blue-600 font-semibold"
            >
              Preferencias
            </RouterLink>
          </li>
        </ul>
      </nav>
    </header>
    <main class="max-w-6xl mx-auto px-4">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
</script>
