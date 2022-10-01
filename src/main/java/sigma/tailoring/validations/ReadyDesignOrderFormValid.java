package sigma.tailoring.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ReadyDesignOrderFormValidation.class)
public @interface ReadyDesignOrderFormValid {
    String pageName();

    String formAttribute();

    String templateAttribute();

    String partSizeAttribute();

    String errorAttribute();

    Class<?>[] checkGroups();

    String message() default "Invalid Order Based on Template";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
