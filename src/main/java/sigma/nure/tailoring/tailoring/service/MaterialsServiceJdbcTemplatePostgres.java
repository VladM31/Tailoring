package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;
import sigma.nure.tailoring.tailoring.repository.MaterialsRepository;

import java.util.List;

public class MaterialsServiceJdbcTemplatePostgres implements MaterialsService{

    private final MaterialsRepository repository;

    public MaterialsServiceJdbcTemplatePostgres(MaterialsRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Material> findAllMaterial() {
        return repository.findAllMaterial();
    }

    @Override
    public boolean updateMaterial(Material material) {
        return repository.updateMaterial(material);
    }

    @Override
    public boolean saveMaterial(Material material) {
        return repository.saveMaterial(material);
    }

    @Override
    public List<Color> findAllColors() {
        return repository.findAllColors();
    }

    @Override
    public boolean updateColor(Color color) {
        return repository.updateColor(color);
    }

    @Override
    public boolean saveColor(Color color) {
        return repository.saveColor(color);
    }
}
