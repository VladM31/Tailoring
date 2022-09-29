package sigma.tailoring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UsersManagementController {

    @GetMapping("management")
    public String getShowAllUserPage() {
        return "ShowAllUsersPage";
    }
}
