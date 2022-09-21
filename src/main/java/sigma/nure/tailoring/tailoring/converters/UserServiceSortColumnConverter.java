package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceSortColumnConverter {
    private final Map<String, String> sortColumnMap;

    public UserServiceSortColumnConverter() {
        sortColumnMap = this.buildMap();
    }

    public String convert(String columnName) {
        return sortColumnMap.getOrDefault(columnName, "u.date_registration");
    }

    private Map<String, String> buildMap() {
        Map<String, String> columnMap = new HashMap<>();
        columnMap.putAll(Map.of(
                "city", "u.city",
                "male", "u.male",
                "email", "u.email",
                "active", "u.active",
                "country", "u.country",
                "lastname", "u.lastname",
                "firstname", "u.firstname",
                "roleName", "r.name",
                "userState", "u.user_state",
                "phoneNumber", "u.phone_number"
        ));
        columnMap.put("dateRegistration", "u.date_registration");
        return Collections.unmodifiableMap(columnMap);
    }
}
