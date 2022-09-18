package sigma.nure.tailoring.tailoring.service;

import org.springframework.scheduling.annotation.Scheduled;
import sigma.nure.tailoring.tailoring.entities.*;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.tools.OrderSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class PopularTemplateServiceImpl implements PopularTemplateService {
    private static final Long DELAY_BEFORE_NEW_CALCULATION_IN_MICROSECOND = 21_600_000L;
    private static final Long ALL_ORDERS = Long.MAX_VALUE;
    private static final Long ALL_TEMPLATES = Long.MAX_VALUE;
    private static final Long EMPTY_VALUE = 0L;
    private static final boolean MAN = true;
    private static final boolean WOMAN = false;
    private static final double IMPORTANCE_COEFFICIENT_FOR_SIZE_ORDERS = 0.7;
    private static final double IMPORTANCE_COEFFICIENT_FOR_POPULAR_GENDER = 0.5;
    private static final double IMPORTANCE_COEFFICIENT_FOR_NOT_POPULAR_GENDER = 0.3;
    
    private final OrderRepository orderRepository;
    private final TailoringTemplateRepository templateRepository;
    private List<PopularTailoringTemplate> popularTemplates;

    public PopularTemplateServiceImpl(OrderRepository orderRepository, TailoringTemplateRepository templateRepository) {
        this.orderRepository = orderRepository;
        this.templateRepository = templateRepository;
        calculateTemplate();
    }

    @Override
    public List<PopularTailoringTemplate> getPopularTailoringTemplate() {
        return popularTemplates;
    }

    @Scheduled(initialDelay = DELAY_BEFORE_NEW_CALCULATION_IN_MICROSECOND,
            fixedDelay = DELAY_BEFORE_NEW_CALCULATION_IN_MICROSECOND)
    private void calculateTemplate() {
        var templates = getTemplates();

        var orders = getOrdersByTemplates(templates);

        final boolean isPopularMale = calculatePopularGender(orders);

        var ordersGroupByTemplateIdAndCustomerGender = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getTemplateId(),
                        Collectors.groupingBy(order -> order.getCustomerOrder().isMale(), Collectors.counting())
                ));

        this.popularTemplates = templates.stream()
                .sorted(createComparator(isPopularMale, ordersGroupByTemplateIdAndCustomerGender))
                .collect(this.mappingToPopularTemplateList());
    }

    private List<TailoringTemplateWithMaterialIds> getTemplates() {
        return templateRepository.
                findBy(TailoringTemplateSearchCriteria.builder()
                                .isActive(true)
                                .build(),
                        Page.builder()
                                .limit(ALL_TEMPLATES)
                                .build()
                );
    }

    private boolean calculatePopularGender(List<TailoringOrder> orders) {
        var countGenders = orders.stream().collect(
                Collectors.groupingBy(o -> o.getCustomerOrder().isMale(),
                        Collectors.counting())
        );

        return countGenders.getOrDefault(MAN, EMPTY_VALUE) > countGenders.getOrDefault(WOMAN, EMPTY_VALUE);
    }

    private List<TailoringOrder> getOrdersByTemplates(List<TailoringTemplateWithMaterialIds> templates) {
        List<Long> templateIds = templates
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        return orderRepository.findBy(
                OrderSearchCriteria.builder()
                        .templateIds(templateIds)
                        .orderStatuses(List.of(OrderStatus.DONE, OrderStatus.EXECUTED))
                        .paymentStatuses(List.of(OrderPaymentStatus.PAYMENTED, OrderPaymentStatus.PAYMENTED_HALF))
                        .build(),
                Page.builder()
                        .limit(ALL_ORDERS)
                        .build());
    }

    private Map<Long, Double> getTemplateIdWithImportanceCoefficient(boolean isPopularMale, Map<Long, Map<Boolean, Long>> processedData) {
        Map<Long, Double> map = new HashMap<>();

        processedData.forEach((k, v) -> {
            final double countOrderWithCoefficient = (v.getOrDefault(MAN, EMPTY_VALUE) + v.getOrDefault(WOMAN, EMPTY_VALUE)) * IMPORTANCE_COEFFICIENT_FOR_SIZE_ORDERS;
            final double countPopularGenderWithCoefficient = v.getOrDefault(isPopularMale, EMPTY_VALUE) * IMPORTANCE_COEFFICIENT_FOR_POPULAR_GENDER;
            final double countNotPopularGenderWithCoefficient = v.getOrDefault(!isPopularMale, EMPTY_VALUE) * IMPORTANCE_COEFFICIENT_FOR_NOT_POPULAR_GENDER;

            map.put(k, countOrderWithCoefficient + countPopularGenderWithCoefficient + countNotPopularGenderWithCoefficient);
        });

        return map;
    }

    private Comparator<TailoringTemplateWithMaterialIds> createComparator(boolean isPopularMale, Map<Long, Map<Boolean, Long>> processedData) {
        final var importanceCoefficient = getTemplateIdWithImportanceCoefficient(isPopularMale, processedData);

        return (first, second) -> Double.compare(
                importanceCoefficient.getOrDefault(second.getId(), EMPTY_VALUE.doubleValue()),
                importanceCoefficient.getOrDefault(first.getId(), EMPTY_VALUE.doubleValue())
        );
    }

    private Collector<TailoringTemplateWithMaterialIds, ?, List<PopularTailoringTemplate>> mappingToPopularTemplateList() {
        return Collectors.mapping(template ->
                new PopularTailoringTemplate(
                        template.getId(),
                        template.getName(),
                        Collections.unmodifiableSet(template.getImagesUrl())
                ), Collectors.toUnmodifiableList());
    }
}
