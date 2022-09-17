package sigma.nure.tailoring.tailoring.repository;

import com.google.gson.Gson;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sigma.nure.tailoring.tailoring.entities.PartSizeForTemplate;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.*;

@Repository
public class JdbcTemplatePostgresTailoringTemplateRepository implements TailoringTemplateRepository {

    private static final String SELECT_TEMPLATE = "" +
            "SELECT t.id, t.name, t.active, t.date_of_creation AS dateOfCreation, t.type_template AS typeTemplate, \n" +
            "t.image_names AS imagesUrlParam, t.colour_ids AS colorIdsParam, t.cost, t.material_ids AS materialIdsParam, \n" +
            "t.template_part_sizes AS partSizes, t.template_description AS templateDescription \n" +
            "FROM tailoring_templates t, json_array_elements(t.colour_ids) colId,json_array_elements(t.material_ids) AS matId\n " +
            "WHERE (:areTemplateIdsNull::boolean OR t.id IN(:templateIds::bigint))\n" +
            "AND (:name::varchar IS NULL OR t.name LIKE CONCAT ('%',:name::varchar, '%'))\n" +
            "AND (:areTypeTemplatesNull::boolean OR t.type_template IN(:typeTemplates))\n" +
            "AND (:isActive::boolean IS NULL OR t.active = :isActive)\n" +
            "AND (:startCost::int IS NULL OR t.cost >= :startCost::int)\n" +
            "AND (:endCost::int IS NULL OR t.cost <= :endCost::int)\n" +
            "AND (:startDateOfCreation::timestamp IS NULL OR t.date_of_creation >= :startDateOfCreation::timestamp)\n" +
            "AND (:endDateOfCreation::timestamp IS NULL OR t.date_of_creation <= :endDateOfCreation::timestamp)\n" +
            "group by id\n " +
            " HAVING (:areColorIdsNull::boolean OR array_agg(colId::text::int) && :containsColorIds )\n" +
            "AND (:areMaterialIdsNull::boolean OR array_agg(matId::text::int) && :containsMaterialIds )\n" +
            " ORDER BY :sortColumn :sortDirection LIMIT :limit OFFSET :offset ";

    private static final String SELECT_TYPES_ORDER = "SELECT DISTINCT type_template FROM tailoring_templates";

    private static final String SAVE = "INSERT INTO tailoring_templates(name, active, date_of_creation, cost, type_template," +
            " image_names, colour_ids, material_ids, template_part_sizes, template_description) \n" +
            "VALUES (:name,:active,:dateOfCreation,:cost,:typeTemplate,:imageUrl::json,:colorIds::json,:materialIds::json,:partSize::json,:description)";

    private static final String UPDATE = "UPDATE tailoring_templates SET name = :name, active = :active, date_of_creation = :dateOfCreation, \n" +
            "cost = :cost, type_template = :typeTemplate, image_names = :imageUrl::json, colour_ids = :colorIds::json, \n" +
            "material_ids = :materialIds::json, template_part_sizes = :partSize::json, template_description = :description \n" +
            "WHERE id = :id";

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
    private final RowMapper<TailoringTemplateWithMaterialIds> templateRowMapper;
    private final Gson gson;
    private final RepositoryHandler handler;

