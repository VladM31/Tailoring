package sigma.nure.tailoring.tailoring.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ArraySizeValidation.class)
@Repeatable(ArraySizeList.class)
public @interface ArraySize {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    boolean nullable() default true;

    String message() default "Invalid Array Size";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}