package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TailoringTemplateWithMaterialIds {
    private Long id;
    private int cost;
    private boolean active;
    private String name;
    private String typeTemplate;
    private String templateDescription;
    private LocalDateTime dateOfCreation;
    private Set<String> imagesUrl;
    private Set<Integer> materialIds;
    private Set<Integer> colorIds;
    private Set<PartSizeForTemplate> partSizeForTemplates;
}
