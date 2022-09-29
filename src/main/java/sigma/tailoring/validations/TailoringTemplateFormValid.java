package sigma.tailoring.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE, ElementType.TYPE_PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = TailoringTemplateFormValidation.class)
@Repeatable(value = TailoringTemplateFormValidList.class)
public @interface TailoringTemplateFormValid {
    String pageName();

    String formName();

    String errorName();

    Class<?>[] checkGroups();

    String message() default "Invalid Tailoring Template";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


