package sigma.tailoring.validations;


import com.google.gson.Gson;
import org.thymeleaf.util.StringUtils;
import sigma.tailoring.entities.*;
import sigma.tailoring.exceptions.ReadyDesignOrderFormException;
import sigma.tailoring.tools.ReadyDesignOrderForm;
import sigma.tailoring.service.TailoringTemplateService;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.TailoringTemplateSearchCriteria;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReadyDesignOrderFormValidation implements ConstraintValidator<ReadyDesignOrderFormValid, ReadyDesignOrderForm> {
    private static final String NOT_FOUND = "NOT FOUND";
    private static final Boolean EMPTY_PART_SIZES = Boolean.TRUE;
    private final TailoringTemplateService templateService;
    private final Validator validator;
    private final Gson gson;

    private String pageName;
    private String formAttribute;
    private String templateAttribute;
    private String partSizeAttribute;
    private String errorAttribute;
    private Class<?>[] checkGroups;
    private String message;

    public ReadyDesignOrderFormValidation(TailoringTemplateService templateService) {
        this.templateService = templateService;
        this.gson = new Gson();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public void initialize(ReadyDesignOrderFormValid constraintAnnotation) {
        this.pageName = constraintAnnotation.pageName();
        this.formAttribute = constraintAnnotation.formAttribute();
        this.templateAttribute = constraintAnnotation.templateAttribute();
        this.partSizeAttribute = constraintAnnotation.partSizeAttribute();
        this.errorAttribute = constraintAnnotation.errorAttribute();
        this.checkGroups = constraintAnnotation.checkGroups();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(ReadyDesignOrderForm form, ConstraintValidatorContext context) {
        String validErrors = this.validator.validate(form, this.checkGroups)
                .stream()
                .map(error -> error.getMessage())
                .collect(Collectors.joining("\n"));

        var template = this.findTemplateById(form.getTemplateId());

        String compareError = findCompareErrors(template, form);

        if (StringUtils.isEmptyOrWhitespace(validErrors) && StringUtils.isEmptyOrWhitespace(compareError)) {
            return true;
        }
        throw new ReadyDesignOrderFormException("Valid order based on template form has error", this.pageName,
                Map.of(
                        this.formAttribute, form,
                        this.templateAttribute, template,
                        this.partSizeAttribute, gson.toJson(template.getPartSizeForTemplates()),
                        this.errorAttribute, String.join("\n", this.message, validErrors, compareError)
                ));

    }

    private TailoringTemplate findTemplateById(Long templateId) {
        return this.templateService.findBy(
                TailoringTemplateSearchCriteria.builder()
                        .templateIds(List.of(templateId))
                        .isActive(true)
                        .build(),
                new Page()
        ).stream().findFirst().orElse(null);
    }

    private String findCompareErrors(TailoringTemplate template, ReadyDesignOrderForm form) {
        List<String> errors = new ArrayList<>();

        if (hasNotColor(template.getColors(), form.getColorId())) {
            errors.add("Template don't contains selected color");
        }

        if (hasNotMaterial(template.getMaterials(), form.getMaterialId())) {
            errors.add("Template don't contains selected material");
        }

        String partSizeError = findPartSizeError(template.getPartSizeForTemplates(), form.getPartSizeJson());

        if (!partSizeError.equals(NOT_FOUND)) {
            errors.add(partSizeError);
        }

        return errors.stream().collect(Collectors.joining("\n"));
    }

    private boolean hasNotColor(Set<Color> colors, Integer colorId) {
        return !colors.stream().map(c -> c.getId()).anyMatch(id -> id.equals(colorId));
    }

    private boolean hasNotMaterial(Set<Material> materials, Integer materialId) {
        return !materials.stream().map(m -> m.getId()).anyMatch(id -> id.equals(materialId));
    }

    private String findPartSizeError(Set<PartSizeForTemplate> partSizeForTemplates, String partSizeOrderJson) {
        List<PartSizeOrder> partSizeSortOrders = Arrays.stream(this.gson.fromJson(partSizeOrderJson, PartSizeOrder[].class))
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .collect(Collectors.toList());

        if (partSizeForTemplates.size() != partSizeSortOrders.size()) {
            return "Different size between part size order and part size for template";
        }

        List<PartSizeForTemplate> partSizeSortTemplates = partSizeForTemplates
                .stream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
                .collect(Collectors.toList());

        for (int i = 0, size = partSizeSortOrders.size(); i < size; i++) {
            if (!partSizeSortTemplates.get(i).getName().equals(partSizeSortOrders.get(i).getName())) {
                return "Different name between part size order and part size for template";
            }

            if (!hasSizeIfNotEmpty(
                    partSizeSortTemplates.get(i).getWidth(),
                    partSizeSortOrders.get(i).getWidth())) {
                return "Different width between part size order and part size for template";
            }

            if (!hasSizeIfNotEmpty(
                    partSizeSortTemplates.get(i).getHeight(),
                    partSizeSortOrders.get(i).getHeight())) {
                return "Different height between part size order and part size for template";
            }

            if (!hasSizeIfNotEmpty(
                    partSizeSortTemplates.get(i).getLength(),
                    partSizeSortOrders.get(i).getLength())) {
                return "Different length between part size order and part size for template";
            }

            if (!hasSizeIfNotEmpty(
                    partSizeSortTemplates.get(i).getVolume(),
                    partSizeSortOrders.get(i).getVolume())) {
                return "Different volume between part size order and part size for template";
            }
        }

        return NOT_FOUND;
    }

    private boolean hasSizeIfNotEmpty(Set<Integer> sizes, Integer size) {
        return sizes.isEmpty() ? EMPTY_PART_SIZES : sizes.contains(size);
    }
}
