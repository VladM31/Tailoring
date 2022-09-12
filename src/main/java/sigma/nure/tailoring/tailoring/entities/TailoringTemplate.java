package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TailoringTemplate {
    private Long id;
    private String name;
    private boolean active;
    private LocalDateTime dateOfCreation;
    private int cost;
    private String typeTemplate;
    private String templateDescription;
    private Set<Material> materials;
    private Set<Color> colors;
    private Set<String> imagesUrl;
    private Set<PartSizeForTemplate> partSizeForTemplates;
}
