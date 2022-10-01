package sigma.tailoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyerOrder {
    private Long id;
    private String dateOfCreation;
    private String status;
    private String paymentStatus;
    private Integer cost;
    private String img;
    private Boolean isFromTemplate;
}
