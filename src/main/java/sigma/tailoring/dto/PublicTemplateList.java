package sigma.tailoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.entities.TailoringTemplate;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PublicTemplateList {
    private List<Template> templates;

    public PublicTemplateList(List<TailoringTemplate> tailoringTemplates) {
        templates = tailoringTemplates
                .stream()
                .map(t -> this.toTemplate(t))
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Template {
        private Long id;
        private Integer cost;
        private String name;
        private String frontImage;
        private Set<String> materialsName;
        private Set<String> colorsName;
    }

    private Template toTemplate(TailoringTemplate tailoringTemplate) {
        Template template = new Template();

        template.id = tailoringTemplate.getId();
        template.cost = tailoringTemplate.getCost();
        template.name = tailoringTemplate.getName();
        template.frontImage = tailoringTemplate
                .getImagesUrl()
                .stream()
                .filter(t -> t.contains("front-"))
                .findFirst().orElse("Error.jpg");

        template.materialsName = tailoringTemplate
                .getMaterials()
                .stream()
                .map(m -> m.getName())
                .collect(Collectors.toSet());

        template.colorsName = tailoringTemplate
                .getColors()
                .stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());

        return template;
    }
}
