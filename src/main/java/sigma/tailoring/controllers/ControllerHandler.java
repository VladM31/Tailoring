package sigma.tailoring.controllers;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;
import sigma.tailoring.entities.User;

@Component
public class ControllerHandler {

    public void setTopLabel(Model model, @Nullable User user) {
        model.addAttribute("isAuthorized", user != null);
    }

    public String toTitleCase(Enum<?> status) {
        return StringUtils.capitalize(
                status.name()
                        .toLowerCase()
                        .replaceAll("_", " "));
    }
}
