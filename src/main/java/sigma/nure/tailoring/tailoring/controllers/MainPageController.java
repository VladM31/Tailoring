package sigma.nure.tailoring.tailoring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.service.PopularTemplateService;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainPageController {
    private final PopularTemplateService popularTemplateService;
    private final HandlerForControllers handler;
    private static final Function<Stream<String>,String> findFrontImage =
            (images) -> images.filter(img -> img.contains("front-")).findFirst().orElse("Error.png");

    public MainPageController(PopularTemplateService popularTemplateService, HandlerForControllers handler) {
        this.popularTemplateService = popularTemplateService;
        this.handler = handler;
    }

    @GetMapping("/")
    public String showMainPage(User user, Model model){

        handler.setUserDataOnTopLabel(model,user);
        model.addAttribute("findFrontImg",findFrontImage);
        model.addAttribute("popularTemplates",popularTemplateService
                .getPopularTailoringTemplate()
                .stream()
                .limit(3)
                .collect(Collectors.toList()));

        return "MainPage";
    }
}
