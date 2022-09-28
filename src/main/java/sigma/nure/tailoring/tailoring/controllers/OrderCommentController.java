package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.service.CommentService;
import sigma.nure.tailoring.tailoring.tools.CommentOrderForm;
import sigma.nure.tailoring.tailoring.tools.OnSave;

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
