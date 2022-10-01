package sigma.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.tailoring.tools.OrderSortColumn;

import java.util.Map;

import static java.util.Map.entry;

@Component
public class OrderWebSortColumnConverter {
    private final Map<OrderSortColumn, String> sortColumnMap;

    public OrderWebSortColumnConverter() {
        this.sortColumnMap = buildMap();
    }

    public String convert(OrderSortColumn columnName) {
        return sortColumnMap.get(columnName != null ? columnName : OrderSortColumn.DATE_OF_CREATION);
    }

    private Map<OrderSortColumn, String> buildMap() {

        return Map.ofEntries(
                entry(OrderSortColumn.ADDRESS_FOR_SEND, "addressForSend"),
                entry(OrderSortColumn.STATUS, "status"),
                entry(OrderSortColumn.PAYMENT_STATUS, "paymentStatus"),
                entry(OrderSortColumn.DATE_OF_CREATION, "dateOfCreation"),
                entry(OrderSortColumn.TEMPLATE_ID, "templateId"),
                entry(OrderSortColumn.END_DATE, "endDate"),
                entry(OrderSortColumn.COST, "cost"),
                entry(OrderSortColumn.COUNT, "countOfOrder"),
                entry(OrderSortColumn.PHONE_NUMBER, "customerPhoneNumber"),
                entry(OrderSortColumn.CITY, "customerCity"),
                entry(OrderSortColumn.COUNTRY, "customerCountry"),
                entry(OrderSortColumn.FIRSTNAME, "customerFirstname"),
                entry(OrderSortColumn.GENDER, "customerMale"),
                entry(OrderSortColumn.MATERIAL, "materialName"),
                entry(OrderSortColumn.COLOR, "colorName")
        );
    }
}
