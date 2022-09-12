package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TailoringTemplateSearchCriteria {
    private Iterable<Long> templateIds;
    private Iterable<Integer> colorIds;
    private Iterable<Integer> materialIds;
    private String name;
    private String typeTemplate;
    private String description;
    private Boolean isActive;
    private Range<Integer> cost;
    private Range<LocalDateTime> dateOfCreation;
}
