import { defineStore } from "pinia";
import type Preferences from "../models/Preferences";
import { http } from "../services";

export const usePreferencesStore = defineStore('preferences', {
    state: () => ({
        loading: false,
        preferences: null as Preferences | null,
    }),
    actions: {
        async load() {
            this.loading = true
            try{ const {data } = await http.get('/api/preferences'); this.preferences = data }
            finally { this.loading = false }
        },
        async save(p: Preferences) {
            const { data } = await http.put('/api/preferences', p)
            this.preferences = data
        }
    }
})