package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class StuffController {

    private final List<Color> colorRepository;
    private final List<Material> materialsRepository;
    private int generatorId = 0;

    public StuffController(){
        colorRepository = new ArrayList<>();
        colorRepository.add(new Color(generatorId++,"Yellow","FFFF00"));
        colorRepository.add(new Color(generatorId++,"Red","FF0000"));
        colorRepository.add(new Color(generatorId++,"Black","000000"));

        materialsRepository = new ArrayList<>();
        materialsRepository.add(new Material(generatorId++,"Wool",50));
        materialsRepository.add(new Material(generatorId++,"Silk",120));
    }

    @GetMapping(value = "/colors")
    public ResponseEntity<List<Color>> getAllColors(){

        return new ResponseEntity<>(colorRepository, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/colors")
    public boolean addColor(String name,String code){
        colorRepository.add(new Color(generatorId++,name,code));
        return true;
    }

    @PutMapping (value = "/colors")
    public boolean updateColor(Color color){
        Color original = colorRepository.stream().filter(c -> c.getId().equals(color.getId()))
                .findFirst().get();

        original.setCode(color.getCode());
        original.setName(color.getName());
        return true;
    }


    @GetMapping(value = "/materials")
    public ResponseEntity<List<Material>> getAllMaterials(){
        return new ResponseEntity<>(this.materialsRepository, HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/materials")
    public boolean addMaterial(@RequestParam String name,@RequestParam Integer cost){
        materialsRepository.add(new Material(generatorId++,name,cost));
        return true;
    }

    @PutMapping (value = "/materials")
    public boolean updateMaterial(Material material){
        Material original = materialsRepository.stream().filter(m -> m.getId().equals(material.getId()))
                .findFirst().get();

        original.setCost(material.getCost());
        original.setName(material.getName());
        return true;
    }
}
