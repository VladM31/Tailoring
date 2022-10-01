package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.converters.OrderWebSortColumnConverter;
import sigma.tailoring.entities.OrderPaymentStatus;
import sigma.tailoring.entities.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilter {
    private Long orderId;
    private Long userId;
    private Long[] materialIds;
    private Long[] colorIds;
    private Long[] templateIds;
    private OrderStatus[] orderStatuses;
    private OrderPaymentStatus[] paymentStatuses;
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

    private Long limit;
    private Long offset;
    private boolean desc;
    private OrderSortColumn sortColumn;

    public OrderSearchCriteria toOrderSearchCriteria(HandlerFilter handler) {
        return OrderSearchCriteria.builder()
                .orderIds(this.orderId == null ? null : List.of(this.orderId))
                .materialIds(handler.toList(this.materialIds))
                .colorIds(handler.toList(this.colorIds))
                .userIds(this.userId == null ? null : List.of(this.userId))
                .orderStatuses(handler.toList(this.orderStatuses))
                .paymentStatuses(handler.toList(this.paymentStatuses))
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .city(this.city)
                .country(this.country)
                .firstname(this.firstname)
                .endDate(this.endDate)
                .dateOfCreation(this.dateOfCreation)
                .isMale(this.isMale)
                .isTemplate(this.isTemplate)
                .cost(this.cost)
                .count(this.count)
                .build();
    }

    public Page toPage(OrderWebSortColumnConverter sortColumnConverter) {
        return Page.builder()
                .limit(this.limit)
                .offset(this.offset)
                .direction(this.desc ? Page.Direction.DESC : Page.Direction.ASC)
                .orderBy(sortColumnConverter.convert(this.sortColumn))
                .build();
    }
}
