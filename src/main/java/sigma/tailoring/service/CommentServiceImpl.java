package sigma.tailoring.service;

import sigma.tailoring.entities.CommentsUnderOrder;
import sigma.tailoring.entities.Role;
import sigma.tailoring.entities.User;
import sigma.tailoring.exceptions.OrderCommentException;
import sigma.tailoring.repository.OrderCommentsRepository;
import sigma.tailoring.repository.OrderRepository;
import sigma.tailoring.tools.CommentOrderForm;
import sigma.tailoring.tools.OrderHandler;
import sigma.tailoring.tools.OrderSearchCriteria;
import sigma.tailoring.tools.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CommentServiceImpl implements CommentService {
    private final OrderCommentsRepository orderCommentsRepository;
    private final OrderRepository orderRepository;
    private final OrderHandler orderHandler;

    public CommentServiceImpl(OrderCommentsRepository repository, OrderRepository orderRepository, OrderHandler orderHandler) {
        this.orderCommentsRepository = repository;
        this.orderRepository = orderRepository;
        this.orderHandler = orderHandler;
    }

    @Override
    public boolean save(User user, CommentOrderForm comment) {
        checkUserRights(user, comment.getTailoringOrderId());
        return orderCommentsRepository.save(toComment(comment));
    }

    @Override
    public List<CommentOrderForm> findAllByOrderId(User user, Long orderId) {
        checkUserRights(user, orderId);

        return orderCommentsRepository.findAllByOrderId(orderId)
                .stream()
                .collect(
                        Collectors.mapping(
                                CommentServiceImpl::toCommentForm,
                                Collectors.toList()
                        ));
    }

    private void checkUserRights(User user, Long tailoringOrderId) {
        orderHandler.getOrderByRole(user, tailoringOrderId,
                new OrderCommentException("User with id = %d with name = %s %s tried to add comment for order with id = %d"
                        .formatted(user.getId(), user.getFirstname(), user.getLastname(), tailoringOrderId))
        );
    }

    private CommentsUnderOrder toComment(CommentOrderForm form) {
        CommentsUnderOrder comment = new CommentsUnderOrder();

        comment.setMessage(form.getMessage());
        comment.setUserId(form.getUserId());
        comment.setTailoringOrderId(form.getTailoringOrderId());

        return comment;
    }

    private static CommentOrderForm toCommentForm(CommentsUnderOrder comment) {
        CommentOrderForm form = new CommentOrderForm(
                comment.getMessage(),
                comment.getUserId(),
                comment.getTailoringOrderId(),
                comment.getDateOfCreation()
        );
        return form;
    }
}
