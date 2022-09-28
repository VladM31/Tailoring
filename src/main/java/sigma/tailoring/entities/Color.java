package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Color {
    private Integer id;
    private String name;
    private String code;

    public Color(Integer id) {
        this.id = id;
    }
}
