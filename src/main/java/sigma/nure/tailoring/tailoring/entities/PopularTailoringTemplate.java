package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public record PopularTailoringTemplate(
        Long id,
        String name,
        Set<String> imagesUrl
) {
}
