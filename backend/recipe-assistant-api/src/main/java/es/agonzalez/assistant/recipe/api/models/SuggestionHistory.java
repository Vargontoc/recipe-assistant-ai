package es.agonzalez.assistant.recipe.api.models;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "suggestion_history", indexes= {
    @Index(name = "ix_sugg_hist_created_at", columnList="created_at DESC")
})
public class SuggestionHistory {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "model_used", length= 80, nullable=false)
    private String modelUsed;

    @Lob
    @Column(name = "input_ingredients", columnDefinition="TEXT", nullable=false)
    private String inputIngredients;

    @Lob
    @Column(name = "preferences_snapshot", columnDefinition= "TEXT")
    private String preferencesSnapshot;

    @Column(name = "response_title", length=200)
    private String responseTitle;

    @Column(name="response_body", columnDefinition="TEXT")
    private String response;

    @Column(name="duration_ms")
    private Long duration;

    @Column(name="created_at", nullable=false, updatable=false)
    private Instant createAt;


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

    public String getInputIngredients() {
        return inputIngredients;
    }

    public void setInputIngredients(String inputIngredients) {
        this.inputIngredients = inputIngredients;
    }

    public String getPreferencesSnapshot() {
        return preferencesSnapshot;
    }

    public void setPreferencesSnapshot(String preferencesSnapshot) {
        this.preferencesSnapshot = preferencesSnapshot;
    }

    public String getResponseTitle() {
        return responseTitle;
    }

    public void setResponseTitle(String responseTitle) {
        this.responseTitle = responseTitle;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    
    
    
}
