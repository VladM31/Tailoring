package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;

import java.util.List;

public interface CommentsUnderOrderService {
    boolean save(CommentsUnderOrder comment);
    List<CommentsUnderOrder> findAllByOrderId(Long orderId);
}
