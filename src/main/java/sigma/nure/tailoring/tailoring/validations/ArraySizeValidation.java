package sigma.nure.tailoring.tailoring.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArraySizeValidation implements ConstraintValidator<ArraySize, Object[]> {
    private int min;
    private int max;
    private boolean nullable;
    private String message;

    @Override
    public void initialize(ArraySize constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        nullable = constraintAnnotation.nullable();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {


        if (value == null) {
            if (nullable) {
                return true;
            }
            context.buildConstraintViolationWithTemplate(message);
            return false;
        }
        if (value.length >= min && value.length <= max) {
            return true;
        }
        context.buildConstraintViolationWithTemplate(message);
        return false;
    }
}
