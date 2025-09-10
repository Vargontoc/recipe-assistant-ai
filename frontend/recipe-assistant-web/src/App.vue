<template>
  <div class="min-h-screen bg-gray-50 text-gray-800">
    <header class="bg-white/95 backdrop-blur-sm border-b sticky top-0 z-50 shadow-sm">
      <nav class="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        <!-- Mobile Menu Button -->
        <button 
          @click="isMobileMenuOpen = !isMobileMenuOpen"
          class="md:hidden p-2 text-gray-600 hover:text-indigo-600 transition-colors"
        >
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
      </nav>

      <!-- Mobile Navigation -->
      <div 
        v-show="isMobileMenuOpen"
        class="md:hidden bg-white border-t"
      >
        <div class="px-6 py-4 space-y-3">
          <RouterLink 
            to="/" 
            @click="isMobileMenuOpen = false"
            class="block text-gray-600 hover:text-indigo-600 transition-colors py-2"
            active-class="text-indigo-600 font-semibold"
          >
            🏠 Inicio
          </RouterLink>
          <RouterLink 
            to="/suggest" 
            @click="isMobileMenuOpen = false"
            class="block bg-gradient-to-r from-green-500 to-emerald-500 text-white px-4 py-3 rounded-lg font-medium"
            active-class="from-green-600 to-emerald-600"
          >
            ✨ IA Sugerir
          </RouterLink>
          <RouterLink 
            to="/search" 
            @click="isMobileMenuOpen = false"
            class="block text-gray-600 hover:text-indigo-600 transition-colors py-2"
            active-class="text-indigo-600 font-semibold"
          >
            🔍 Buscar
          </RouterLink>
          <RouterLink 
            to="/favorites" 
            @click="isMobileMenuOpen = false"
            class="block text-gray-600 hover:text-indigo-600 transition-colors py-2"
            active-class="text-indigo-600 font-semibold"
          >
            💝 Favoritos
          </RouterLink>
          <RouterLink 
            to="/history" 
            @click="isMobileMenuOpen = false"
            class="block text-gray-600 hover:text-indigo-600 transition-colors py-2"
            active-class="text-indigo-600 font-semibold"
          >
            📚 Historial
          </RouterLink>
        </div>
      </div>
    </header>
    
    <main class="max-w-7xl mx-auto">
      <RouterView />
    </main>
    
    <!-- Footer -->
    <footer class="bg-white border-t mt-20">
      <div class="max-w-7xl mx-auto px-6 py-8">
        <div class="text-center text-gray-600">
          <p class="mb-2">
            Hecho con ❤️ y mucha 🤖 IA para los amantes de la cocina
          </p>
          <p class="text-sm">
            © 2025 Recipe Assistant AI - Transformando ingredientes en experiencias
          </p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const isMobileMenuOpen = ref(false)
</script>
