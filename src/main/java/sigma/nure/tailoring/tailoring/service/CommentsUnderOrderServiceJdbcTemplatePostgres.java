package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.repository.CommentsUnderOrderRepository;

import java.util.List;

public class CommentsUnderOrderServiceJdbcTemplatePostgres implements CommentsUnderOrderService{

    private final CommentsUnderOrderRepository repository;

    public CommentsUnderOrderServiceJdbcTemplatePostgres(CommentsUnderOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean save(CommentsUnderOrder comment) {
        if(comment.getTailoringOrderId() == null ||
           comment.getUserId() == null ||
           comment.getMessage() == null ||
           comment.getMessage().isBlank() ||
           comment.getMessage().length() > 200
        ){
            return false;
        }
        return repository.save(comment);
    }

    @Override
    public List<CommentsUnderOrder> findAllByOrderId(Long orderId) {
        return repository.findAllByOrderId(orderId);
    }
}
