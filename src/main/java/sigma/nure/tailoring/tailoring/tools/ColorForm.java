package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.nure.tailoring.tailoring.entities.Color;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorForm {
    @Null(groups = OnSave.class)
    @NotNull(groups = OnUpdate.class)
    private Integer id;
    @Size(min = 2, max = 20, message = "Name must be greater than 2 and less than 20 characters", groups = AllOperation.class)
    @NotBlank(message = "Name must not be empty", groups = AllOperation.class)
    private String name;
    @Pattern(regexp = "[0-9A-Fa-f]{6}")
    @Size(min = 6, max = 6, message = "Code does not consist of 6 characters", groups = AllOperation.class)
    @NotBlank(message = "Code must not be empty", groups = AllOperation.class)
    @NotNull(message = "Code mustn't be null", groups = AllOperation.class)
    private String code;

    public Color toColor() {
        return new Color(id, name, code);
    }

}
