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
public class OrderParameters {
    private Iterable<Long> ids;
    private Iterable<Long> materialIds;
    private Iterable<Long> colorIds;
    private Iterable<Long> userIds;
    private Iterable<OrderStatus> orderStatuses;
    private Iterable<OrderPaymentStatus> paymentStatuses;
    private String addressForSendContaining;
    private String userPhoneNumberContaining;
    private String userCityContaining;
    private String userCountryContaining;
    private String userFirstnameContaining;
    private LocalDate beforeOrEqualsEndDate,afterOrEqualsEndDate;
    private LocalDateTime beforeOrEqualsDateOfCreation,afterOrEqualsDareOfCreation;
    private Boolean userIsMale;
    private Boolean isFromTemplate;
    private Integer greatOrEqualsCost,lessOrEqualsCost;
    private Integer greatOrEqualsCount,lessOrEqualsCount;
}
