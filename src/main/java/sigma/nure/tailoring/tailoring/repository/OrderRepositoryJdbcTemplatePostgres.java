package sigma.nure.tailoring.tailoring.repository;

import com.google.gson.Gson;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import sigma.nure.tailoring.tailoring.entities.*;
import sigma.nure.tailoring.tailoring.tools.OrderSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.*;

@Repository
public class OrderRepositoryJdbcTemplatePostgres implements OrderRepository {

    private static final String SELECT_ORDER =
            "SELECT o.id AS orderId, o.address_for_send AS addressForSend,\n" +
                    "       o.order_description AS orderDescription, o.order_status AS status, o.order_payment_status AS paymentStatus,\n" +
                    "       o.date_of_creation AS dateOfCreation, o.is_from_template AS fromTemplate, o.end_date AS endDate,\n" +
                    "       o.cost, o.count_of_order AS countOfOrder, o.user_id AS userId, u.phone_number AS phoneNumber,\n" +
                    "       u.city, u.country, u.firstname, u.male,\n" +
                    "       m.id AS materialId , m.name AS materialName, m.cost_one_square_meter AS materialCost,\n" +
                    "       c.id AS colorId, c.name AS colorName, c.color_code AS code,\n" +
                    "       o.images AS imagesJson, o.part_sizes AS partSizesJson\n" +
                    "FROM tailoring_order o\n" +
                    "         LEFT JOIN \"user\" u on u.id = o.user_id\n" +
                    "         INNER JOIN color c on c.id = o.color_id\n" +
                    "         INNER JOIN material m on m.id = o.material_id\n" +
                    "WHERE \n" +
                    "(:orderIdsAreNull OR o.id IN(:orderIds::bigint)) AND \n" +
                    "(:materialIdsAreNull OR o.material_id IN(:materialIds::int)) AND \n" +
                    "(:colorIdsAreNull OR o.color_id IN(:colorIds::int)) AND \n" +
                    "(:userIdsAreNull OR o.user_id IN(:userIds::bigint)) AND \n" +
                    "(:orderStatusesAreNull OR o.order_status IN(:orderStatuses)) AND \n" +
                    "(:paymentStatusesAreNull OR o.order_payment_status IN(:paymentStatuses)) AND \n" +
                    "(:address::varchar IS NULL OR o.address_for_send LIKE CONCAT ('%',:address::varchar, '%')) AND \n" +
                    "(:phoneNumber::varchar IS NULL OR u.phone_number LIKE CONCAT ('%',:phoneNumber::varchar, '%')) AND \n" +
                    "(:city::varchar IS NULL OR u.city LIKE CONCAT ('%',:city::varchar, '%')) AND \n" +
                    "(:country::varchar IS NULL OR u.country LIKE CONCAT ('%',:country::varchar, '%')) AND \n" +
                    "(:firstname::varchar IS NULL OR u.firstname LIKE CONCAT ('%',:firstname::varchar, '%')) AND \n" +
                    "(:beforeOrEqualsEndDate::date IS NULL OR o.end_date <= :beforeOrEqualsEndDate::date) AND \n" +
                    "(:afterOrEqualsEndDate::date IS NULL OR o.end_date >= :afterOrEqualsEndDate::date) AND \n" +
                    "(:beforeOrEqualsDateOfCreation::timestamp IS NULL OR o.date_of_creation <= :beforeOrEqualsDateOfCreation::timestamp) AND \n" +
                    "(:afterOrEqualsDareOfCreation::timestamp IS NULL OR o.date_of_creation >= :afterOrEqualsDareOfCreation::timestamp) AND \n" +
                    "(:userIsMale::boolean IS NULL OR u.male = :userIsMale::boolean) AND \n" +
                    "(:isFromTemplate::boolean IS NULL OR o.is_from_template = :isFromTemplate::boolean) AND \n" +
                    "(:greatOrEqualsCost::int IS NULL OR o.cost >= :greatOrEqualsCost::int) AND \n" +
                    "(:lessOrEqualsCost::int IS NULL OR o.cost <= :lessOrEqualsCost::int) AND \n" +
                    "(:greatOrEqualsCount::int IS NULL OR o.count_of_order >= :greatOrEqualsCount::int) AND \n" +
                    "(:lessOrEqualsCount::int IS NULL OR o.count_of_order <= :lessOrEqualsCount::int) \n";

