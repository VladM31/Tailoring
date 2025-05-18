package sigma.tailoring.controllers;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.google.gson.Gson;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.tools.*;
import sigma.tailoring.service.TailoringOrderService;
import sigma.tailoring.entities.User;
import sigma.tailoring.service.TailoringTemplateService;

import javax.validation.Valid;
import java.util.List;

@Controller
@Validated
@RequestMapping("orders")
public class OrderPageController {
    private final TailoringOrderService orderService;
    private final TailoringTemplateService templateService;
    private final Gson gson;
    private final OrderHandler orderHandler;

    public OrderPageController(TailoringOrderService orderService,
                               TailoringTemplateService templateService,
                               OrderHandler orderHandler) {
        this.orderService = orderService;
        this.templateService = templateService;
        this.orderHandler = orderHandler;
        this.gson = new Gson();
    }

    @GetMapping("{orderId}/chat")
    public String getChatPage(@AuthenticationPrincipal User user, Model model, @PathVariable Long orderId) {
        model.addAttribute("order", orderHandler.getOrderByRole(user, orderId,
                new RuntimeException("Order not found")));
        model.addAttribute("user", user);
        return "chatOrderPage";
    }

    @GetMapping("/create/based-on-template/{templateId}")
    public String getCreateOrderBasedOnTemplatePage(Model model, @PathVariable Long templateId) {
        var template = templateService.findBy(
                TailoringTemplateSearchCriteria.builder().templateIds(List.of(templateId)).build(),
                new Page()
        ).stream().findFirst().orElseThrow();

        model.addAttribute("template", template);
        model.addAttribute("orderForm", new ReadyDesignOrderForm());
        model.addAttribute("partSizeJson", gson.toJson(template.getPartSizeForTemplates()));

        return "CreateOrderBasedOnTemplatePage";
    }

    @Validated(OnSaveForm.class)
    @PostMapping("/create/based-on-template/{templateId}")
    public String saveOrderBasedOnTemplate(@AuthenticationPrincipal User user, @ModelAttribute @Valid ReadyDesignOrderForm orderForm) {
        orderService.save(orderForm.toModifyTailoringOrder(gson, user.getId()));
        return "redirect:/orders/customer/show";
    }

    @GetMapping("{orderId}/show")
    public String getOrderDescribePage(@AuthenticationPrincipal User user,
                                       @PathVariable Long orderId,
                                       Model model) {
        model.addAttribute("order", orderHandler.getOrderByRole(user, orderId,
                new RuntimeException("Order not found")));

        return "OrderDataPage";
    }

    @GetMapping("customer/show")
    public String getCustomerOrderOffice() {
        return "CustomerOffice";
    }

    @GetMapping("management")
    @PreAuthorize("hasAuthority('ADMINISTRATION')")
    public String getOrderManagement() {
        return "ShowOrdersForAdmin";
    }


    @GetMapping("create/specific")
    public String getCreateSpecificPage() {
        return "CreateSpecificOrder";
    }


}
