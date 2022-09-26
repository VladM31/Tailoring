package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component
public class OrderServiceSortColumnConverter {
    private final Map<String, String> sortColumnMap;

    public OrderServiceSortColumnConverter() {
        sortColumnMap = this.buildMap();
    }

    public String convert(String columnName) {
        return sortColumnMap.getOrDefault(columnName == null ? "" : columnName, "o.date_of_creation");
    }

    private Map<String, String> buildMap() {

        return Map.ofEntries(
                entry("addressForSend", "o.address_for_send"),
                entry("status", "o.order_status"),
                entry("paymentStatus", "o.order_payment_status"),
                entry("dateOfCreation", "o.date_of_creation"),
                entry("templateId", "o.tailoring_templates_id"),
                entry("endDate", "o.end_date"),
                entry("cost", "o.cost"),
                entry("countOfOrder", "o.count_of_order"),
                entry("customerPhoneNumber", "u.phone_number"),
                entry("customerCity", "u.city"),
                entry("customerCountry", "u.country"),
                entry("customerFirstname", "u.firstname"),
                entry("customerMale", "u.male"),
                entry("materialName", "m.name"),
                entry("colorName", "c.name")
        );
    }
}
