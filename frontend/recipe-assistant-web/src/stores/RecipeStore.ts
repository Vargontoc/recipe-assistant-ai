import { defineStore } from "pinia";
import type Recipe from "../models/Recipe";
import { http } from "../services";

export const useRecipesStore = defineStore('recipes', {
    state: () => ({
        items: [] as Recipe[],
        total: 0,
        loading: false,
        q: '',
        page: 0,
        size: 10
    }),
    actions: {
        async fetch() {
            this.loading = true
            try {
                const { data } = await http.get('/api/recipes', { params: { q: this.q, page: this.page, size: this.size}});
                this.items = data.content
                this.total = data.totalElements
            }finally { this.loading = false }
        }
    }
})