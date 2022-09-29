package sigma.tailoring.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.tailoring.entities.User;
import sigma.tailoring.service.CommentService;
import sigma.tailoring.tools.CommentOrderForm;
import sigma.tailoring.tools.OnSave;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("order")
public class OrderCommentController {
    private final CommentService commentService;

    public OrderCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{orderId}/comments")
    public List<CommentOrderForm> getComments(@AuthenticationPrincipal User user, @PathVariable Long orderId) {
        return commentService.findAllByOrderId(user, orderId);
    }

    @PostMapping("{orderId}/comments")
    @Validated(OnSave.class)
    public boolean saveComment(@AuthenticationPrincipal User user, @Valid CommentOrderForm commentOrderForm, @PathVariable Long orderId) {
        commentOrderForm.setTailoringOrderId(orderId);
        return commentService.save(user, commentOrderForm);
    }


}
