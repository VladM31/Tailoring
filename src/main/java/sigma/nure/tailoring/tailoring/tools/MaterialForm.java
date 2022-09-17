package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sigma.nure.tailoring.tailoring.entities.Material;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialForm {
    private Integer id;
    @Size(min = 2, max = 60, message = "Name must be greater than 2 and less than 60 characters")
    @NotBlank(message = "Name must not be empty")
    @NotNull(message = "Name mustn't be null")
    private String name;
    @Min(message = "Cost can not be less than 1", value = 1)
    @NotNull(message = "Cost mustn't be null")
    private Integer cost;

    public Material toMaterial() {
        return new Material(id, name, cost);
    }
}
