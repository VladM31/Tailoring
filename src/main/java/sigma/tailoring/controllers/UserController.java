package sigma.tailoring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.tailoring.converters.UserWebSortColumnConverter;
import sigma.tailoring.service.UserService;
import sigma.tailoring.tools.HandlerFilter;
import sigma.tailoring.tools.UserFilter;
import sigma.tailoring.dto.UserList;

@RestController
@RequestMapping("/users")
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


}