package sigma.nure.tailoring.tailoring.repository;

import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;

import java.util.List;

public interface CommentsUnderOrderRepository {
    boolean save(CommentsUnderOrder comment);
    List<CommentsUnderOrder> findAllByOrderId(Long orderId);
}
