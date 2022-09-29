package sigma.nure.tailoring.tailoring.tools;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import sigma.nure.tailoring.tailoring.entities.PartSizeForTemplate;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplate;
import sigma.nure.tailoring.tailoring.validations.ArraySize;
import sigma.nure.tailoring.tailoring.validations.TailoringTemplateFormValid;

import javax.validation.constraints.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TailoringTemplateFormValid(pageName = "CreateOrEditTemplate", formName = "templateForm", errorName = "errors",
        checkGroups = {OnSave.class, AllOperation.class}, groups = OnSaveForm.class,
        message = "Please choose again images,colors and materials")
@TailoringTemplateFormValid(pageName = "CreateOrEditTemplate", formName = "templateForm", errorName = "errors",
        checkGroups = {OnUpdate.class, AllOperation.class}, groups = OnUpdateForm.class,
        message = "Please choose again colors and materials")
public class TailoringTemplateForm {
    private static final Logger LOGGER = LoggerFactory.getLogger(TailoringTemplateForm.class);
    private static final int EMPTY_SIZE = 0;
    public static final String SUCCESSFUL = "";

    @Null(groups = OnSave.class)
    @NotNull(groups = OnUpdate.class)
    private Long id;
    @Size(min = 5, max = 60, message = "Name must be between 5 and 60", groups = AllOperation.class)
    @NotBlank(message = "Name mustn't be blank", groups = AllOperation.class)
    private String name;
    private boolean active;
    @Min(value = 1, message = "Cost must be greater than or equal to 1", groups = AllOperation.class)
    @NotNull(message = "Cost mustn't be null", groups = AllOperation.class)
    private Integer cost;
    @Size(min = 5, max = 60, message = "Type template must be between 5 and 60", groups = AllOperation.class)
    @NotBlank(message = "Type template mustn't be blank", groups = AllOperation.class)
    private String typeTemplate;
    @Size(min = 5, max = 2000, message = "Template description must be between 5 and 60", groups = AllOperation.class)
    @NotBlank(message = "Template description mustn't be blank", groups = AllOperation.class)
    private String templateDescription;
    @NotNull(message = "Materials mustn't be null")
    @Size(min = 1, message = "Materials must be great than 0", groups = AllOperation.class)
    @NotEmpty(message = "Materials mustn't be empty.")
    private Integer[] materialIds;
    @NotNull(message = "Colors mustn't be null")
    @Size(min = 1, message = "Colors must be great than 0", groups = AllOperation.class)
    @NotEmpty(message = "Colors mustn't be empty.")
    private Integer[] colorIds;
    @ArraySize(max = 10, message = "Images url must be less than 11", groups = OnSave.class)
    @ArraySize(min = 1, max = 10, nullable = false, message = "Images url must be between 1 and 10 and not null", groups = OnUpdate.class)
    private File[] imagesUrl;
    @ArraySize(max = 10, message = "Upload images must be less than 11", groups = OnUpdate.class)
    @ArraySize(min = 1, max = 10, nullable = false, message = "Upload images must be between 1 and 10 and not null", groups = OnSave.class)
    private MultipartFile[] uploadImages;
    @NotBlank(message = "Part size mustn't be blank", groups = AllOperation.class)
    private String partSizeJson;
    @NotNull(groups = OnUpdate.class, message = "Date of creation mustn't be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateOfCreation;

    public TailoringTemplateForm(TailoringTemplate template, Gson gson, String directory) {
        this.id = template.getId();
        this.name = template.getName();
        this.active = template.isActive();
        this.cost = template.getCost();
        this.typeTemplate = template.getTypeTemplate();
        this.templateDescription = template.getTemplateDescription();
        this.dateOfCreation = template.getDateOfCreation();
        this.materialIds = template.getMaterials().stream().map(m -> m.getId()).toArray(Integer[]::new);
        this.colorIds = template.getColors().stream().map(c -> c.getId()).toArray(Integer[]::new);
        this.imagesUrl = template.getImagesUrl().stream().map(url -> new File(directory, url)).toArray(File[]::new);
        this.setPartSizeJson(gson, template.getPartSizeForTemplates());
    }

    public Set<PartSizeTemplateForm> getPartSizeTemplateForm(Gson gson) {
        return Stream.of(
                        gson.fromJson(partSizeJson, PartSizeTemplateForm[].class))
                .collect(Collectors.toSet());
    }

    public Set<PartSizeForTemplate> toPartSizeForTemplates(Gson gson) {
        return Stream.of(gson.fromJson(partSizeJson, PartSizeTemplateForm[].class))
                .collect(Collectors.mapping(part -> part.toPartSizeForTemplate(),
                        Collectors.toSet()));
    }

    public void setPartSizeJson(Gson gson, Set<PartSizeForTemplate> partSizeForTemplates) {
        partSizeJson = gson.toJson(partSizeForTemplates);
    }

    public void clearBlankUploadFiles() {
        this.uploadImages = Arrays.stream(this.uploadImages)
                .filter(img -> !img.getOriginalFilename().isBlank())
                .toArray(MultipartFile[]::new);
    }
}
