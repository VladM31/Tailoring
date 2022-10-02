package sigma.tailoring.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sigma.tailoring.converters.TemplateWebSortColumnConverter;
import sigma.tailoring.dto.PublicTemplateList;
import sigma.tailoring.entities.TailoringTemplate;
import sigma.tailoring.service.TailoringTemplateService;
import sigma.tailoring.tools.HandlerFilter;
import sigma.tailoring.tools.TemplateFilter;

import java.util.List;

@RestController
@RequestMapping("templates")
public class TailoringTemplateController {
    private static final boolean ACTIVE_TEMPLATES = true;
    private final TailoringTemplateService templateService;
    private final HandlerFilter handlerFilter;
    private final TemplateWebSortColumnConverter sortColumnConverter;

    public TailoringTemplateController(TailoringTemplateService templateService, HandlerFilter handlerFilter, TemplateWebSortColumnConverter sortColumnConverter) {
        this.templateService = templateService;
        this.handlerFilter = handlerFilter;
        this.sortColumnConverter = sortColumnConverter;
    }

    @GetMapping("public")
    public PublicTemplateList getPublicTemplateList(TemplateFilter filter) {
        var criteria = filter.toSearchCriteria(this.handlerFilter);
        criteria.setIsActive(ACTIVE_TEMPLATES);

        return new PublicTemplateList(templateService.findBy(criteria, filter.toPage(sortColumnConverter)));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRATION')")
    public List<TailoringTemplate> getTailoringTemplates(TemplateFilter filter) {
        return templateService.findBy(
                filter.toSearchCriteria(handlerFilter),
                filter.toPage(sortColumnConverter));
    }
}
