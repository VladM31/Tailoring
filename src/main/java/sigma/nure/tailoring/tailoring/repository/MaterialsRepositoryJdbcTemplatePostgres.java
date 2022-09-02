package sigma.nure.tailoring.tailoring.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sigma.nure.tailoring.tailoring.entities.Color;
import sigma.nure.tailoring.tailoring.entities.Material;

import java.util.List;

@Repository
public class MaterialsRepositoryJdbcTemplatePostgres implements MaterialsRepository{

    private final JdbcTemplate jdbc;
    private final RowMapper<Material> rowMapper;


    public MaterialsRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.rowMapper = new BeanPropertyRowMapper<>(Material.class);
    }

    private static final String SELECT_ALL_MATERIALS = "SELECT id, name, cost_one_square_meter AS cost FROM material";
    @Override
    public List<Material> findAllMaterial() {
        return jdbc.query(SELECT_ALL_MATERIALS,rowMapper);
    }

    private static final String UPDATE_MATERIAL_BY_ID = "UPDATE material SET name = ?, cost_one_square_meter = ? WHERE id = ?";
    @Override
    public boolean updateMaterial(Material material) {
        return jdbc.update(UPDATE_MATERIAL_BY_ID,material.getName(),material.getCost(),material.getId()) != 0;
    }

    private static final String INSERT_MATERIAL = "INSERT INTO material(name,cost_one_square_meter) VALUES(?,?)";
    @Override
    public boolean saveMaterial(Material material) {
        return jdbc.update(INSERT_MATERIAL,material.getName(),material.getCost()) != 0;
    }

    @Override
    public List<Color> findAllColors() {
        return null;
    }

    @Override
    public boolean updateColor(Color color) {
        return false;
    }

    @Override
    public boolean saveColor(Color color) {
        return false;
    }
}
