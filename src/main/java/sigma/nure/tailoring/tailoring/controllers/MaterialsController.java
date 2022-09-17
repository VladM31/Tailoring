package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;
import sigma.nure.tailoring.tailoring.service.MaterialsService;
import sigma.nure.tailoring.tailoring.tools.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order-stuff")
@Validated
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
    @Validated({OnSave.class, Every.class})
    public ResponseEntity<Boolean> saveColor(@Valid ColorForm form) {
        return new ResponseEntity<>(materialsService.saveColor(form.toColor()), HttpStatus.OK);
    }

    @PutMapping(value = "/colors")
    @Validated({OnUpdate.class, Every.class})
    public ResponseEntity<Boolean> updateColor(@Valid ColorForm form) {
        return new ResponseEntity<>(materialsService.updateColor(form.toColor()), HttpStatus.OK);
    }


    @GetMapping(value = {"/materials"})
    public ResponseEntity<List<Material>> getAllMaterials() {
        return new ResponseEntity<>(this.materialsService.findAllMaterial(), HttpStatus.OK);
    }

    @PostMapping(value = "/materials")
    @Validated({OnSave.class, Every.class})
    public ResponseEntity<Boolean> saveMaterial(@Valid MaterialForm form) {
        return new ResponseEntity<>(materialsService.saveMaterial(form.toMaterial()), HttpStatus.OK);
    }

    @PutMapping(value = "/materials")
    @Validated({OnUpdate.class, Every.class})
    public ResponseEntity<Boolean> updateMaterial(@Valid MaterialForm form) {
        return new ResponseEntity<>(materialsService.updateMaterial(form.toMaterial()), HttpStatus.OK);
    }


}
