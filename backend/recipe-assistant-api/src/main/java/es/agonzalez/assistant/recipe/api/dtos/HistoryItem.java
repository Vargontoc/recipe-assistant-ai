package es.agonzalez.assistant.recipe.api.dtos;

import java.time.Instant;
import java.util.UUID;

public class HistoryItem {
    private UUID id;
    private String modelUsed;
    private String responseTitle;
    private Long duration;
    private  Instant createdAt;
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getModelUsed() {
        return modelUsed;
    }
    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }
    public String getResponseTitle() {
        return responseTitle;
    }
    public void setResponseTitle(String responseTitle) {
        this.responseTitle = responseTitle;
    }
    public Long getDuration() {
        return duration;
    }
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    
}
