package es.agonzalez.assistant.recipe.api.integrations.ollama;

public interface OllamaClient {
    String generate(String prompt);
}
