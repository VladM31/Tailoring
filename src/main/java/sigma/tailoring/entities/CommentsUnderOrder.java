package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsUnderOrder {
    private Long id;
    private String message;
    private LocalDateTime dateOfCreation;
    private Long userId;
    private Long tailoringOrderId;
}
