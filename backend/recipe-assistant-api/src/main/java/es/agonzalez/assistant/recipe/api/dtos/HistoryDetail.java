package es.agonzalez.assistant.recipe.api.dtos;

import java.time.Instant;
import java.util.UUID;

public class HistoryDetail {
    private UUID id;
    private String modelUser;
    private String ingredients;
    private String preferences;
    private String response;
    private Long duration;
    private Instant createAt;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getModelUser() {
        return modelUser;
    }
    public void setModelUser(String modelUser) {
        this.modelUser = modelUser;
    }
    public String getIngredients() {
        return ingredients;
    }
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
    public String getPreferences() {
        return preferences;
    }
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    public Long getDuration() {
        return duration;
    }
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public Instant getCreateAt() {
        return createAt;
    }
    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    
}
