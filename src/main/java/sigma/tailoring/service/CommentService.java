package sigma.tailoring.service;

import sigma.tailoring.entities.User;
import sigma.tailoring.tools.CommentOrderForm;

import java.util.List;

public interface CommentService {
    boolean save(User user, CommentOrderForm comment);

    List<CommentOrderForm> findAllByOrderId(User user, Long orderId);
}
