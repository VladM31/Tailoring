package sigma.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.tailoring.tools.TemplateSortColumn;

import java.util.Map;

@Component
public class TemplateWebSortColumnConverter {
    private final Map<TemplateSortColumn, String> sortColumnMap;

    public TemplateWebSortColumnConverter() {
        this.sortColumnMap = buildMap();
    }

    public String convert(TemplateSortColumn sortColumn) {
        return sortColumnMap.getOrDefault(
                sortColumn != null ? sortColumn : TemplateSortColumn.DATA_OF_CREATION, "dateOfCreation");
    }

    private Map<TemplateSortColumn, String> buildMap() {
        return Map.of(
                TemplateSortColumn.NAME, "name",
                TemplateSortColumn.COST, "cost",
                TemplateSortColumn.ACTIVE, "active",
                TemplateSortColumn.TYPE_TEMPLATE, "typeTemplate",
                TemplateSortColumn.DATA_OF_CREATION, "dateOfCreation"
        );
    }
}
