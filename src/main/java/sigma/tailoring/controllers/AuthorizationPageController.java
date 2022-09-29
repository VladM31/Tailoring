package sigma.tailoring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sigma.tailoring.exceptions.FormException;
import sigma.tailoring.service.SecurityService;
import sigma.tailoring.tools.OnSaveForm;
import sigma.tailoring.tools.RegistrationUserForm;

@Controller
@RequestMapping("authorization")
public class AuthorizationPageController {
    private final SecurityService securityService;

    public AuthorizationPageController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("login")
    public String getLoginPage() {
        return "SignIn";
    }

    @GetMapping("sign-up")
    public String getSingUpUserForm(Model model) {
        model.addAttribute("registrationUserForm", new RegistrationUserForm());
        return "SignUp";
    }

    @PostMapping("sign-up")
    public String postSingUpUserForm(@Validated(OnSaveForm.class) RegistrationUserForm userForm) {
        if (securityService.saveUser(userForm)) {
            return "redirect:/authorization/check-code?phoneNumber=" + userForm.getPhoneNumber();
        }
        throw new FormException("Sorry, you were not registered", userForm, "registrationUserForm", "SignUp", "errors");
    }

    @GetMapping("check-code")
    public String getCheckCodePage() {
        return "InputCodePage";
    }
}
