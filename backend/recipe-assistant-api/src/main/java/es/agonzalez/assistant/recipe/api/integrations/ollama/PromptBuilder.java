package es.agonzalez.assistant.recipe.api.integrations.ollama;

import java.util.Set;
import java.util.StringJoiner;

import es.agonzalez.assistant.recipe.api.dtos.SuggestRequest;

public class PromptBuilder {
    public static String build(SuggestRequest req) {
        StringJoiner ing = new StringJoiner(", ");
        req.getIngredients().forEach(s -> ing.add('"' + s.replace("\"", "'") + '"'));

        String diet = req.getPreferences().getDiet().name();
        String ex = toCsv(req.getPreferences().getExludeIngredients());
        String al = toCsv(req.getPreferences().getAllergens());

        return """
                Eres un asistente culinario experto.
                Genera UNA SOLA receta adecuda a los ingredientes disponibles y preferencias.
                RESPONDE EXCLUSIVAMENTE con un JSON válido, sin texto adicional.

                Esquema JSON de salida:
                {
                    "title": string,
                    "summary": string,
                    "steps": string[], // cada paso una breve explicacion
                    "tags": string[] // 3-6 etiquetas
                }

                Requisitos:
                - Ingredientes disponibles: [%s]
                - Dieta: %s
                - Excluir ingredientes: [%s]
                - Alergias (no incluir): [%s]
                - Prioriza sencillez (<= 6 pasos) y tiempos razonables.
                - Si faltan ingredientes críticos, propón sustitutos comunes.

                Devuelve solo el JSON indicado.
                """.formatted(ing.toString(), diet, ex, al);

    }

    private static String toCsv(Set<String> items) {
        if(items == null) return "";
        StringJoiner sj = new StringJoiner(", ");
        for(String s: items) {
            if(s != null && !s.isBlank())
            {
                sj.add('"' + s.replace("\"", "'") + '"');
            }
        }
        return sj.toString();
    }

    
}
