package sigma.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.Image;
import sigma.tailoring.entities.PartSizeOrder;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModifyTailoringOrderConverter {

    public ModifyTailoringOrder convert(TailoringOrderList.Order order) {
        ModifyTailoringOrder editOrder = new ModifyTailoringOrder();


        editOrder.setStatus(order.getStatus());
        editOrder.setPaymentStatus(order.getPaymentStatus());
        editOrder.setCost(order.getCost());
        editOrder.setEndDate(order.getEndDate());

        editOrder.setId(order.getId());
        editOrder.setAddressForSend(order.getAddressForSend());
        editOrder.setOrderDescription(order.getOrderDescription());
        editOrder.setDateOfCreation(order.getDateOfCreation());
        editOrder.setTemplateId(order.getTemplateId());
        editOrder.setCustomerId(order.getClientOrder().getId());
        editOrder.setCountOfOrder(order.getCountOfOrder());
        editOrder.setMaterialId(order.getMaterial().getId());
        editOrder.setColorId(order.getColor().getId());
        editOrder.setPartSizes(this.toPartSizeOrder(order.getPartSizes()));
        editOrder.setImages(order.getImages().stream()
                .map(f -> new Image(f.getName()))
                .collect(Collectors.toList()));
        editOrder.setUploadImages(List.of());

        return editOrder;
    }

    private List<PartSizeOrder> toPartSizeOrder(List<TailoringOrderList.OrderPart> partSizes) {
        return partSizes
                .stream()
                .map(p -> new PartSizeOrder(
                        p.getName(),
                        p.getWidth(),
                        p.getVolume(),
                        p.getLength(),
                        p.getHeight())
                ).collect(Collectors.toList());
    }
}
