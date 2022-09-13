package sigma.nure.tailoring.tailoring.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdsOrderBasedOnTemplateRepositoryJdbcTemplate implements IdsOrderBasedOnTemplateRepository {

    private static final String SELECT = "SELECT user_order_id AS orderId,tailoring_templates_id AS templateId FROM template_orders;";

    private final JdbcTemplate jdbc;

    public IdsOrderBasedOnTemplateRepositoryJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Map<Long, List<Long>> findIdsForTemplatesAndOrdersBasedOnTemplate() {

        var mapFromDb = jdbc.queryForList(SELECT);
        Map<Long, List<Long>> result = new HashMap<>();


        mapFromDb.forEach(line -> {
            long templateId = Long.parseLong(line.get("templateId").toString());
            if (result.get(templateId) == null) {
                result.put(templateId, new ArrayList<>());
            }

            result.get(templateId).add(Long.valueOf(line.get("orderId").toString()));

        });

        return result;
    }
}
