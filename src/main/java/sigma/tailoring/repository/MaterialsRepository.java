package sigma.tailoring.repository;

import sigma.tailoring.entities.Color;
import sigma.tailoring.entities.Material;

import java.util.List;

public interface MaterialsRepository {
    List<Material> findAllMaterial();

    boolean updateMaterial(Material material);

    boolean saveMaterial(Material material);

    List<Color> findAllColors();

    boolean updateColor(Color color);

    boolean saveColor(Color color);
}
