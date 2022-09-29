package sigma.tailoring.validations;

import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RUNTIME)
public @interface RegistrationUserFormValid {
    String pageName();

    String formAttribute();

    String errorAttribute();

    Class<?>[] checkGroups();

    String message() default "Invalid Registration User";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
