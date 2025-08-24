package es.agonzalez.assistant.recipe.api.utils;

import java.util.Optional;

public class JsonExtractor {
      public static Optional<String> firstJsonObject(String text) {
    if (text == null) return Optional.empty();
    // Si viene dentro de bloque markdown ```json ... ```
    int mdStart = text.indexOf("```");
    if (mdStart >= 0) {
      int mdEnd = text.indexOf("```", mdStart + 3);
      if (mdEnd > mdStart) {
        String inside = text.substring(mdStart + 3, mdEnd);
        int brace = inside.indexOf('{');
        if (brace >= 0) return balanceFrom(inside, brace);
      }
    }
    // Búsqueda directa del primer '{'
    int i = text.indexOf('{');
    if (i >= 0) return balanceFrom(text, i);
    return Optional.empty();
  }

  private static Optional<String> balanceFrom(String s, int startIdx) {
    int depth = 0;
    boolean inString = false;
    boolean escape = false;
    for (int i = startIdx; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (inString) {
        if (escape) { escape = false; continue; }
        if (ch == '\\') { escape = true; continue; }
        if (ch == '"') inString = false;
      } else {
          switch (ch) {
              case '"' -> inString = true;
              case '{' -> depth++;
              case '}' -> {
                  depth--;
                  if (depth == 0) {
                      return Optional.of(s.substring(startIdx, i + 1).trim());
                  }
              }
              default -> {
              }
          }
      }
    }
    return Optional.empty();
  }
}
