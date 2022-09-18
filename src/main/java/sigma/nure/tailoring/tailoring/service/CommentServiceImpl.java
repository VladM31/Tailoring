package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.exceptions.TriedSetCommentForAnotherUserOrderException;
import sigma.nure.tailoring.tailoring.repository.OrderCommentsRepository;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.tools.CommentOrderForm;
import sigma.nure.tailoring.tailoring.tools.OrderSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentServiceImpl implements CommentService {
    private final OrderCommentsRepository orderCommentsRepository;
    private final OrderRepository orderRepository;

    public CommentServiceImpl(OrderCommentsRepository repository, OrderRepository orderRepository) {
        this.orderCommentsRepository = repository;
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean save(User user, CommentOrderForm comment) {
        checkUserRights(user, comment);

        return orderCommentsRepository.save(toComment(comment));
    }

    @Override
    public List<CommentOrderForm> findAllByOrderId(Long orderId) {
        if (orderId == null) {
            return new ArrayList<>();
        }
        return orderCommentsRepository.findAllByOrderId(orderId)
                .stream()
                .collect(
                        Collectors.mapping(
                                CommentServiceImpl::toCommentForm,
                                Collectors.toList()
                        ));
    }

    private void checkUserRights(User user, CommentOrderForm comment) {
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
            throw new TriedSetCommentForAnotherUserOrderException("User with id = %d with name = %s %s tried to add comment for order with id = %d"
                    .formatted(user.getId(), user.getFirstname(), user.getLastname(), comment.getTailoringOrderId()));
        }
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
