package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.entities.OrderPaymentStatus;
import sigma.tailoring.entities.OrderStatus;

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
    private Iterable<Long> templateIds;
    private Iterable<OrderStatus> orderStatuses;
    private Iterable<OrderPaymentStatus> paymentStatuses;
    private String address;
    private String phoneNumber;
    private String city;
    private String country;
    private String firstname;
    private Range<LocalDate> endDate;
    private Range<LocalDateTime> dateOfCreation;
    private Boolean isMale;
    private Boolean isTemplate;
    private Range<Integer> cost;
    private Range<Integer> count;

}
