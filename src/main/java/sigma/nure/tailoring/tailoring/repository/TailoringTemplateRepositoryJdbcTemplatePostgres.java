package sigma.nure.tailoring.tailoring.repository;

import com.google.gson.Gson;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import sigma.nure.tailoring.tailoring.entities.PartSizeForTemplate;
import sigma.nure.tailoring.tailoring.entities.TailoringTemplateWithMaterialIds;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.TailoringTemplateSearchCriteria;

import java.util.*;

public class TailoringTemplateRepositoryJdbcTemplatePostgres implements TailoringTemplateRepository {

    private static final String SELECT_TEMPLATE = "" +
            "SELECT t.id, t.name, t.active, t.date_registration AS dateOfCreation, t.type_template AS typeTemplate, \n" +
            "t.image_names AS imagesUrlParam, t.colour_ids AS colorIdsParam, t.cost, t.material_ids AS materialIdsParam, \n" +
            "t.template_part_sizes AS partSizes, t.template_description AS templateDescription \n" +
            "FROM tailoring_templates t, json_array_elements(t.colour_ids) colId,json_array_elements(t.material_ids) AS matId\n " +
            "WHERE (:areTemplateIdsNull OR t.id IN(:templateIds::bigint))\n" +
            "AND (:name::varchar IS NULL OR t.name LIKE CONCAT ('%',:name::varchar, '%'))\n" +
            "AND (:areTypeTemplatesNull OR t.type_template IN(:typeTemplates))\n" +
            "AND (:isActive IS NULL OR t.active = :isActive)\n" +
            "AND (:startCost::int IS NULL OR t.cost >= :startCost::int)\n" +
            "AND (:endCost::int IS NULL OR t.cost <= :endCost::int)\n" +
            "AND (:startDateOfCreation::timestamp IS NULL OR t.date_registration >= :startDateOfCreation::timestamp)" +
            "AND (:endDateOfCreation::timestamp IS NULL OR t.date_registration <= :endDateOfCreation::timestamp)\n" +
            "group by id\n";//+
//            "WHERE" +
//            "group by id ";

    private static final String HAVING = " HAVING (:areColorIdsNull::boolean OR array_agg(colId::text::int) && %s )\n" +
            "AND (:areMaterialIdsNull::boolean OR array_agg(matId::text::int) && %s )\n";

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
    private final RowMapper<TailoringTemplateWithMaterialIds> rowMapper;
    private final Gson jsonConvector;
    private final RepositoryHandler handler;

    public TailoringTemplateRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc, RepositoryHandler handler) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.handler = handler;
        this.jsonConvector = new Gson();
        this.rowMapper = this.generateRowMapper();
    }

    @Override
    public List<TailoringTemplateWithMaterialIds> findBy(TailoringTemplateSearchCriteria criteria, Page page) {
        String script = SELECT_TEMPLATE + getHavingWithParameters(criteria.getColorIds(), criteria.getMaterialIds())
                + handler.getScriptFromPage(page, "date_registration", Page.Direction.DESC, 100L, 0L);

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

        return namedJdbc.query(script, args, rowMapper);
    }

    @Override
    public Set<String> findAllTypeTemplate() {
        return null;
    }

    @Override
    public boolean save(TailoringTemplateWithMaterialIds template) {
        return false;
    }

    @Override
    public boolean update(TailoringTemplateWithMaterialIds template) {
        return false;
    }

    private RowMapper<TailoringTemplateWithMaterialIds> generateRowMapper() {
        RowMapper<TailoringTemplateWithMaterialIds> mapperForNotJsonFields =
                new BeanPropertyRowMapper(TailoringTemplateWithMaterialIds.class);
        return (r, i) -> {
            var template = mapperForNotJsonFields.mapRow(r, i);

            template.setImagesUrl(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("imagesUrlParam"),
                            String[].class)
                    )));
            template.setPartSizeForTemplates(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("partSizes"),
                            PartSizeForTemplate[].class)
                    )));
            template.setColorIds(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("colorIdsParam"),
                            Integer[].class)
                    )));
            template.setMaterialIds(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
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

    private String getHavingWithParameters(Iterable<Integer> colorIds, Iterable<Integer> materialIds) {
        return HAVING.formatted(getConditionForJsonArray(colorIds), getConditionForJsonArray(materialIds));
    }

    private String getConditionForJsonArray(Iterable<?> iterable) {
        var iterableString = handler.getStringIterableFromEnumIterable(iterable);

        return "'{" + String.join(",", iterableString == null ? List.of() : iterableString) + "}'";
    }
}
