package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentOrderForm {
    @NotBlank(message = "Message must be not blank", groups = OnSave.class)
    @Size(min = 1, max = 200, message = "Message must be great than 0 and less than 201", groups = OnSave.class)
    private String message;
    @NotNull(message = "User id not set", groups = OnSave.class)
    private Long userId;
    @NotNull(message = "Order id not set", groups = OnSave.class)
    private Long tailoringOrderId;
    private LocalDateTime dateOfCreation;
}
