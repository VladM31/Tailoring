package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private String name;
    private String originalName;

    public Image(String name) {
        this.name = name;
    }
}
