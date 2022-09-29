package sigma.tailoring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.entities.*;
import sigma.tailoring.entities.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TailoringOrderList {
    private final List<TailoringOrderList.Order> orderList;

    public TailoringOrderList(List<TailoringOrder> tailoringOrders, String directory) {
        this.orderList = tailoringOrders
                .stream()
                .map(tailoringOrder -> this.toOrder(tailoringOrder, directory))
                .collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        private Long id;
        private String addressForSend;
        private String orderDescription;
        private OrderStatus status;
        private OrderPaymentStatus paymentStatus;
        private LocalDateTime dateOfCreation;
        private Long templateId;
        private LocalDate endDate;
        private Integer cost;
        private Integer countOfOrder;
        private Material material;
        private Color color;
        private ClientOrder clientOrder;
        private List<File> images;
        private List<OrderPart> partSizes;

        public boolean isFromTemplate() {
            return this.templateId != null;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientOrder {
        private Long id;
        private String phoneNumber;
        private String city;
        private String country;
        private String firstname;
        private Boolean isMale;

        public ClientOrder(CustomerOrder customerOrder) {
            this.id = customerOrder.getId();
            this.phoneNumber = customerOrder.getPhoneNumber();
            this.city = customerOrder.getCity();
            this.country = customerOrder.getCountry();
            this.firstname = customerOrder.getFirstname();
            this.isMale = customerOrder.isMale();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderPart {
        private String name;
        private Integer width;
        private Integer volume;
        private Integer length;
        private Integer height;

        public OrderPart(PartSizeOrder partSizeOrder) {
            this.name = partSizeOrder.getName();
            this.width = partSizeOrder.getWidth();
            this.volume = partSizeOrder.getVolume();
            this.length = partSizeOrder.getLength();
            this.height = partSizeOrder.getHeight();
        }
    }

    private Order toOrder(TailoringOrder tailoringOrder, String directory) {
        Order order = new Order();

        order.id = tailoringOrder.getId();
        order.addressForSend = tailoringOrder.getAddressForSend();
        order.orderDescription = tailoringOrder.getOrderDescription();
        order.status = tailoringOrder.getStatus();
        order.paymentStatus = tailoringOrder.getPaymentStatus();
        order.dateOfCreation = tailoringOrder.getDateOfCreation();
        order.templateId = tailoringOrder.getTemplateId();
        order.endDate = tailoringOrder.getEndDate();
        order.cost = tailoringOrder.getCost();
        order.countOfOrder = tailoringOrder.getCountOfOrder();
        order.material = tailoringOrder.getMaterial();
        order.color = tailoringOrder.getColor();
        order.clientOrder = new ClientOrder(tailoringOrder.getCustomerOrder());

        order.images = tailoringOrder.getImages()
                .stream()
                .map(img -> new File(directory, img.getName()))
                .collect(Collectors.toList());

        order.partSizes = tailoringOrder.getPartSizes()
                .stream()
                .map(OrderPart::new)
                .collect(Collectors.toList());

        return order;
    }


}
