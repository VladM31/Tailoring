package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TailoringOrder {
    private Long id;
    private String addressForSend;
    private String orderDescription;
    private OrderStatus status;
    private OrderPaymentStatus paymentStatus;
    private LocalDateTime dateOfCreation;
    private Long templateId;
    private LocalDate endDate;
    private int cost;
    private int countOfOrder;
    private Material material;
    private Color color;
    private ShortUserData userData;
    private List<Image> images;
    private List<PartSizeOrder> partSizes;

    public boolean isFromTemplate() {
        return this.templateId != null;
    }
}