    public JdbcTemplatePostgresTailoringTemplateRepository(JdbcTemplate jdbc, RepositoryHandler handler) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.handler = handler;
        this.gson = new Gson();
        this.templateRowMapper = this.generateRowMapper();
    }

    @Override
    public List<TailoringTemplateWithMaterialIds> findBy(TailoringTemplateSearchCriteria criteria, Page page) {
        String script = this.setParametersInsideScript(
                SELECT_TEMPLATE,
                criteria.getColorIds(),
                criteria.getMaterialIds(),
                page
        );

        checkIterableCriteria(criteria);

        Map<String, Object> args = new HashMap<>();

        args.put("areTemplateIdsNull", criteria.getTemplateIds() == null);
        args.put("templateIds", criteria.getTemplateIds());
        args.put("areTypeTemplatesNull", criteria.getTypeTemplates() == null);
        args.put("typeTemplates", criteria.getTypeTemplates());
        args.put("areColorIdsNull", criteria.getColorIds() == null);
        args.put("areMaterialIdsNull", criteria.getMaterialIds() == null);

        args.put("name", criteria.getName());
        args.put("isActive", criteria.getIsActive());

        args.put("startCost", Optional.ofNullable(criteria.getCost())
                .map(c -> c.getFrom()).orElse(null));
        args.put("endCost", Optional.ofNullable(criteria.getCost())
                .map(c -> c.getTo()).orElse(null));
        args.put("startDateOfCreation", Optional.ofNullable(criteria.getDateOfCreation())
                .map(date -> date.getFrom()).orElse(null));
        args.put("endDateOfCreation", Optional.ofNullable(criteria.getDateOfCreation())
                .map(date -> date.getTo()).orElse(null));

        return namedJdbc.query(script, args, templateRowMapper);
    }

    @Override
    public Set<String> findAllTypeTemplate() {
        return new HashSet<>(jdbc.queryForList(SELECT_TYPES_ORDER, String.class));
    }

    @Override
    public boolean save(TailoringTemplateWithMaterialIds t) {
        Map<String, Object> args = getArgsWithoutId(t);
        return namedJdbc.update(SAVE, args) != 0;
    }

    @Override
    public boolean update(TailoringTemplateWithMaterialIds t) {
        Map<String, Object> args = getArgsWithoutId(t);

        args.put("id", t.getId());
        return namedJdbc.update(UPDATE, args) != 0;
    }

    private RowMapper<TailoringTemplateWithMaterialIds> generateRowMapper() {
        RowMapper<TailoringTemplateWithMaterialIds> templateRowMapper =
                new BeanPropertyRowMapper(TailoringTemplateWithMaterialIds.class);
        return (r, i) -> {
            var template = templateRowMapper.mapRow(r, i);

            template.setImagesUrl(new HashSet<>(
                    Set.of(gson.fromJson(
                            r.getString("imagesUrlParam"),
                            String[].class)
                    )));
            template.setPartSizeForTemplates(new HashSet<>(
                    Set.of(gson.fromJson(
                            r.getString("partSizes"),
                            PartSizeForTemplate[].class)
                    )));
            template.setColorIds(new HashSet<>(
                    Set.of(gson.fromJson(
                            r.getString("colorIdsParam"),
                            Integer[].class)
                    )));
            template.setMaterialIds(new HashSet<>(
                    Set.of(gson.fromJson(
                            r.getString("materialIdsParam"),
                            Integer[].class)
                    )));

            return template;
        };
    }

    private void checkIterableCriteria(TailoringTemplateSearchCriteria criteria) {
        criteria.setTemplateIds(handler.getNullIfCollectionNullOrEmpty(criteria.getTemplateIds()));
        criteria.setColorIds(handler.getNullIfCollectionNullOrEmpty(criteria.getColorIds()));
        criteria.setMaterialIds(handler.getNullIfCollectionNullOrEmpty(criteria.getMaterialIds()));
        criteria.setTypeTemplates(handler.getNullIfCollectionNullOrEmpty(criteria.getTypeTemplates()));
    }

    private String setParametersInsideScript(String script, Iterable<Integer> colorIds, Iterable<Integer> materialIds, Page page) {
        return script
                .replaceFirst(":containsColorIds", getConditionForJsonArray(colorIds))
                .replaceFirst(":containsMaterialIds", getConditionForJsonArray(materialIds))
                .replaceFirst(":sortColumn", page.getOrderByOrDefault("date_of_creation"))
                .replaceFirst(":sortDirection", page.getDirectionOrDefault(Page.Direction.DESC).toString())
                .replaceFirst(":limit", page.getLimitOrDefault(100L).toString())
                .replaceFirst(":offset", page.getOffsetOrDefault(0L).toString());
    }

    private String getConditionForJsonArray(Iterable<?> iterable) {
        var iterableString = handler.getStringIterableFromOtherTypeIterable(iterable);

        return "'{" + String.join(",", iterableString == null ? List.of() : iterableString) + "}'";
    }

    private Map<String, Object> getArgsWithoutId(TailoringTemplateWithMaterialIds t) {
        Map<String, Object> args = new HashMap<>();

        args.put("name", t.getName());
        args.put("active", t.isActive());
        args.put("dateOfCreation", t.getDateOfCreation());
        args.put("cost", t.getCost());
        args.put("typeTemplate", t.getTypeTemplate());
        args.put("imageUrl", gson.toJson(t.getImagesUrl()));
        args.put("colorIds", gson.toJson(t.getColorIds()));
        args.put("materialIds", gson.toJson(t.getMaterialIds()));
        args.put("partSize", gson.toJson(t.getPartSizeForTemplates()));
        args.put("description", t.getTemplateDescription());
        return args;
    }
}
