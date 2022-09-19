package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.nure.tailoring.tailoring.service.UserService;
import sigma.nure.tailoring.tailoring.tools.HandlerFilter;
import sigma.nure.tailoring.tailoring.tools.UserFilter;
import sigma.nure.tailoring.tailoring.tools.UserList;
import sigma.nure.tailoring.tailoring.tools.UserSortColumn;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private static final Map<UserSortColumn,String> SORT_COLUMN_CONVECTOR = new HashMap<>();

    private final UserService userService;
    private final HandlerFilter handlerFilter;

    public UserController(UserService userService, HandlerFilter handlerFilter) {
        this.userService = userService;
        this.handlerFilter = handlerFilter;
    }

    @GetMapping("/users")
    public UserList getUsers(UserFilter userFilter){
        return userFilter.filtering(userService,handlerFilter, SORT_COLUMN_CONVECTOR);
    }

    {
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.CITY,"city");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.MALE,"male");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.EMAIL,"email");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.ACTIVE,"active");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.COUNTRY,"country");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.LASTNAME,"lastname");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.FIRSTNAME,"firstname");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.ROLE_NAME,"name");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.USER_STATE,"userState");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.PHONE_NUMBER,"phoneNumber");
        SORT_COLUMN_CONVECTOR.put(UserSortColumn.DATE_REGISTRATION,"dateRegistration");
    }
}
