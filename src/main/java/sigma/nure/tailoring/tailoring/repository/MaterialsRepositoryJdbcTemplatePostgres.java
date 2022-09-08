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

    private static final String SELECT_ALL_MATERIALS = "SELECT id, name, cost_one_square_meter AS cost FROM material";

    private static final String UPDATE_MATERIAL_BY_ID = "UPDATE material SET name = ?, cost_one_square_meter = ? WHERE id = ?";

    private static final String INSERT_MATERIAL = "INSERT INTO material(name,cost_one_square_meter) VALUES(?,?)";

    private static final String SELECT_ALL_COLORS = "SELECT id, name, color_code AS code FROM color";

    private static final String UPDATE_COLORS_BY_ID = "UPDATE color SET name = ?, color_code = ? WHERE id = ?";

    private static final String INSERT_COLOR = "INSERT INTO color(name,color_code) VALUES(?,?)";

    private final JdbcTemplate jdbc;
    private final RowMapper<Material> materialRowMapper;
    private final RowMapper<Color> colorRowMapper;

    public MaterialsRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.materialRowMapper = new BeanPropertyRowMapper<>(Material.class);
        this.colorRowMapper = new BeanPropertyRowMapper<>(Color.class);
    }

    @Override
    public List<Material> findAllMaterial() {
        return jdbc.query(SELECT_ALL_MATERIALS, materialRowMapper);
    }

    @Override
    public boolean updateMaterial(Material material) {
        return jdbc.update(UPDATE_MATERIAL_BY_ID, material.getName(), material.getCost(), material.getId()) != 0;
    }

    @Override
    public boolean saveMaterial(Material material) {
        return jdbc.update(INSERT_MATERIAL, material.getName(), material.getCost()) != 0;
    }

    @Override
    public List<Color> findAllColors() {
        return jdbc.query(SELECT_ALL_COLORS, this.colorRowMapper);
    }

    @Override
    public boolean updateColor(Color color) {
        return jdbc.update(UPDATE_COLORS_BY_ID, color.getName(), color.getCode(), color.getId()) != 0;
    }

    @Override
    public boolean saveColor(Color color) {
        return jdbc.update(INSERT_COLOR, color.getName(), color.getCode()) != 0;
    }

}
