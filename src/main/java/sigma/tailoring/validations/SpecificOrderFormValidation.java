package sigma.tailoring.validations;

import com.google.gson.Gson;
import org.thymeleaf.util.StringUtils;
import sigma.tailoring.entities.PartSizeOrder;
import sigma.tailoring.exceptions.OrderException;
import sigma.tailoring.tools.SpecificOrderForm;

import javax.validation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecificOrderFormValidation implements ConstraintValidator<SpecificOrderFormValid, SpecificOrderForm> {
    private static final String EMPTY_ERROR = "";
    private final Validator validator;
    private final Gson gson;
    private Class<?>[] checkGroups;
    private String message;

    public SpecificOrderFormValidation() {
        this.gson = new Gson();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public void initialize(SpecificOrderFormValid constraintAnnotation) {
        this.checkGroups = constraintAnnotation.checkGroups();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(SpecificOrderForm form, ConstraintValidatorContext constraintValidatorContext) {
        String validErrors = this.validator.validate(form, this.checkGroups)
                .stream()
                .map(error -> error.getMessage())
                .collect(Collectors.joining("\n"));

        String partSizeError = findPartSizeError(form.getPartSizeJson());

        if (StringUtils.isEmptyOrWhitespace(validErrors) && StringUtils.isEmptyOrWhitespace(partSizeError)) {
            return true;
        }

        throw new OrderException(String.join("\n", this.message, validErrors, partSizeError));
    }

    private String findPartSizeError(String partSizeJson) {
        List<PartSizeOrder> partSizeOrders = Stream.of(gson.fromJson(partSizeJson, PartSizeOrder[].class))
                .collect(Collectors.toList());

        if (partSizeOrders.isEmpty()) {
            return "Part sizes must be between 1 and 10";
        }

        for (PartSizeOrder partSizeOrder : partSizeOrders) {
            if (StringUtils.isEmptyOrWhitespace(partSizeOrder.getName())) {
                return "Part size name mustn't be blank";
            }

            if (this.lessThanOne(partSizeOrder.getWidth())) {
                return "Width less than 1";
            }

            if (this.lessThanOne(partSizeOrder.getLength())) {
                return "Length less than 1";
            }

            if (this.lessThanOne(partSizeOrder.getVolume())) {
                return "Volume less than 1";
            }

            if (this.lessThanOne(partSizeOrder.getHeight())) {
                return "Height less than 1";
            }
        }

        return EMPTY_ERROR;
    }

    private boolean lessThanOne(Integer number) {
        if (number == null) {
            return false;
        }
        return number < 1;
    }
}
