package sigma.nure.tailoring.tailoring.service;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.exceptions.TriedToTakeSecurityDataException;
import sigma.nure.tailoring.tailoring.repository.CommentsUnderOrderRepository;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.tools.Answer;
import sigma.nure.tailoring.tailoring.tools.CommentOrderForm;
import sigma.nure.tailoring.tailoring.tools.OrderSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentServiceImpl implements CommentService {
    private final CommentsUnderOrderRepository commentRepository;
    private final OrderRepository orderRepository;

    public CommentServiceImpl(CommentsUnderOrderRepository repository, OrderRepository orderRepository) {
        this.commentRepository = repository;
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean save(User user, CommentOrderForm comment) {
        validByUserRights(user, comment);

        return commentRepository.save(convectorCommentFormToComment(comment));
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

    private void validByUserRights(User user, CommentOrderForm comment) {
        if (user.getRole().equals(Role.ADMINISTRATION)) {
            return;
        }

        if (orderRepository.findBy(OrderSearchCriteria
                                .builder()
                                .userIds(List.of(user.getId()))
                                .templateIds(List.of(comment.getTailoringOrderId()))
                                .build(),
                        new Page())
                .isEmpty()
        ) {
            throw new TriedToTakeSecurityDataException("User with id = %d with name = %s %s tried added comment for order with id = %d"
                    .formatted(user.getId(), user.getFirstname(), user.getLastname(), comment.getTailoringOrderId()));
        }
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
