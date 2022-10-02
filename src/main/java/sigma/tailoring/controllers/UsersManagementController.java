package sigma.tailoring.controllers;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
@PreAuthorize("hasAuthority('ADMINISTRATION')")
public class UsersManagementController {

    @GetMapping("management")
    public String getShowAllUserPage() {
        return "ShowAllUsersPage";
    }
}
