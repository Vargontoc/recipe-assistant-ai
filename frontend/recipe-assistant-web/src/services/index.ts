import axios from "axios";

export const http = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
    timeout: 20000
})

// Add user identifier header interceptor
http.interceptors.request.use((config) => {
    const userIdentifier = localStorage.getItem('userIdentifier') || `user_${Date.now()}`;
    config.headers['X-User-Identifier'] = userIdentifier;
    // Store user identifier if it was generated
    localStorage.setItem('userIdentifier', userIdentifier);
    return config;
});

http.interceptors.response.use(
    (r) => r,
    (error) => {
        console.error('API Error', error?.response?.data || error.message)
        return Promise.reject(error);
    }
)

// Types for Rating System
export interface RecipeRating {
    id: string;
    recipeId: string;
    rating: number;
    comment?: string;
    userIdentifier: string;
    isAnonymous: boolean;
    createdAt: string;
    updatedAt: string;
}

export interface CreateRatingRequest {
    recipeId: string;
    rating: number;
    comment?: string;
}

export interface RatingStats {
    recipeId: string;
    averageRating: number;
    totalRatings: number;
    ratingDistribution: { [key: number]: number };
}

// Types for Advanced Search
export interface AdvancedSearchRequest {
    query?: string;
    includedIngredients?: string[];
    excludedIngredients?: string[];
    diets?: string[];
    allergens?: string[];
    minCookingTime?: number;
    maxCookingTime?: number;
    minServings?: number;
    maxServings?: number;
    minRating?: number;
    sortBy?: string;
    sortDirection?: string;
}

export interface SearchFilters {
    availableIngredients: string[];
    availableDiets: string[];
    availableAllergens: string[];
    minCookingTime: number;
    maxCookingTime: number;
    minServings: number;
    maxServings: number;
}

// Types for Favorites
export interface RecipeFavorite {
    id: string;
    recipe: any; // Recipe object
    userIdentifier: string;
    createdAt: string;
}

export interface CreateFavoriteRequest {
    recipeId: string;
}

// Types for Search History
export interface SearchHistoryItem {
    id: string;
    userIdentifier: string;
    searchQuery: string;
    searchType: string;
    resultsCount: number;
    createdAt: string;
}

export interface SearchStats {
    totalSearches: number;
    averageResults: number;
    lastSearchAt?: string;
}

// Rating API Service
export const ratingService = {
    // Create a rating
    async createRating(request: CreateRatingRequest): Promise<RecipeRating> {
        const response = await http.post(`/v1/recipes/${request.recipeId}/ratings`, request);
        return response.data;
    },

    // Update a rating
    async updateRating(ratingId: string, request: CreateRatingRequest): Promise<RecipeRating> {
        const response = await http.put(`/v1/recipes/${request.recipeId}/ratings/${ratingId}`, request);
        return response.data;
    },

    // Delete a rating
    async deleteRating(recipeId: string, ratingId: string): Promise<void> {
        await http.delete(`/v1/recipes/${recipeId}/ratings/${ratingId}`);
    },

    // Get ratings for a recipe
    async getRecipeRatings(recipeId: string, page = 0, size = 10) {
        const response = await http.get(`/v1/recipes/${recipeId}/ratings`, {
            params: { page, size }
        });
        return response.data;
    },

    // Get rating statistics
    async getRatingStats(recipeId: string): Promise<RatingStats> {
        const response = await http.get(`/v1/recipes/${recipeId}/ratings/stats`);
        return response.data;
    },

    // Get user's rating for a recipe
    async getUserRating(recipeId: string): Promise<RecipeRating | null> {
        try {
            const response = await http.get(`/v1/recipes/${recipeId}/ratings/user`);
            return response.data;
        } catch (error: any) {
            if (error.response?.status === 404) return null;
            throw error;
        }
    },

    // Check if user has rated recipe
    async hasUserRated(recipeId: string): Promise<boolean> {
        const response = await http.get(`/v1/recipes/${recipeId}/ratings/check`);
        return response.data;
    },

    // Get recent ratings with comments
    async getRecentRatings(page = 0, size = 10) {
        const response = await http.get('/v1/ratings/recent', {
            params: { page, size }
        });
        return response.data;
    }
};

// Advanced Search API Service
export const searchService = {
    // Perform advanced search
    async advancedSearch(request: AdvancedSearchRequest, page = 0, size = 10) {
        const response = await http.post('/recipes/search/advanced', request, {
            params: { page, size }
        });
        return response.data;
    },

    // Get search filter options
    async getSearchFilters(): Promise<SearchFilters> {
        const response = await http.get('/recipes/search/filters');
        return response.data;
    }
};

// Favorites API Service
export const favoritesService = {
    // Add recipe to favorites
    async addFavorite(request: CreateFavoriteRequest): Promise<RecipeFavorite> {
        const response = await http.post('/v1/favorites', request);
        return response.data;
    },

    // Remove recipe from favorites
    async removeFavorite(recipeId: string): Promise<void> {
        await http.delete(`/v1/favorites/recipes/${recipeId}`);
    },

    // Get user's favorites
    async getUserFavorites(page = 0, size = 10) {
        const response = await http.get('/v1/favorites', {
            params: { page, size }
        });
        return response.data;
    },

    // Check if recipe is favorited
    async isFavorited(recipeId: string): Promise<boolean> {
        const response = await http.get(`/v1/favorites/recipes/${recipeId}/check`);
        return response.data;
    },

    // Get favorite count for recipe
    async getFavoriteCount(recipeId: string): Promise<number> {
        const response = await http.get(`/v1/favorites/recipes/${recipeId}/count`);
        return response.data;
    },

    // Get user's favorite count
    async getUserFavoriteCount(): Promise<number> {
        const response = await http.get('/v1/favorites/count');
        return response.data;
    }
};

// Search History API Service
export const searchHistoryService = {
    // Get user's search history
    async getUserSearchHistory(page = 0, size = 20) {
        const response = await http.get('/v1/search-history', {
            params: { page, size }
        });
        return response.data;
    },

    // Get search suggestions
    async getSearchSuggestions(query?: string, limit = 10): Promise<string[]> {
        const response = await http.get('/v1/search-history/suggestions', {
            params: { query, limit }
        });
        return response.data;
    },

    // Get popular search queries
    async getPopularQueries(limit = 10): Promise<string[]> {
        const response = await http.get('/v1/search-history/popular', {
            params: { limit }
        });
        return response.data;
    },

    // Get user's search statistics
    async getUserSearchStats(): Promise<SearchStats> {
        const response = await http.get('/v1/search-history/stats');
        return response.data;
    }
};