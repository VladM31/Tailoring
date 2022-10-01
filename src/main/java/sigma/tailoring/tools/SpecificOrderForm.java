package sigma.tailoring.tools;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.entities.OrderPaymentStatus;
import sigma.tailoring.entities.OrderStatus;
import sigma.tailoring.entities.PartSizeOrder;
import sigma.tailoring.validations.ArraySize;
import sigma.tailoring.validations.SpecificOrderFormValid;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SpecificOrderFormValid(checkGroups = OnSave.class, groups = OnSaveForm.class, message = "Specific order has error")
public class SpecificOrderForm {
    @ArraySize(min = 1, max = 10, nullable = false, message = "Upload images must be between 1 and 10 and not null", groups = OnSave.class)
    private MultipartFile[] uploadFiles;
    @NotNull(message = "Color must be set", groups = OnSave.class)
    private Integer colorId;
    @NotNull(message = "Material must be set", groups = OnSave.class)
    private Integer materialId;
    @Size(min = 2, max = 200, message = "Address length must be between 2 and 200", groups = OnSave.class)
    @NotBlank(message = "Address mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Address mustn't be null", groups = OnSave.class)
    private String deliveryAddress;
    @Min(value = 1, message = "Count must be more than 0", groups = OnSave.class)
    @NotNull(message = "Count must be set", groups = OnSave.class)
    private Integer count;
    @Size(max = 2000, message = "Wishes length must be between 0 and 2000", groups = OnSave.class)
    @NotNull(message = "Wishes mustn't be null", groups = OnSave.class)
    private String wishes;
    @NotBlank(message = "Part size mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Part size must be set", groups = OnSave.class)
    private String partSizeJson;

    public ModifyTailoringOrder toModifyTailoringOrder(Gson gson, Long customerId, OrderHandler orderHandler) {
        ModifyTailoringOrder order = new ModifyTailoringOrder();

        order.setAddressForSend(this.deliveryAddress);
        order.setOrderDescription(this.wishes);
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentStatus(OrderPaymentStatus.NOT_PAYMENTED);
        order.setTemplateId(null);
        order.setCustomerId(customerId);
        order.setEndDate(orderHandler.getDefaultEndDate());
        order.setCost(orderHandler.getDefaultCost());
        order.setCountOfOrder(this.count);
        order.setMaterialId(this.materialId);
        order.setColorId(this.colorId);
        order.setPartSizes(List.of(gson.fromJson(this.partSizeJson, PartSizeOrder[].class)));

        order.setUploadImages(List.of(this.uploadFiles));
        order.setImages(List.of());

        return order;
    }
}
