package sigma.tailoring.converters;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TailoringTemplateSortColumnConverter {
    private final Map<String, String> sortColumnMap;

    public TailoringTemplateSortColumnConverter() {
        this.sortColumnMap = buildMap();
    }

    public String convert(String sortColumn) {
        return sortColumnMap.getOrDefault(sortColumn == null ? "" : sortColumn, "t.date_of_creation");
    }

    private Map<String, String> buildMap() {
        return Map.of(
                "name", "t.name",
                "typeTemplate", "t.type_template",
                "dateOfCreation", "t.date_of_creation",
                "active", "t.active",
                "cost", "t.cost"
        );
    }

}
