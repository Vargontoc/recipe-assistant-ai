package es.agonzalez.assistant.recipe.api.integrations.ollama;

public record  GenerateRequest(String model, String prompt, Boolean stream, Double temperature) {}
