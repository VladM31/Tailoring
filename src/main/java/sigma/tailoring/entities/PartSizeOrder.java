package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartSizeOrder {
    private String name;
    private Integer width;
    private Integer volume;
    private Integer length;
    private Integer height;
}
