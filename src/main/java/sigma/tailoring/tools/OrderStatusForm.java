package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.converters.ModifyTailoringOrderConverter;
import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.OrderStatus;
import sigma.tailoring.exceptions.OrderException;
import sigma.tailoring.service.TailoringOrderService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusForm {
    private static Map<OrderStatus, OrderStatus> STATUS_CHANGER = buildStatusConverter();
    @NotNull(message = "Order id mustn't be null", groups = OnUpdate.class)
    private Long orderId;

    public ModifyTailoringOrder toModifyTailoringOrder(ModifyTailoringOrderConverter converter,
                                                       TailoringOrderList.Order order
    ) {
        var modifyOrder = converter.convert(order);

        modifyOrder.setStatus(STATUS_CHANGER.getOrDefault(
                order.getStatus(),
                OrderStatus.CANCELLED));

        return modifyOrder;
    }

    private static Map<OrderStatus, OrderStatus> buildStatusConverter() {
        return Map.of(
                OrderStatus.AWAITING_PRICE_CONFIRMATION, OrderStatus.UNDER_DEVELOPMENT,
                OrderStatus.DONE, OrderStatus.DONE
        );
    }
}
