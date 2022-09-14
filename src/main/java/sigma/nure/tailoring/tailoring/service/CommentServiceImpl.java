package sigma.nure.tailoring.tailoring.service;

import org.springframework.validation.BindingResult;
import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.repository.CommentsUnderOrderRepository;
import sigma.nure.tailoring.tailoring.tools.Answer;
import sigma.nure.tailoring.tailoring.tools.CommentOrderForm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentServiceImpl implements CommentService {

    private final CommentsUnderOrderRepository commentRepository;

    public CommentServiceImpl(CommentsUnderOrderRepository repository) {
        this.commentRepository = repository;
    }

    @Override
    public Answer<Boolean> save(User user, CommentOrderForm comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new Answer<>(false, bindingResult.getFieldErrors().get(0).getDefaultMessage());
        }


        return null;
    }

    @Override
    public List<CommentOrderForm> findAllByOrderId(Long orderId) {
        if (orderId == null) {
            return new ArrayList<>();
        }
        return commentRepository.findAllByOrderId(orderId)
                .stream()
                .collect(
                        Collectors.mapping(
                                CommentServiceImpl::convectorCommentToCommentForm,
                                Collectors.toList()
                        ));
    }

    private Answer<Boolean> validByUserRights(User user, CommentOrderForm comment) {
        if (user.getRole().equals(Role.ADMINISTRATION)) {
            return new Answer<>(true, "");
        }


        return new Answer<>();
    }

    private CommentsUnderOrder convectorCommentFormToComment(CommentOrderForm form) {
        CommentsUnderOrder comment = new CommentsUnderOrder();

        comment.setMessage(form.getMessage());
        comment.setUserId(form.getUserId());
        comment.setTailoringOrderId(form.getTailoringOrderId());

        return comment;
    }

    private static CommentOrderForm convectorCommentToCommentForm(CommentsUnderOrder comment) {
        CommentOrderForm form = new CommentOrderForm(
                comment.getMessage(),
                comment.getUserId(),
                comment.getTailoringOrderId(),
                comment.getDateOfCreation()
        );
        return form;
    }
}
