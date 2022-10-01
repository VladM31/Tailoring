package sigma.tailoring.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sigma.tailoring.entities.TailoringTemplate;
import sigma.tailoring.service.TailoringTemplateService;
import sigma.tailoring.tools.*;
import sigma.tailoring.tools.*;

import java.util.List;


@Controller
@RequestMapping(value = "/templates")
public class TailoringTemplatePageController {
    private final String directoryTemplateImages;
    private final TailoringTemplateService tailoringTemplateService;
    private final Gson gson;

    public TailoringTemplatePageController(@Value("${template.image.directory}") String directoryTemplateImages, TailoringTemplateService tailoringTemplateService) {
        this.directoryTemplateImages = directoryTemplateImages;
        this.tailoringTemplateService = tailoringTemplateService;
        this.gson = new Gson();
    }

    @GetMapping("/create")
    public String getCreateTailoringTemplate(Model model) {
        model.addAttribute("templateForm", new TailoringTemplateForm());
        return "CreateOrEditTemplate";
    }

    @PostMapping("/create")
    public String postCreateTailoringTemplate(@Validated({OnSaveForm.class}) TailoringTemplateForm form) {
        tailoringTemplateService.save(form);
        return "redirect:/templates/create";
    }

    @GetMapping("/{templateId}/edit")
    public String getEditTailoringTemplate(Model model, @PathVariable Long templateId) {
        TailoringTemplate template = tailoringTemplateService.findBy(
                        TailoringTemplateSearchCriteria.builder().templateIds(List.of(templateId)).build(), new Page())
                .stream().findFirst().orElseThrow();

        model.addAttribute("templateForm", new TailoringTemplateForm(template, gson, directoryTemplateImages));

        return "CreateOrEditTemplate";
    }

    @PostMapping("/{id}/edit")
    public String postEditTailoringTemplate(@Validated({OnUpdateForm.class}) @ModelAttribute TailoringTemplateForm form) {
        tailoringTemplateService.update(form);
        return "redirect:/templates/create";
    }

    @GetMapping("show")
    public String getShowAllTemplatePage() {
        return "ShowAllTemplates";
    }

    @GetMapping("management")
    public String getAdminTemplatePage() {
        return "AdminTemplatePage";
    }
}
