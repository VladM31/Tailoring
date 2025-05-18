package sigma.tailoring.validations;

import com.google.gson.Gson;
import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.exceptions.FormException;
import sigma.tailoring.tools.PartSizeTemplateForm;
import sigma.tailoring.tools.TailoringTemplateForm;

import javax.validation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TailoringTemplateFormValidation implements ConstraintValidator<TailoringTemplateFormValid, TailoringTemplateForm> {
    private Class<?>[] checkGroups;
    private String errorName;
    private String pageName;
    private String formName;
    private String message;
    private Validator validator;
    private Gson gson;

    public TailoringTemplateFormValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.gson = new Gson();
    }

    @Override
    public void initialize(TailoringTemplateFormValid annotation) {
        this.checkGroups = annotation.checkGroups();
        this.errorName = annotation.errorName();
        this.formName = annotation.formName();
        this.pageName = annotation.pageName();
        this.message = annotation.message();
    }

    @Override
    public boolean isValid(TailoringTemplateForm form, ConstraintValidatorContext context) {
        form.clearBlankUploadFiles();

        List<String> errorMessages = new ArrayList<>();

        errorMessages.addAll(findValidationErrors(form));

        errorMessages.addAll(findPartSizeErrors(form));

        if (hasNotFrontImage(form)) {
            errorMessages.add("Didn't choose front message");
        }

        String errorMessage = toMessage(errorMessages);

        if (errorMessage.isBlank()) {
            return true;
        }
        throw new FormException(String.join("\n", errorMessage, this.message), form, this.formName, this.pageName, this.errorName);

    }

    private boolean hasNotFrontImage(TailoringTemplateForm form) {
        return Stream.of(form.getUploadImages())
                .map(MultipartFile::getOriginalFilename).filter(Objects::nonNull).noneMatch(name -> name.contains("front-"))
                &&
                Stream.of(form.getImagesUrl())
                        .map(File::getName).noneMatch(name -> name.contains("front-"));
    }

    private List<String> findPartSizeErrors(TailoringTemplateForm form) {
        if (form.getPartSizeTemplateForm(this.gson).isEmpty()) {
            return List.of("Part sizes is Empty");
        }
        return form.getPartSizeTemplateForm(this.gson).
                stream()
                .map(PartSizeTemplateForm::getPartSizeError)
                .filter(error -> error.hasError())
                .map(error -> error.message())
                .collect(Collectors.toList());
    }

    private List<String> findValidationErrors(TailoringTemplateForm form) {
        return validator.validate(form, checkGroups)
                .stream()
                .map(error -> error.getMessage())
                .collect(Collectors.toList());
    }

    private String toMessage(List<String> errorMessages) {
        return errorMessages
                .stream()
                .collect(
                        Collectors
                                .joining("\n")
                );
    }


}
