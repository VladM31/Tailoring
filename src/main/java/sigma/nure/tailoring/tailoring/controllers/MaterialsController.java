package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;
import sigma.nure.tailoring.tailoring.service.MaterialsService;
import sigma.nure.tailoring.tailoring.tools.ColorForm;
import sigma.nure.tailoring.tailoring.tools.MaterialForm;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/api/v1")
public class MaterialsController {

    private final MaterialsService materialsService;

    public MaterialsController(MaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    @GetMapping(value = {"/colors", "/free/colors"})
    public ResponseEntity<List<Color>> getAllColors() {
        return new ResponseEntity<>(materialsService.findAllColors(), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/colors")
    public ResponseEntity<Boolean> saveColor(@Valid ColorForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        final boolean wasSave = this.materialsService.saveColor(form.toColor());
        return new ResponseEntity<>(wasSave, wasSave ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(value = "/colors")
    public ResponseEntity<Boolean> updateColor(@Valid ColorForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors() || form.getId() == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
        }
        final boolean wasUpdate = this.materialsService.updateColor(form.toColor());
        return new ResponseEntity<>(wasUpdate, wasUpdate ? HttpStatus.ACCEPTED : HttpStatus.NOT_MODIFIED);
    }


    @GetMapping(value = "/materials")
    public ResponseEntity<List<Material>> getAllMaterials(){
        return new ResponseEntity<>(this.materialsService.findAllMaterial(), HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/materials")
    public ResponseEntity<Boolean> saveMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        final boolean wasSave = this.materialsService.saveMaterial(form.toMaterial());
        return new ResponseEntity<>(wasSave, wasSave ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping(value = "/materials")
    public ResponseEntity<Boolean> updateMaterial(@Valid MaterialForm form, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors() || form.getId() == null) {
            return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
        }
        final boolean wasUpdate = this.materialsService.updateMaterial(form.toMaterial());
        return new ResponseEntity<>(wasUpdate, wasUpdate ? HttpStatus.ACCEPTED : HttpStatus.NOT_MODIFIED);
    }
}
