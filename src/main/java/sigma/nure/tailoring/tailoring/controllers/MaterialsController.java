package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;
import sigma.nure.tailoring.tailoring.service.MaterialsService;
import sigma.nure.tailoring.tailoring.tools.Answer;
import sigma.nure.tailoring.tailoring.tools.ColorForm;
import sigma.nure.tailoring.tailoring.tools.MaterialForm;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@PropertySource("classpath:properties/rest-resources.properties")
@RequestMapping("${material.url.resources}")
public class MaterialsController {

    private static final int FIRST_ERROR = 0;
    private static final String EMPTY_DEFAULT_MESSAGE = "";
    private final String errorMessage;
    private final MaterialsService materialsService;

    public MaterialsController(MaterialsService materialsService, @Value("${error.message}") String errorMessage) {
        this.errorMessage = errorMessage;
        this.materialsService = materialsService;
    }

    @GetMapping(value = {"/colors"})
    public ResponseEntity<List<Color>> getAllColors() {
        return new ResponseEntity<>(materialsService.findAllColors(), HttpStatus.OK);
    }

    @PostMapping(value = "/colors")
    public ResponseEntity<Answer<Boolean>> saveColor(@Valid ColorForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return createErrorResponseWithStatus400(bindingResult, EMPTY_DEFAULT_MESSAGE);
        }
        final boolean wasSave = this.materialsService.saveColor(form.toColor());
        return this.getResponse(wasSave);
    }

    @PutMapping(value = "/colors")
    public ResponseEntity<Answer<Boolean>> updateColor(@Valid ColorForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors() || form.getId() == null) {
            return createErrorResponseWithStatus400(bindingResult, "Id is null");
        }
        final boolean wasUpdate = this.materialsService.updateColor(form.toColor());
        return this.getResponse(wasUpdate);
    }


    @GetMapping(value = {"/materials"})
    public ResponseEntity<List<Material>> getAllMaterials(){
        return new ResponseEntity<>(this.materialsService.findAllMaterial(), HttpStatus.OK);
    }

    @PostMapping(value = "/materials")
    public ResponseEntity<Answer<Boolean>> saveMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return createErrorResponseWithStatus400(bindingResult, EMPTY_DEFAULT_MESSAGE);
        }
        final boolean wasSave = this.materialsService.saveMaterial(form.toMaterial());
        return this.getResponse(wasSave);
    }

    @PutMapping(value = "/materials")
    public ResponseEntity<Answer<Boolean>> updateMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors() || form.getId() == null) {
            return createErrorResponseWithStatus400(bindingResult, "Id is null");
        }
        final boolean wasUpdate = this.materialsService.updateMaterial(form.toMaterial());
        return this.getResponse(wasUpdate);
    }

    private ResponseEntity<Answer<Boolean>> createErrorResponseWithStatus400(@NotNull BindingResult bindingResult, String defaultMessage) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Answer(false, bindingResult
                    .getFieldErrors()
                    .get(FIRST_ERROR)
                    .getDefaultMessage()
            ), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Answer<>(false, defaultMessage), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Answer<Boolean>> getResponse(boolean hasNotError) {
        return new ResponseEntity<>(
                new Answer<>(hasNotError, hasNotError ? "" : errorMessage),
                hasNotError ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
