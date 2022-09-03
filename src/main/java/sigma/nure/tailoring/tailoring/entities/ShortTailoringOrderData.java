package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortTailoringOrderData {
    private Long id;
    private int cost;
    private String image;
    private LocalDate endDate;
    private OrderStatus orderStatus;
    private LocalDateTime dateOfCreation;
    private OrderPaymentStatus paymentStatus;

}
