package sigma.tailoring.converters;

import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;
import sigma.tailoring.dto.BuyerOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.service.TailoringTemplateService;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.TailoringTemplateSearchCriteria;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BuyerOrderConverter {
    private static final DateTimeFormatter DATA_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final TailoringTemplateService templateService;

    public BuyerOrderConverter(TailoringTemplateService templateService) {
        this.templateService = templateService;
    }

    public List<BuyerOrder> convert(TailoringOrderList orderList) {
        var templateImages = this.toTemplateImages();

        return orderList
                .getOrderList()
                .stream()
                .map(o -> this.toBuyerOrder(templateImages, o))
                .collect(Collectors.toList());
    }

    private BuyerOrder toBuyerOrder(Map<Long, String> templateImages, TailoringOrderList.Order order) {
        BuyerOrder buyerOrder = new BuyerOrder();

        buyerOrder.setId(order.getId());
        buyerOrder.setDateOfCreation(order.getDateOfCreation().format(DATA_TIME_FORMAT));
        buyerOrder.setStatus(order.getStatus().name());
        buyerOrder.setPaymentStatus(order.getPaymentStatus().name());
        buyerOrder.setCost(order.getCost());
        buyerOrder.setImg(
                templateImages.getOrDefault(order.getTemplateId(),
                        this.toImage(order)
                )
        );
        buyerOrder.setIsFromTemplate(order.isFromTemplate());

        return buyerOrder;
    }

    private Map<Long, String> toTemplateImages() {
        return templateService.findBy(
                        TailoringTemplateSearchCriteria.builder()
                                .build(),
                        new Page()
                )
                .stream()
                .collect(Collectors
                        .toMap(
                                t -> t.getId(),
                                t -> t.getImagesUrl()
                                        .stream()
                                        .filter(img -> img.contains("front-"))
                                        .findFirst()
                                        .orElseThrow()
                        ));
    }

    private String toImage(TailoringOrderList.Order order) {
        return order
                .getImages()
                .stream()
                .findFirst()
                .map(f -> f.getName())
                .orElse("Error.jpg");
    }


}
