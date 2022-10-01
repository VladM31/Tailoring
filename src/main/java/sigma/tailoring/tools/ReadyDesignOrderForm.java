package sigma.tailoring.tools;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.entities.OrderPaymentStatus;
import sigma.tailoring.entities.OrderStatus;
import sigma.tailoring.entities.PartSizeOrder;
import sigma.tailoring.validations.ReadyDesignOrderFormValid;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@ReadyDesignOrderFormValid(pageName = "CreateOrderBasedOnTemplatePage",
        formAttribute = "orderForm", templateAttribute = "template",
        partSizeAttribute = "partSizeJson", errorAttribute = "errors",
        checkGroups = OnSave.class, groups = OnSaveForm.class,
        message = "Please read errors and solve their"
)
public class ReadyDesignOrderForm {
    private static final long WAIT_TWO_WEEK = 2;
    private static final int START_COUNT = 1;
    @Size(min = 2, max = 200, message = "Address length must be between 2 and 200", groups = OnSave.class)
    @NotBlank(message = "Address mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Address mustn't be null", groups = OnSave.class)
    private String address;
    @Size(max = 2000, message = "Describe length must be between 0 and 2000", groups = OnSave.class)
    @NotNull(message = "Describe mustn't be null", groups = OnSave.class)
    private String orderDescription;
    @NotNull(message = "Template id mustn't be null", groups = OnSave.class)
    private Long templateId;
    @NotNull(message = "Material must be selected", groups = OnSave.class)
    private Integer materialId;
    @NotNull(message = "Color must be selected", groups = OnSave.class)
    private Integer colorId;
    @Min(value = 1, message = "Count must be more than 0", groups = OnSave.class)
    @NotNull(message = "Count must be set", groups = OnSave.class)
    private Integer count;
    @Min(value = 0, message = "Cost must be more than 0", groups = OnSave.class)
    @NotNull(message = "Cost must be set", groups = OnSave.class)
    private Integer cost;
    @NotBlank(message = "Part size mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Part size must be set", groups = OnSave.class)
    private String partSizeJson;

    public ReadyDesignOrderForm() {
        this.count = START_COUNT;
    }


    public ModifyTailoringOrder toModifyTailoringOrder(Gson gson, Long customerId) {
        ModifyTailoringOrder order = new ModifyTailoringOrder();

        order.setAddressForSend(this.address);
        order.setOrderDescription(this.orderDescription);
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(OrderPaymentStatus.NOT_PAYMENTED);
        order.setTemplateId(this.templateId);
        order.setCustomerId(customerId);
        order.setEndDate(this.getDefaultEndDate());
        order.setCost(this.cost);
        order.setCountOfOrder(this.count);
        order.setMaterialId(this.materialId);
        order.setColorId(this.colorId);
        order.setPartSizes(List.of(gson.fromJson(this.partSizeJson, PartSizeOrder[].class)));

        order.setUploadImages(List.of(new MultipartFile[]{}));
        order.setImages(List.of());

        return order;
    }

    private LocalDate getDefaultEndDate() {
        return LocalDate.now().plusWeeks(WAIT_TWO_WEEK);
    }

    public String joinImages(List<String> images, String directory) {
        return "["
                + images
                .stream()
                .map(img -> "'" + directory + "/" + img + "'")
                .collect(Collectors.joining(","))
                + "]";
    }
}
