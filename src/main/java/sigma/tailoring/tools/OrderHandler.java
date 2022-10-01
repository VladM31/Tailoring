package sigma.tailoring.tools;

import org.springframework.stereotype.Component;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.Role;
import sigma.tailoring.entities.User;
import sigma.tailoring.exceptions.OrderException;
import sigma.tailoring.service.TailoringOrderService;
import sigma.tailoring.service.TailoringTemplateService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class OrderHandler {
    private final int defaultCost;
    private static final long WAIT_TWO_WEEK = 2;
    private final TailoringOrderService orderService;

    public OrderHandler(TailoringOrderService orderService) {
        this.orderService = orderService;
        this.defaultCost = 2000;
    }

    public TailoringOrderList.Order getOrderByRole(User user, Long orderId) {
        return this.getOrderByRole(user, orderId, new OrderException("Order № %d not found".formatted(orderId)));
    }

    public TailoringOrderList.Order getOrderByRole(User user, Long orderId, RuntimeException exception) {
        return this.getOrder(this.getBuilderByRole(user, orderId)).orElseThrow(() -> exception);
    }

    public LocalDate getDefaultEndDate() {
        return LocalDate.now().plusWeeks(WAIT_TWO_WEEK);
    }


    private Optional<TailoringOrderList.Order> getOrder(OrderSearchCriteria.OrderSearchCriteriaBuilder builder) {
        return this.orderService
                .findBy(builder.build(), new Page())
                .getOrderList()
                .stream()
                .findFirst();
    }

    private OrderSearchCriteria.OrderSearchCriteriaBuilder getBuilderByRole(User user, Long orderId) {
        var orderBuilder = OrderSearchCriteria.builder()
                .orderIds(List.of(orderId));

        if (user.getRole().equals(Role.ADMINISTRATION)) {
            return orderBuilder;
        }

        return orderBuilder.userIds(List.of(user.getId()));
    }

    public int getDefaultCost() {
        return defaultCost;
    }
}
