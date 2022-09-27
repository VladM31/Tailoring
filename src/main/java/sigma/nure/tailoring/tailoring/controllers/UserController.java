package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.nure.tailoring.tailoring.converters.UserWebSortColumnConverter;
import sigma.nure.tailoring.tailoring.service.UserService;
import sigma.nure.tailoring.tailoring.tools.HandlerFilter;
import sigma.nure.tailoring.tailoring.tools.OnUpdate;
import sigma.nure.tailoring.tailoring.tools.UserActiveForm;
import sigma.nure.tailoring.tailoring.tools.UserFilter;
import sigma.nure.tailoring.tailoring.dto.UserList;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserWebSortColumnConverter converter;
    private final UserService userService;
    private final HandlerFilter handlerFilter;

    public UserController(UserWebSortColumnConverter converter, UserService userService, HandlerFilter handlerFilter) {
        this.converter = converter;
        this.userService = userService;
        this.handlerFilter = handlerFilter;
    }

    @GetMapping()
    public UserList getUsers(UserFilter userFilter) {
        var users = userService.findBy(
                userFilter.toUserSearchCriteria(handlerFilter),
                userFilter.toPage(converter));
        return new UserList(users);
    }

    @PutMapping("{id}/active")
    @Validated(OnUpdate.class)
    public boolean updateActive(@Valid UserActiveForm userActiveForm, @PathVariable Long id) {
        return userService.update(userActiveForm.toUser(userService, id));
    }
}
