package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentOrderForm {
    @NotBlank(message = "Message must be not blank")
    @Size(min = 1, max = 200, message = "Message must be great than 0 and less than 201")
    private String message;
    @NotNull(message = "User not set")
    private Long userId;
    @NotNull(message = "Order not set")
    private Long tailoringOrderId;
    private LocalDateTime dateOfCreation;
}
