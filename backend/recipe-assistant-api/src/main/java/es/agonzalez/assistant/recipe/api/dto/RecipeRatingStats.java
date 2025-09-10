package es.agonzalez.assistant.recipe.api.dto;

/**
 * DTO for recipe rating statistics
 */
public record RecipeRatingStats(
        Double averageRating,
        Long totalRatings,
        Long fiveStarCount,
        Long fourStarCount,
        Long threeStarCount,
        Long twoStarCount,
        Long oneStarCount
) {
    
    public static RecipeRatingStats empty() {
        return new RecipeRatingStats(0.0, 0L, 0L, 0L, 0L, 0L, 0L);
    }
    
    /**
     * Get rating percentage for a specific star count
     */
    public double getPercentage(int stars) {
        if (totalRatings == 0) return 0.0;
        
        long count = switch (stars) {
            case 5 -> fiveStarCount;
            case 4 -> fourStarCount;
            case 3 -> threeStarCount;
            case 2 -> twoStarCount;
            case 1 -> oneStarCount;
            default -> 0L;
        };
        
        return (double) count / totalRatings * 100.0;
    }
}
