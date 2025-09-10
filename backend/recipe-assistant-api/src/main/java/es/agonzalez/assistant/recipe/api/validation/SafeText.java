package es.agonzalez.assistant.recipe.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation to check for safe text content
 * Prevents XSS and injection attacks
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SafeTextValidator.class)
public @interface SafeText {
    String message() default "Text contains potentially unsafe content";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Maximum allowed length for the text
     */
    int maxLength() default 1000;
    
    /**
     * Whether to allow HTML tags
     */
    boolean allowHtml() default false;
}