    private static final String PIN_TO_TEMPLATE = "INSERT INTO template_orders(tailoring_order_id,tailoring_templates_id) VALUES(?,?)";

    private static final String UPDATE = "UPDATE tailoring_order SET " +
            "address_for_send = :addressForSend, order_description = :orderDescription, " +
            "order_status = :status, order_payment_status = :paymentStatus, " +
            "is_from_template = :isFromTemplate, end_date = :endDate, " +
            "cost = :theCost, count_of_order = :countOfOrder, " +
            "material_id = :materialId, color_id = :colorId, user_id = :userId, " +
            "part_sizes = :partSizes::json, images = :theImages::json " +
            "WHERE id = :orderId";

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
    private final RowMapper<TailoringOrder> rowMapper;
    private final RepositoryHandler handler;
    private final SimpleJdbcInsert insertOrder;
    private final Gson jsonConvector;

    public OrderRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc, RepositoryHandler handler) {
        this.jdbc = jdbc;
        this.handler = handler;
        this.jsonConvector = new Gson();
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.rowMapper = getRowMapper();
        this.insertOrder = new SimpleJdbcInsert(jdbc.getDataSource())
                .withTableName("tailoring_order")
                .usingGeneratedKeyColumns("id")
                .usingColumns("address_for_send", "order_description", "order_status", "order_payment_status",
                        "date_of_creation", "is_from_template", "part_sizes", "images", "end_date", "cost",
                        "count_of_order", "user_id", "material_id", "color_id");
    }

    @Override
    public List<TailoringOrder> findBy(OrderSearchCriteria parameters, Page page) {
        String scriptSelect = SELECT_ORDER + handler.getScriptFromPage(page, "o.date_of_creation", Page.Direction.DESC, 100L, 0L);

        checkCollectionParameters(parameters);

        Map<String, Object> paramForFiltering = new HashMap<>();

        paramForFiltering.put("orderIdsAreNull", parameters.getOrderIds() == null);
        paramForFiltering.put("orderIds", parameters.getOrderIds());
        paramForFiltering.put("materialIdsAreNull", parameters.getMaterialIds() == null);
        paramForFiltering.put("materialIds", parameters.getMaterialIds());
        paramForFiltering.put("colorIdsAreNull", parameters.getColorIds() == null);
        paramForFiltering.put("colorIds", parameters.getColorIds());
        paramForFiltering.put("userIdsAreNull", parameters.getUserIds() == null);
        paramForFiltering.put("userIds", parameters.getUserIds());
        paramForFiltering.put("orderStatusesAreNull", parameters.getOrderStatuses() == null);
        paramForFiltering.put("orderStatuses", handler.getStringIterableFromEnumIterable(parameters.getOrderStatuses()));
        paramForFiltering.put("paymentStatusesAreNull", parameters.getPaymentStatuses() == null);
        paramForFiltering.put("paymentStatuses", handler.getStringIterableFromEnumIterable(parameters.getPaymentStatuses()));

        paramForFiltering.put("address", parameters.getAddress());
        paramForFiltering.put("phoneNumber", parameters.getPhoneNumber());
        paramForFiltering.put("city", parameters.getCity());
        paramForFiltering.put("country", parameters.getCountry());
        paramForFiltering.put("firstname", parameters.getFirstname());

        paramForFiltering.put("beforeOrEqualsEndDate", parameters.getBeforeOrEqualsEndDate());
        paramForFiltering.put("afterOrEqualsEndDate", parameters.getAfterOrEqualsEndDate());
        paramForFiltering.put("beforeOrEqualsDateOfCreation", parameters.getBeforeOrEqualsDateOfCreation());
        paramForFiltering.put("afterOrEqualsDareOfCreation", parameters.getAfterOrEqualsDareOfCreation());

        paramForFiltering.put("userIsMale", parameters.getUserIsMale());
        paramForFiltering.put("isFromTemplate", parameters.getIsFromTemplate());
        paramForFiltering.put("greatOrEqualsCost", parameters.getGreatOrEqualsCost());
        paramForFiltering.put("lessOrEqualsCost", parameters.getLessOrEqualsCost());
        paramForFiltering.put("greatOrEqualsCount", parameters.getGreatOrEqualsCount());
        paramForFiltering.put("lessOrEqualsCount", parameters.getLessOrEqualsCount());

        return namedJdbc.query(scriptSelect, paramForFiltering, rowMapper);
    }

    @Override
    public Optional<Long> save(TailoringOrder order) {
        Map<String, Object> args = new HashMap<>();

        args.put("address_for_send", order.getAddressForSend());
        args.put("order_description", order.getOrderDescription());
        args.put("order_status", order.getStatus().name());
        args.put("order_payment_status", order.getPaymentStatus().name());
        args.put("date_of_creation", order.getDateOfCreation());
        args.put("is_from_template", order.isFromTemplate());
        args.put("part_sizes", this.jsonConvector.toJson(order.getPartSizes()));
        args.put("images", this.jsonConvector.toJson(order.getImages()));
        args.put("end_date", order.getEndDate());
        args.put("cost", order.getCost());
        args.put("count_of_order", order.getCountOfOrder());
        args.put("user_id", order.getUserData().getId());
        args.put("material_id", order.getMaterial().getId());
        args.put("color_id", order.getColor().getId());

        Long id = (Long) insertOrder.executeAndReturnKey(args);

        return Optional.ofNullable(id);
    }

    @Override
    public boolean pinToTemplate(Long orderId, Long templateId) {
        return jdbc.update(PIN_TO_TEMPLATE, orderId, templateId) != 0;
    }

    @Override
    public boolean update(TailoringOrder order) {
        Map<String, Object> args = new HashMap<>();

        args.put("addressForSend", order.getAddressForSend());
        args.put("orderDescription", order.getOrderDescription());
        args.put("status", order.getStatus().name());
        args.put("paymentStatus", order.getPaymentStatus().name());
        args.put("isFromTemplate", order.isFromTemplate());
        args.put("endDate", order.getEndDate());
        args.put("theCost", order.getCost());
        args.put("countOfOrder", order.getCountOfOrder());
        args.put("materialId", order.getMaterial().getId());
        args.put("colorId", order.getColor().getId());
        args.put("userId", order.getUserData().getId());
        args.put("partSizes", this.jsonConvector.toJson(order.getPartSizes()));
        args.put("theImages", this.jsonConvector.toJson(order.getImages()));
        args.put("orderId", order.getId());

        return this.namedJdbc.update(UPDATE, args) != 0;
    }

    private RowMapper<TailoringOrder> getRowMapper() {
        final var rowMapperForNotJson = new BeanPropertyRowMapper<>(TailoringOrder.class);
        final var rowMapperShortUserData = new BeanPropertyRowMapper<>(ShortUserData.class);
        return (r, i) -> {
            TailoringOrder order = rowMapperForNotJson.mapRow(r, i);
            order.setId(r.getLong("orderId"));

            order.setUserData(rowMapperShortUserData.mapRow(r, i));
            order.getUserData().setId(r.getLong("userId"));

            Color color = new Color();
            color.setId(r.getInt("colorId"));
            color.setName(r.getString("colorName"));
            color.setCode("code");
            order.setColor(color);

            Material material = new Material();
            material.setId(r.getInt("materialId"));
            material.setName(r.getString("materialName"));
            material.setCost(r.getInt("materialCost"));
            order.setMaterial(material);

            order.setImages(new ArrayList<>(List.of(jsonConvector.fromJson(r.getString("imagesJson"), Image[].class))));
            order.setPartSizes(new ArrayList<>(List.of(jsonConvector.fromJson(r.getString("partSizesJson"), PartSizeOrder[].class))));

            return order;
        };
    }

    private void checkCollectionParameters(OrderSearchCriteria params) {
        params.setOrderIds(handler.getNullIfCollectionNullOrEmpty(params.getOrderIds()));
        params.setMaterialIds(handler.getNullIfCollectionNullOrEmpty(params.getMaterialIds()));
        params.setColorIds(handler.getNullIfCollectionNullOrEmpty(params.getColorIds()));
        params.setUserIds(handler.getNullIfCollectionNullOrEmpty(params.getUserIds()));
        params.setOrderStatuses(handler.getNullIfCollectionNullOrEmpty(params.getOrderStatuses()));
        params.setPaymentStatuses(handler.getNullIfCollectionNullOrEmpty(params.getPaymentStatuses()));
    }
}
