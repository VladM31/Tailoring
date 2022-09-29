package sigma.tailoring.validations;

import sigma.tailoring.exceptions.FormException;
import sigma.tailoring.service.UserService;
import sigma.tailoring.tools.RegistrationUserForm;

import javax.validation.*;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationUserFormValidation implements ConstraintValidator<RegistrationUserFormValid, RegistrationUserForm> {
    private final UserService userService;
    private Class<?>[] checkGroups;
    private String errorAttribute;
    private String pageName;
    private String formAttribute;
    private String message;
    private Validator validator;

    public RegistrationUserFormValidation(UserService userService) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.userService = userService;
    }

    @Override
    public void initialize(RegistrationUserFormValid annotation) {
        this.checkGroups = annotation.checkGroups();
        this.errorAttribute = annotation.errorAttribute();
        this.formAttribute = annotation.formAttribute();
        this.pageName = annotation.pageName();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(RegistrationUserForm form, ConstraintValidatorContext constraintValidatorContext) {
        List<String> errors = this.validator.validate(form, this.checkGroups)
                .stream()
                .map(error -> error.getMessage())
                .collect(Collectors.toList());

        if (userService.isBooked(form.getEmail(), form.getPhoneNumber())) {
            errors.add("Email or phone number is booked");
        }

        String errorMessage = errors.stream().collect(Collectors.joining("\n"));

        if (errorMessage.isBlank()) {
            return true;
        }

        throw new FormException(String.join("\n", this.message, errorMessage),
                form, this.formAttribute, this.pageName, this.errorAttribute);
    }
}
