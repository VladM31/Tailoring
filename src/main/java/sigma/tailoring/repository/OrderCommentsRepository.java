package sigma.tailoring.repository;

import sigma.tailoring.entities.CommentsUnderOrder;

import java.util.List;

public interface OrderCommentsRepository {
    boolean save(CommentsUnderOrder comment);

    List<CommentsUnderOrder> findAllByOrderId(Long orderId);
}
