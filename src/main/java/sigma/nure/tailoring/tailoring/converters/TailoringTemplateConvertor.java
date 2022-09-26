package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplate;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.nure.tailoring.tailoring.repository.MaterialsRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TailoringTemplateConvertor {
    private final MaterialsRepository materialsRepository;

    public TailoringTemplateConvertor(MaterialsRepository materialsRepository) {
        this.materialsRepository = materialsRepository;
    }

    public TemplateConvertor getConverter() {

        final var toMaterials = createMaterialsConverter(materialsRepository.findAllMaterial());

        final var toColors = createColorsConverter(materialsRepository.findAllColors());

        return new TemplateConvertor(toMaterials, toColors);
    }


    private Function<Iterable<Integer>, Set<Material>> createMaterialsConverter(List<Material> materialList) {
        final Map<Integer, Material> materialMap = materialList.stream().collect(Collectors.toMap(m -> m.getId(), m -> m));

        return (ids) -> {
            Set<Material> materials = new HashSet<>();

            ids.forEach(id -> materials.add(materialMap.get(id)));

            return materials;
        };
    }

    private Function<Iterable<Integer>, Set<Color>> createColorsConverter(List<Color> colorList) {
        final Map<Integer, Color> colorMap = colorList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

        return (ids) -> {
            Set<Color> colors = new HashSet<>();

            ids.forEach(id -> colors.add(colorMap.get(id)));

            return colors;
        };
    }

    public class TemplateConvertor {
        private final Function<Iterable<Integer>, Set<Material>> toMaterials;
        private final Function<Iterable<Integer>, Set<Color>> toColors;


        public TemplateConvertor(Function<Iterable<Integer>, Set<Material>> toMaterials,
                                 Function<Iterable<Integer>, Set<Color>> toColors) {
            this.toMaterials = toMaterials;
            this.toColors = toColors;
        }


        public TailoringTemplate convert(TailoringTemplateWithMaterialIds templateFromRepo) {
            TailoringTemplate template = new TailoringTemplate();

            template.setId(templateFromRepo.getId());
            template.setName(templateFromRepo.getName());
            template.setActive(templateFromRepo.isActive());
            template.setDateOfCreation(templateFromRepo.getDateOfCreation());
            template.setCost(templateFromRepo.getCost());
            template.setTypeTemplate(templateFromRepo.getTypeTemplate());
            template.setTemplateDescription(templateFromRepo.getTemplateDescription());

            template.setMaterials(toMaterials.apply(templateFromRepo.getMaterialIds()));
            template.setColors(toColors.apply(templateFromRepo.getColorIds()));

            template.setImagesUrl(templateFromRepo.getImagesUrl());
            template.setPartSizeForTemplates(templateFromRepo.getPartSizeForTemplates());

            return template;
        }
    }
}
