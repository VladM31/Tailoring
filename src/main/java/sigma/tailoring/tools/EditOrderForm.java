package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import sigma.tailoring.converters.ModifyTailoringOrderConverter;
import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.*;
import sigma.tailoring.exceptions.OrderException;
import sigma.tailoring.service.TailoringOrderService;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditOrderForm {
    @NotNull(message = "Order id mustn't be null", groups = OnUpdate.class)
    private Long orderId;
    @NotNull(message = "Price mustn't be null", groups = OnUpdate.class)
    @Min(value = 1, message = "Price must be great than 0", groups = OnUpdate.class)
    private Integer price;
    @Future(message = "End date must be future", groups = OnUpdate.class)
    @NotNull(message = "End date mustn't be null", groups = OnUpdate.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "Status mustn't be null", groups = OnUpdate.class)
    private OrderStatus status;
    @NotNull(message = "Payment status mustn't be null", groups = OnUpdate.class)
    private OrderPaymentStatus paymentStatus;

    public ModifyTailoringOrder toModifyTailoringOrder(TailoringOrderService orderService, ModifyTailoringOrderConverter converter) {
        var order = getOrder(orderService);

        ModifyTailoringOrder editOrder = converter.convert(order);

        editOrder.setStatus(this.status == null ? order.getStatus() : this.status);
        editOrder.setEndDate(this.endDate == null ? editOrder.getEndDate() : this.endDate);
        editOrder.setCost(this.price == null ? editOrder.getCost() : this.price);
        editOrder.setPaymentStatus(this.paymentStatus == null ? editOrder.getPaymentStatus() : this.paymentStatus);

        return editOrder;
    }

    private TailoringOrderList.Order getOrder(TailoringOrderService orderService) {
        return orderService.findBy(OrderSearchCriteria
                        .builder()
                        .orderIds(List.of(this.orderId))
                        .build(), new Page())
                .getOrderList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new OrderException("Order with number %d not found".formatted(this.orderId)));
    }
}
