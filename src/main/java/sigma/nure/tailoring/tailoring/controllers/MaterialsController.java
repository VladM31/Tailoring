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
import sigma.nure.tailoring.tailoring.tools.ColorForm;
import sigma.nure.tailoring.tailoring.tools.MaterialForm;
import sigma.nure.tailoring.tailoring.tools.ResponseException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@PropertySource("classpath:properties/rest-resources.properties")
@RequestMapping("${material.url.resources}")
public class MaterialsController {
    private final MaterialsService materialsService;

    public MaterialsController(MaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    @GetMapping(value = {"/colors"})
    public ResponseEntity<List<Color>> getAllColors() {
        return new ResponseEntity<>(materialsService.findAllColors(), HttpStatus.OK);
    }

    @PostMapping(value = "/colors")
    public ResponseEntity<Boolean> saveColor(@Valid @ModelAttribute ColorForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw ResponseException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
                    .bindingResultToMessage(bindingResult);
        }
        if (materialsService.saveColor(form.toColor())) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        throw ResponseException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR).
                errorMessage("MaterialsService.saveColor didn't work")
                .build();
    }

    @PutMapping(value = "/colors")
    public ResponseEntity<Boolean> updateColor(@Valid ColorForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || form.getId() == null) {
            throw ResponseException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .answerMessage(form.getId() == null ? "Id is null" : "")
                    .build()
                    .bindingResultToMessage(bindingResult);
        }
        if (materialsService.updateColor(form.toColor())) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        throw ResponseException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR).
                errorMessage("MaterialsService.updateColor didn't work")
                .build();
    }


    @GetMapping(value = {"/materials"})
    public ResponseEntity<List<Material>> getAllMaterials() {
        return new ResponseEntity<>(this.materialsService.findAllMaterial(), HttpStatus.OK);
    }

    @PostMapping(value = "/materials")
    public ResponseEntity<Boolean> saveMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw ResponseException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
                    .bindingResultToMessage(bindingResult);
        }
        if (materialsService.saveMaterial(form.toMaterial())) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        throw ResponseException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR).
                errorMessage("MaterialsService.saveMaterial didn't work")
                .build();
    }

    @PutMapping(value = "/materials")
    public ResponseEntity<Boolean> updateMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors() || form.getId() == null) {
            throw ResponseException.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .answerMessage(form.getId() == null ? "Id is null" : "")
                    .build()
                    .bindingResultToMessage(bindingResult);
        }
        if (materialsService.updateMaterial(form.toMaterial())) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        throw ResponseException.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR).
                errorMessage("MaterialsService.updateMaterial didn't work")
                .build();
    }


}
