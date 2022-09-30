package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thymeleaf.util.StringUtils;
import sigma.tailoring.converters.TemplateWebSortColumnConverter;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateFilter {
    private String name;
    private Boolean isActive;
    private Integer[] colorIds;
    private Integer[] materialIds;
    private String[] types;
    private Range<Integer> cost;
    private Range<LocalDateTime> dateOfCreation;

    private boolean desc;
    private Long limit;
    private Long offset;
    private TemplateSortColumn sortColumn;

    public Page toPage(TemplateWebSortColumnConverter sortColumnConverter) {
        Page page = new Page();

        page.setDirection(this.desc ? Page.Direction.DESC : Page.Direction.ASC);
        page.setLimit(this.limit);
        page.setOffset(this.offset);
        page.setOrderBy(sortColumnConverter.convert(this.sortColumn));

        return page;
    }

    public TailoringTemplateSearchCriteria toSearchCriteria(HandlerFilter handlerFilter) {
        TailoringTemplateSearchCriteria criteria = new TailoringTemplateSearchCriteria();

        criteria.setColorIds(handlerFilter.toList(this.colorIds));
        criteria.setMaterialIds(handlerFilter.toList(this.materialIds));
        criteria.setTypeTemplates(handlerFilter.toList(this.types));
        criteria.setName(StringUtils.isEmptyOrWhitespace(this.name) ? null : this.name);
        criteria.setIsActive(this.isActive);
        criteria.setCost(this.cost);
        criteria.setDateOfCreation(this.dateOfCreation);

        return criteria;
    }
}
