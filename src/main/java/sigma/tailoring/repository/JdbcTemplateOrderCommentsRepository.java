package sigma.tailoring.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import sigma.tailoring.entities.CommentsUnderOrder;

import java.time.LocalDateTime;
import java.util.List;

public class JdbcTemplateOrderCommentsRepository implements OrderCommentsRepository {

    private static final String INSERT_COMMENT =
            "INSERT INTO comments_under_order(message,date_of_creation,user_id,needlework_order_id) VALUES(?,?,?,?)";

    private static final String SELECT_ALL_COMMENTS_BY_ORDER_ID =
            "SELECT id, message, " +
                    "date_of_creation AS dateOfCreation, " +
                    "user_id AS userId, " +
                    "needlework_order_id AS tailoringOrderId " +
                    "FROM " +
                    "comments_under_order " +
                    "WHERE " +
                    "needlework_order_id = ? ";

    private final JdbcTemplate jdbc;
    private final RowMapper<CommentsUnderOrder> rowMapper;

    public JdbcTemplateOrderCommentsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.rowMapper = new BeanPropertyRowMapper<>(CommentsUnderOrder.class);
    }

    @Override
    public boolean save(CommentsUnderOrder comment) {
        return jdbc.update(INSERT_COMMENT,
                comment.getMessage(),
                LocalDateTime.now(),
                comment.getUserId(),
                comment.getTailoringOrderId()
        ) != 0;
    }

    @Override
    public List<CommentsUnderOrder> findAllByOrderId(Long orderId) {
        return jdbc.query(SELECT_ALL_COMMENTS_BY_ORDER_ID, this.rowMapper, orderId);
    }
}
