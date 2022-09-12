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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TailoringTemplateRepositoryJdbcTemplatePostgres implements TailoringTemplateRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;
    private final RowMapper<TailoringTemplateWithMaterialIds> rowMapper;
    private final Gson jsonConvector;

    public TailoringTemplateRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.jsonConvector = new Gson();
        this.rowMapper = this.generateRowMapper();
    }

    @Override
    public List<TailoringTemplateWithMaterialIds> findBy(TailoringTemplateSearchCriteria criteria, Page page) {
        return null;
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
            ;
            template.setImagesUrl(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("imagesUrl"),
                            String[].class)
                    )));
            template.setPartSizeForTemplates(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("partSizes"),
                            PartSizeForTemplate[].class)
                    )));
            template.setColorIds(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("colorIds"),
                            Integer[].class)
                    )));
            template.setMaterialIds(new HashSet<>(
                    Set.of(jsonConvector.fromJson(
                            r.getString("materialIds"),
                            Integer[].class)
                    )));

            return template;
        };
    }
}
