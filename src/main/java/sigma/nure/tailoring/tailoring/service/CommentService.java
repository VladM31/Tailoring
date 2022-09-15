package sigma.nure.tailoring.tailoring.service;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.tools.Answer;
import sigma.nure.tailoring.tailoring.tools.CommentOrderForm;

import java.util.List;

public interface CommentService {
    Answer<HttpStatus> save(User user, CommentOrderForm comment, BindingResult bindingResult);

    List<CommentOrderForm> findAllByOrderId(Long orderId);
}
