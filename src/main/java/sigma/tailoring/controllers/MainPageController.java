package sigma.tailoring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sigma.tailoring.entities.User;
import sigma.tailoring.service.PopularTemplateService;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainPageController {
    private static final Function<Stream<String>, String> FIND_FRONT_IMAGE =
            (images) -> images.filter(img -> img.contains("front-")).findFirst().orElse("Error.png");

    private final PopularTemplateService popularTemplateService;
    private final ControllerHandler controllerHandler;

    public MainPageController(PopularTemplateService popularTemplateService, ControllerHandler handler) {
        this.popularTemplateService = popularTemplateService;
        this.controllerHandler = handler;
    }

    @GetMapping("/")
    public String showMainPage(User user, Model model) {
        controllerHandler.setTopLabel(model, user);
        model.addAttribute("findFrontImg", FIND_FRONT_IMAGE);
        model.addAttribute("popularTemplates", popularTemplateService
                .getPopularTailoringTemplate()
                .stream()
                .limit(3)
                .collect(Collectors.toList()));

        return "MainPage";
    }
}
