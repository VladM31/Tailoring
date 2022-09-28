package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartSizeForTemplate {
    private String name;
    private Set<Integer> width;
    private Set<Integer> volume;
    private Set<Integer> length;
    private Set<Integer> height;
}
