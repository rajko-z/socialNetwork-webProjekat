package validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeRestrictionValidator.class)
@Documented
public @interface AgeRestriction {
    String message() default "Users should be at least 15 Y.O to register.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
