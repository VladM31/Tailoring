package sigma.nure.tailoring.tailoring.repository;

import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;

import java.util.List;

public interface MaterialsRepository {
    List<Material> findAllMaterial();

    List<Material> findMaterialsByIdIn(Integer ...ids);

    boolean updateMaterial(Material material);

    boolean saveMaterial(Material material);

    List<Color> findAllColors();

    List<Color> findColorsByIdIn(Integer ...ids);

    boolean updateColor(Color color);

    boolean saveColor(Color color);
}
