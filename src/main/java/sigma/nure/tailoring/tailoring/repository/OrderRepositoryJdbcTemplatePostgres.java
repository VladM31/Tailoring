package sigma.nure.tailoring.tailoring.repository;

import com.google.gson.Gson;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import sigma.nure.tailoring.tailoring.entities.*;
import sigma.nure.tailoring.tailoring.tools.OrderParameters;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryJdbcTemplatePostgres implements OrderRepository{

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
            "         INNER JOIN material m on m.id = o.material_id;";

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
    private final RowMapper<TailoringOrder> rowMapper;
    private final RepositoryHandler handler;
    private final Gson jsonConvector;

    public OrderRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc,RepositoryHandler handler) {
        this.jdbc = jdbc;
        this.handler = handler;
        this.jsonConvector = new Gson();
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.rowMapper = getRowMapper();
    }

    private RowMapper<TailoringOrder> getRowMapper(){
        final var rowMapperForNotJson = new BeanPropertyRowMapper<>(TailoringOrder.class);
        final var rowMapperShortUserData = new BeanPropertyRowMapper<>(ShortUserData.class);
        return  (r,i) -> {
            TailoringOrder order = rowMapperForNotJson.mapRow(r,i);
            order.setId(r.getLong("orderId"));

            order.setUserData(rowMapperShortUserData.mapRow(r,i));
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

            order.setImages(jsonConvector.fromJson(r.getString("imagesJson"),List.class));
            order.setPartSizes(jsonConvector.fromJson(r.getString("partSizesJson"),List.class));

            return order;
        };


    }


    @Override
    public List<TailoringOrder> findBy(OrderParameters parameters, Page page) {
        return jdbc.query(SELECT_ORDER,rowMapper);
    }

    @Override
    public boolean save(TailoringOrder order, Long templateId) {
        return false;
    }

    @Override
    public boolean update(TailoringOrder order) {
        return false;
    }

    @Override
    public List<ShortTailoringOrderData> findShortDataByUserId(Long userId) {
        return null;
    }

}
