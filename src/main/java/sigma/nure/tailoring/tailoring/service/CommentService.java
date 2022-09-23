package sigma.nure.tailoring.tailoring.service;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.tools.CommentOrderForm;

import java.util.List;

public interface CommentService {
    boolean save(User user, CommentOrderForm comment);

    List<CommentOrderForm> findAllByOrderId(Long orderId);
}
