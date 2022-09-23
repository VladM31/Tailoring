package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import sigma.nure.tailoring.tailoring.entities.User;

@Component
public class ControllerHandler {

    public void setTopLabel(Model model, @Nullable User user) {
        model.addAttribute("isAuthorized", user != null);
    }
}
