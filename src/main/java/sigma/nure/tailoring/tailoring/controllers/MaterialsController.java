package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;
import sigma.nure.tailoring.tailoring.service.MaterialsService;
import sigma.nure.tailoring.tailoring.tools.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class MaterialsController {
    private final MaterialsService materialsService;

    public MaterialsController(MaterialsService materialsService) {
        this.materialsService = materialsService;
    }

    @GetMapping(value = {"/colors"})
    public List<Color> getAllColors() {
        return materialsService.findAllColors();
    }

    @PostMapping(value = "/colors")
    @Validated({OnSave.class, AllOperation.class})
    public boolean saveColor(@Valid ColorForm form) {
        return materialsService.saveColor(form.toColor());
    }

    @PutMapping(value = "/colors")
    @Validated({OnUpdate.class, AllOperation.class})
    public boolean updateColor(@Valid ColorForm form) {
        return materialsService.updateColor(form.toColor());
    }

    @GetMapping(value = {"/materials"})
    public List<Material> getAllMaterials() {
        return this.materialsService.findAllMaterial();
    }

    @PostMapping(value = "/materials")
    @Validated({OnSave.class, AllOperation.class})
    public boolean saveMaterial(@Valid MaterialForm form) {
        return materialsService.saveMaterial(form.toMaterial());
    }

    @PutMapping(value = "/materials")
    @Validated({OnUpdate.class, AllOperation.class})
    public boolean updateMaterial(@Valid MaterialForm form) {
        return materialsService.updateMaterial(form.toMaterial());
    }


}