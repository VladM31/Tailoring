package sigma.tailoring.controllers;

import com.google.gson.Gson;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.converters.BuyerOrderConverter;
import sigma.tailoring.converters.ModifyTailoringOrderConverter;
import sigma.tailoring.converters.OrderWebSortColumnConverter;
import sigma.tailoring.dto.BuyerOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.User;
import sigma.tailoring.exceptions.OrderException;
import sigma.tailoring.service.TailoringOrderService;
import sigma.tailoring.tools.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("orders")
@Validated
public class OrderController {
    private final TailoringOrderService orderService;
    private final OrderWebSortColumnConverter sortColumnConverter;
    private final BuyerOrderConverter buyerOrderConverter;
    private final HandlerFilter handlerFilter;
    private final ModifyTailoringOrderConverter modifyTailoringOrderConverter;
    private final OrderHandler orderHandler;
    private final Gson gson;

    public OrderController(TailoringOrderService orderService,
                           OrderWebSortColumnConverter sortColumnConverter,
                           BuyerOrderConverter buyerOrderConverter,
                           HandlerFilter handlerFilter,
                           ModifyTailoringOrderConverter modifyTailoringOrderConverter, OrderHandler orderHandler) {
        this.orderService = orderService;
        this.sortColumnConverter = sortColumnConverter;
        this.buyerOrderConverter = buyerOrderConverter;
        this.handlerFilter = handlerFilter;
        this.modifyTailoringOrderConverter = modifyTailoringOrderConverter;
        this.orderHandler = orderHandler;
        this.gson = new Gson();
    }

    @GetMapping
    @ResponseBody
    public TailoringOrderList getTailoringOrderList(OrderFilter orderFilter) {
        return this.orderService.findBy(
                orderFilter.toOrderSearchCriteria(this.handlerFilter),
                orderFilter.toPage(this.sortColumnConverter)
        );
    }

    @GetMapping("customer")
    @ResponseBody
    public List<BuyerOrder> getBuyerOrderList(@AuthenticationPrincipal User user, BuyerOrderFilter filter) {
        return buyerOrderConverter.convert(
                orderService.findBy(
                        filter.toOrderSearchCriteria(handlerFilter, user),
                        filter.toPage(sortColumnConverter)
                ));
    }

    @PutMapping("{orderId}")
    @ResponseBody
    @Validated(OnUpdate.class)
    public boolean editOrder(@Valid @ModelAttribute EditOrderForm form) {
        return orderService.update(form.toModifyTailoringOrder(orderService, modifyTailoringOrderConverter));
    }

    @ResponseBody
    @Validated(OnUpdate.class)
    @PutMapping("{orderId}/status")
    public String editOrderStatus(@AuthenticationPrincipal User user,
                                  @Valid @ModelAttribute OrderStatusForm orderStatusForm) {
        var order = orderStatusForm.toModifyTailoringOrder(modifyTailoringOrderConverter,
                orderHandler.getOrderByRole(
                        user,
                        orderStatusForm.getOrderId()));

        if (orderService.update(order)) {
            return order.getStatus().name();
        }

        throw new OrderException("Order № %d didn't update".formatted(order.getId()));
    }

    @PostMapping("create/specific")
    @Validated(OnSaveForm.class)
    public boolean saveSpecificOrder(
            @AuthenticationPrincipal User user,
            @Valid SpecificOrderForm files) {
        return orderService.save(files.toModifyTailoringOrder(gson, user.getId(), orderHandler));
    }
}
