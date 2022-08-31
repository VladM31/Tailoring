package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
