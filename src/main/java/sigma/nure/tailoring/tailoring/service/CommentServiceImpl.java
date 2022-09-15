package sigma.nure.tailoring.tailoring.service;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import sigma.nure.tailoring.tailoring.entities.CommentsUnderOrder;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
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
    public Answer<HttpStatus> save(User user, CommentOrderForm comment, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new Answer<>(HttpStatus.BAD_REQUEST, bindingResult.getFieldErrors()
                    .stream().collect(Collectors
                            .mapping(error -> error.getDefaultMessage(),
                                    Collectors.joining("\n"))));
        }

        var validAnswer = this.validByUserRights(user, comment);
        if (!validAnswer.getValue().equals(HttpStatus.OK)) {
            return validAnswer;
        }

        final boolean wasSave = commentRepository.save(convectorCommentFormToComment(comment));
        return new Answer<>(wasSave ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR,
                wasSave ? "" : "Sorry, repeat the request or write to the administration");
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

    private Answer<HttpStatus> validByUserRights(User user, CommentOrderForm comment) {
        if (user.getRole().equals(Role.ADMINISTRATION)) {
            return new Answer<>(HttpStatus.OK, "");
        }

        if (orderRepository.findBy(OrderSearchCriteria
                                .builder()
                                .userIds(List.of(user.getId()))
                                .templateIds(List.of(comment.getTailoringOrderId()))
                                .build(),
                        new Page())
                .isEmpty()
        ) {
            return new Answer<>(HttpStatus.BAD_REQUEST, "Sorry, it's not your order");
        }

        return new Answer<>(HttpStatus.OK, "");
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
