package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.nure.tailoring.tailoring.entities.OrderPaymentStatus;
import sigma.nure.tailoring.tailoring.entities.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchCriteria {
    private Iterable<Long> orderIds;
    private Iterable<Long> materialIds;
    private Iterable<Long> colorIds;
    private Iterable<Long> userIds;
    private Iterable<OrderStatus> orderStatuses;
    private Iterable<OrderPaymentStatus> paymentStatuses;
    private String address;
    private String phoneNumber;
    private String city;
    private String country;
    private String firstname;
    private LocalDate beforeOrEqualsEndDate;
    private LocalDate afterOrEqualsEndDate;
    private LocalDateTime beforeOrEqualsDateOfCreation;
    private LocalDateTime afterOrEqualsDareOfCreation;
    private Boolean userIsMale;
    private Boolean isFromTemplate;
    private Integer greatOrEqualsCost;
    private Integer lessOrEqualsCost;
    private Integer greatOrEqualsCount;
    private Integer lessOrEqualsCount;
}
