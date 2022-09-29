package sigma.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.tailoring.entities.Color;
import sigma.tailoring.entities.Material;
import sigma.tailoring.entities.TailoringTemplate;
import sigma.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.tailoring.repository.MaterialsRepository;

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

        final var materialsConverter = new StuffConverter<>(materialsRepository.findAllMaterial(),
                (m) -> m.getId());

        final var colorsConverter = new StuffConverter<>(materialsRepository.findAllColors(), c -> c.getId());

        return new TemplateConvertor(materialsConverter, colorsConverter);
    }

    private class StuffConverter<Stuff> {
        private final Map<Integer, Stuff> stuffMap;

        public StuffConverter(List<Stuff> stuffList, Function<Stuff, Integer> getId) {
            stuffMap = stuffList.stream().collect(Collectors.toMap(s -> getId.apply(s), s -> s));
        }

        public Set<Stuff> convert(Iterable<Integer> ids) {
            Set<Stuff> stuffs = new HashSet<>();

            ids.forEach(id -> stuffs.add(stuffMap.get(id)));

            return stuffs;
        }
    }

    public class TemplateConvertor {
        private final StuffConverter<Material> materialsConverter;
        private final StuffConverter<Color> colorsConverter;


        public TemplateConvertor(StuffConverter<Material> materialsConverter,
                                 StuffConverter<Color> colorsConverter) {
            this.materialsConverter = materialsConverter;
            this.colorsConverter = colorsConverter;
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

            template.setMaterials(materialsConverter.convert(templateFromRepo.getMaterialIds()));
            template.setColors(colorsConverter.convert(templateFromRepo.getColorIds()));

            template.setImagesUrl(templateFromRepo.getImagesUrl());
            template.setPartSizeForTemplates(templateFromRepo.getPartSizeForTemplates());

            return template;
        }
    }
}
