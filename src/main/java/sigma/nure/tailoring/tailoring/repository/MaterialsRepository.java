package sigma.nure.tailoring.tailoring.repository;

import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;

import java.util.List;

public interface MaterialsRepository {
    List<Material> findAllMaterial();

    boolean updateMaterial(Material material);

    boolean saveMaterial(Material material);

    List<Color> findAllColors();

    boolean updateColor(Color color);

    boolean saveColor(Color color);
}
