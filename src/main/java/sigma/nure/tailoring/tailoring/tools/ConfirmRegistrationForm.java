package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRegistrationForm {

    @Pattern(regexp = "\\d{10,15}", groups = AllOperation.class)
    @NotBlank(message = "Number mustn't be blank", groups = AllOperation.class)
    @NotNull(message = "Number mustn't be null", groups = AllOperation.class)
    private String phoneNumber;
    @Pattern(regexp = "[0-9A-Za-z]{20,20}", message = "Code consist [0-9A-Za-z] and size 20", groups = AllOperation.class)
    @NotBlank(message = "Code mustn't be blank", groups = AllOperation.class)
    @NotNull(message = "Code mustn't be null", groups = AllOperation.class)
    private String code;
}
