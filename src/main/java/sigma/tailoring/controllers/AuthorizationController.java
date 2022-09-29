package sigma.tailoring.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.tailoring.service.TelegramBotClient;
import sigma.tailoring.service.SecurityService;
import sigma.tailoring.tools.AllOperation;
import sigma.tailoring.tools.ConfirmRegistrationForm;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("authorization")
public class AuthorizationController {
    private final TelegramBotClient botClient;
    private final SecurityService securityService;

    public AuthorizationController(TelegramBotClient botClient, SecurityService securityService) {
        this.botClient = botClient;
        this.securityService = securityService;
    }

    @GetMapping("number")
    @ResponseBody
    public boolean hasNumber(@RequestParam @Valid @Pattern(regexp = "\\d{10,15}", message = "Number not correct") String number) {
        return botClient.hasPhoneNumber(number);
    }

    @PostMapping("confirm-code")
    @ResponseBody
    @Validated(AllOperation.class)
    public boolean confirmCode(@Valid ConfirmRegistrationForm form) {
        return securityService.confirmRegistration(form);
    }
}
