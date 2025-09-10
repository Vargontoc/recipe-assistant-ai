package es.agonzalez.assistant.recipe.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Validator implementation for SafeText annotation
 */
public class SafeTextValidator implements ConstraintValidator<SafeText, String> {

    private int maxLength;
    private boolean allowHtml;
    
    // Patterns to detect potentially dangerous content
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
            "<script[^>]*>.*?</script>", 
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );
    
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
            "('|(\\-\\-)|(;)|(\\||\\|)|(\\*|\\*))",
            Pattern.CASE_INSENSITIVE
    );
    
    private static final Pattern XSS_PATTERN = Pattern.compile(
            "(javascript:|vbscript:|onload=|onerror=|onclick=)",
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public void initialize(SafeText constraintAnnotation) {
        this.maxLength = constraintAnnotation.maxLength();
        this.allowHtml = constraintAnnotation.allowHtml();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Let @NotNull handle null values
        }

        // Check length
        if (value.length() > maxLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Text exceeds maximum length of " + maxLength + " characters"
            ).addConstraintViolation();
            return false;
        }

        // Check for script tags
        if (SCRIPT_PATTERN.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Script tags are not allowed"
            ).addConstraintViolation();
            return false;
        }

        // Check for XSS patterns
        if (XSS_PATTERN.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Potentially dangerous JavaScript detected"
            ).addConstraintViolation();
            return false;
        }

        // Check for SQL injection patterns
        if (SQL_INJECTION_PATTERN.matcher(value).find()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Text contains potentially unsafe SQL patterns"
            ).addConstraintViolation();
            return false;
        }

        // Check HTML tags if not allowed
        if (!allowHtml && value.contains("<") && value.contains(">")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "HTML tags are not allowed"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
