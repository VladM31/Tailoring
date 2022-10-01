package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.converters.OrderWebSortColumnConverter;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.OrderPaymentStatus;
import sigma.tailoring.entities.OrderStatus;
import sigma.tailoring.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyerOrderFilter {
    public static final List<OrderSortColumn> AVAILABLE_COLUMNS_TO_SORT = buildAvailableColumns();
    private Range<LocalDateTime> dateOfCreation;
    private Range<Integer> cost;
    private OrderStatus[] statuses;
    private OrderPaymentStatus[] paymentStatuses;
    private Boolean isFromTemplate;

    private Long limit;
    private Long offset;
    private boolean desc;
    private OrderSortColumn sortColumn;

    public OrderSearchCriteria toOrderSearchCriteria(HandlerFilter handler, User user){
        return OrderSearchCriteria.builder()
                .userIds(List.of(user.getId()))
                .dateOfCreation(this.dateOfCreation)
                .cost(this.cost)
                .orderStatuses(handler.toList(this.statuses))
                .paymentStatuses(handler.toList(this.paymentStatuses))
                .isTemplate(this.isFromTemplate)
                .build();
    }

    public Page toPage(OrderWebSortColumnConverter sortColumnConverter){
        return Page.builder()
                .limit(this.limit)
                .offset(this.offset)
                .direction(this.desc ? Page.Direction.DESC : Page.Direction.ASC)
                .orderBy(sortColumnConverter.convert(
                        AVAILABLE_COLUMNS_TO_SORT.contains(this.sortColumn == null ?
                                OrderSortColumn.DATE_OF_CREATION : this.sortColumn )
                                ? this.sortColumn : OrderSortColumn.DATE_OF_CREATION
                ))
                .build();
    }

    private static List<OrderSortColumn> buildAvailableColumns(){
        return List.of(
                OrderSortColumn.DATE_OF_CREATION,
                OrderSortColumn.COST,
                OrderSortColumn.STATUS,
                OrderSortColumn.PAYMENT_STATUS
        );
    }
}
