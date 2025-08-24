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
    getters: {
        totalPages: (s) => Math.ceil(s.total / s.size) || 1
    },
    actions: {
        async fetch() {
            this.loading = true
            try {
                const { data } = await http.get('/api/recipes', { params: { q: this.q, page: this.page, size: this.size}});
                this.items = data.content
                this.total = data.totalElements
            }finally { this.loading = false }
        },
        setQuery(q: string) {
            this.q = q
            this.page = 0
            return this.fetch()
        },
        goTo(p: number) {
            this.page = Math.max(0, Math.min(p, this.totalPages - 1))
        }
    }
})