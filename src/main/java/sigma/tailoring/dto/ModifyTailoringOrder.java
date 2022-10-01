package sigma.tailoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.entities.Image;
import sigma.tailoring.entities.OrderPaymentStatus;
import sigma.tailoring.entities.OrderStatus;
import sigma.tailoring.entities.PartSizeOrder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyTailoringOrder {
    private Long id;
    private String addressForSend;
    private String orderDescription;
    private OrderStatus status;
    private OrderPaymentStatus paymentStatus;
    private LocalDateTime dateOfCreation;
    private Long templateId;
    private Long customerId;
    private LocalDate endDate;
    private Integer cost;
    private Integer countOfOrder;
    private Integer materialId;
    private Integer colorId;
    private List<MultipartFile> uploadImages;
    private List<PartSizeOrder> partSizes;
    private List<Image> images;

}
