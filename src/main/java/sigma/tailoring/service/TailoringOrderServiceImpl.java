package sigma.tailoring.service;

import org.springframework.web.multipart.MultipartFile;
import sigma.tailoring.converters.FileConverter;
import sigma.tailoring.converters.OrderServiceSortColumnConverter;
import sigma.tailoring.dto.ModifyTailoringOrder;
import sigma.tailoring.dto.TailoringOrderList;
import sigma.tailoring.entities.*;
import sigma.tailoring.repository.OrderRepository;
import sigma.tailoring.tools.OrderSearchCriteria;
import sigma.tailoring.tools.Page;
import sigma.tailoring.entities.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TailoringOrderServiceImpl implements TailoringOrderService {
    private static final String FILE_NAME_TEMPLATE = "order_img_%d_customer_%d_%d_%s";
    private static final Function<ModifyTailoringOrder, Function<String, String>> GET_FILE_NAME_GENERATOR = (order) -> {
        return (name) -> FILE_NAME_TEMPLATE
                .formatted(
                        order.getId(),
                        order.getCustomerId(),
                        System.currentTimeMillis(),
                        name.replaceAll("[:;, \'\"]", "_")
                );
    };

    private final OrderServiceSortColumnConverter sortColumnConverter;
    private final OrderRepository orderRepository;
    private final FileConverter fileConverter;
    private final String orderImageDirectory;

    public TailoringOrderServiceImpl(OrderServiceSortColumnConverter sortColumnConverter,
                                     OrderRepository orderRepository, FileConverter fileConverter, String orderImageDirectory) {
        this.sortColumnConverter = sortColumnConverter;

        this.orderRepository = orderRepository;
        this.fileConverter = fileConverter;
        this.orderImageDirectory = orderImageDirectory;
    }

    @Override
    public TailoringOrderList findBy(OrderSearchCriteria orderSearchCriteria, Page page) {
        page.setOrderBy(sortColumnConverter.convert(page.getOrderBy()));
        return new TailoringOrderList(orderRepository.findBy(orderSearchCriteria, page), orderImageDirectory);
    }

    @Override
    public boolean save(ModifyTailoringOrder order) {
        TailoringOrder tailoringOrder = this.toTailoringOrder(order);
        tailoringOrder.setDateOfCreation(LocalDateTime.now());
        return orderRepository.save(tailoringOrder).isPresent();
    }

    @Override
    public boolean update(ModifyTailoringOrder order) {
        return orderRepository.update(this.toTailoringOrder(order));
    }


    private TailoringOrder toTailoringOrder(ModifyTailoringOrder order) {
        if (!order.getUploadImages().isEmpty()) {
            List<File> files = fileConverter.toFiles(orderImageDirectory, order.getUploadImages(), GET_FILE_NAME_GENERATOR.apply(order));
            order.setImages(this.toImages(files, order.getUploadImages()));
        }

        TailoringOrder tailoringOrder = new TailoringOrder();

        tailoringOrder.setAddressForSend(order.getAddressForSend());
        tailoringOrder.setOrderDescription(order.getOrderDescription());
        tailoringOrder.setStatus(order.getStatus());
        tailoringOrder.setPaymentStatus(order.getPaymentStatus());
        tailoringOrder.setTemplateId(order.getTemplateId());
        tailoringOrder.setEndDate(order.getEndDate());
        tailoringOrder.setCost(order.getCost());
        tailoringOrder.setCountOfOrder(order.getCountOfOrder());
        tailoringOrder.setMaterial(new Material(order.getMaterialId()));
        tailoringOrder.setColor(new Color(order.getColorId()));
        tailoringOrder.setCustomerOrder(new CustomerOrder(order.getCustomerId()));
        tailoringOrder.setPartSizes(order.getPartSizes());
        tailoringOrder.setImages(order.getImages());
        tailoringOrder.setId(order.getId());

        return tailoringOrder;
    }

    private List<Image> toImages(List<File> files, List<MultipartFile> uploadFiles) {
        List<Image> images = new ArrayList<>(files.size());
        for (int i = 0, size = files.size(); i < size; i++) {
            images.add(new Image(
                    files.get(i).getName(),
                    uploadFiles.get(i).getOriginalFilename()));
        }
        return images;
    }
}
