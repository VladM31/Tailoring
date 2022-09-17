package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sigma.nure.tailoring.tailoring.entities.Material;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialForm {
    @Null(groups = OnSave.class)
    @NotNull(groups = OnUpdate.class)
    private Integer id;
    @Size(min = 2, max = 60, message = "Name must be greater than 2 and less than 60 characters", groups = Every.class)
    @NotBlank(message = "Name must not be empty", groups = Every.class)
    @NotNull(message = "Name mustn't be null", groups = Every.class)
    private String name;
    @Min(message = "Cost can not be less than 1", value = 1, groups = Every.class)
    @NotNull(message = "Cost mustn't be null", groups = Every.class)
    private Integer cost;

    public Material toMaterial() {
        return new Material(id, name, cost);
    }
}
