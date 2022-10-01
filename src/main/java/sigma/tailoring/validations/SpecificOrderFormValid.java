package sigma.tailoring.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = SpecificOrderFormValidation.class)
public @interface SpecificOrderFormValid {
    Class<?>[] checkGroups();

    String message() default "Invalid Specific Order";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
