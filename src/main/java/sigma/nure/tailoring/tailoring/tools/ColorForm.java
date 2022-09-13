package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sigma.nure.tailoring.tailoring.entities.Color;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorForm {
    @Nullable
    private Integer id;
    @Size(min = 2, max = 20, message = "Name must be greater than 2 and less than 20 characters")
    @NotBlank(message = "Name must not be empty")
    private String name;
    @Pattern(regexp = "[0-9A-Fa-b].")
    @Size(min = 6, max = 6, message = "Code does not consist of 6 characters")
    @NotBlank(message = "Code must not be empty")
    private String code;

    public Color toColor() {
        return new Color(id, name, code);
    }

}
