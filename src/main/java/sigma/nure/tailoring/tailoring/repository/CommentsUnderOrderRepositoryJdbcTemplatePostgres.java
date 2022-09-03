package sigma.nure.tailoring.tailoring.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;

import java.util.List;

public class CommentsUnderOrderRepositoryJdbcTemplatePostgres implements CommentsUnderOrderRepository{

    private final JdbcTemplate jdbc;

    public CommentsUnderOrderRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public boolean save(CommentsUnderOrder comment) {
        return false;
    }

    @Override
    public List<CommentsUnderOrder> findAllByOrderId(Long orderId) {
        return null;
    }
}
