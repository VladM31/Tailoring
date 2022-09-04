package sigma.nure.tailoring.tailoring.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class MaterialsRepositoryJdbcTemplatePostgres implements MaterialsRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<Material> materialRowMapper;
    private final RowMapper<Color> colorRowMapper;
    private final NamedParameterJdbcTemplate namedJdbc;


    public MaterialsRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.materialRowMapper = new BeanPropertyRowMapper<>(Material.class);
        this.colorRowMapper = new BeanPropertyRowMapper<>(Color.class);
    }

    private static final String SELECT_ALL_MATERIALS = "SELECT id, name, cost_one_square_meter AS cost FROM material";

    @Override
    public List<Material> findAllMaterial() {
        return jdbc.query(SELECT_ALL_MATERIALS, materialRowMapper);
    }

    private static final String SELECT_MATERIALS_BY_ID_IN = SELECT_ALL_MATERIALS + " WHERE id IN(:ids) ";

    @Override
    public List<Material> findMaterialsByIdIn(Integer... ids) {
        Map<String, List<Integer>> paramMap = Collections.singletonMap("ids", List.of(ids));
        return namedJdbc.query(SELECT_MATERIALS_BY_ID_IN, paramMap, materialRowMapper);
    }

    private static final String UPDATE_MATERIAL_BY_ID = "UPDATE material SET name = ?, cost_one_square_meter = ? WHERE id = ?";

    @Override
    public boolean updateMaterial(Material material) {
        return jdbc.update(UPDATE_MATERIAL_BY_ID, material.getName(), material.getCost(), material.getId()) != 0;
    }

    private static final String INSERT_MATERIAL = "INSERT INTO material(name,cost_one_square_meter) VALUES(?,?)";

    @Override
    public boolean saveMaterial(Material material) {
        return jdbc.update(INSERT_MATERIAL, material.getName(), material.getCost()) != 0;
    }

    private static final String SELECT_ALL_COLORS = "SELECT id, name, color_code AS code FROM color";

    @Override
    public List<Color> findAllColors() {
        return jdbc.query(SELECT_ALL_COLORS, this.colorRowMapper);
    }

    private static final String SELECT_COLORS_BY_ID_IN = SELECT_ALL_COLORS + " WHERE id IN(:ids) ";

    @Override
    public List<Color> findColorsByIdIn(Integer... ids) {
        Map<String, List<Integer>> paramMap = Collections.singletonMap("ids", List.of(ids));
        return namedJdbc.query(SELECT_COLORS_BY_ID_IN, paramMap, colorRowMapper);
    }

    private static final String UPDATE_COLORS_BY_ID = "UPDATE color SET name = ?, color_code = ? WHERE id = ?";

    @Override
    public boolean updateColor(Color color) {
        return jdbc.update(UPDATE_COLORS_BY_ID, color.getName(), color.getCode(), color.getId()) != 0;
    }

    private static final String INSERT_COLOR = "INSERT INTO color(name,color_code) VALUES(?,?)";

    @Override
    public boolean saveColor(Color color) {
        return jdbc.update(INSERT_COLOR, color.getName(), color.getCode()) != 0;
    }

}
