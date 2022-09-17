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
@RequestMapping("/order-stuff")
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
        return new ResponseEntity<>(materialsService.saveColor(form.toColor()), HttpStatus.OK);
    }

    @PutMapping(value = "/colors")
    public ResponseEntity<Boolean> updateColor(@Valid ColorForm form, BindingResult bindingResult) {
        return new ResponseEntity<>(materialsService.updateColor(form.toColor()), HttpStatus.OK);
    }


    @GetMapping(value = {"/materials"})
    public ResponseEntity<List<Material>> getAllMaterials() {
        return new ResponseEntity<>(this.materialsService.findAllMaterial(), HttpStatus.OK);
    }

    @PostMapping(value = "/materials")
    public ResponseEntity<Boolean> saveMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        return new ResponseEntity<>(materialsService.saveMaterial(form.toMaterial()), HttpStatus.OK);
    }

    @PutMapping(value = "/materials")
    public ResponseEntity<Boolean> updateMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        return new ResponseEntity<>(materialsService.updateMaterial(form.toMaterial()), HttpStatus.OK);
    }


}
