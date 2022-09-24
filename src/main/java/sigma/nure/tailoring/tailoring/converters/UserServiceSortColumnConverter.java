package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

@Component
public class UserServiceSortColumnConverter {
    private final Map<String, String> sortColumnMap;

    public UserServiceSortColumnConverter() {
        sortColumnMap = this.buildMap();
    }

    public String convert(String columnName) {
        return sortColumnMap.getOrDefault(columnName == null ? "" : columnName, "u.date_registration");
    }

    private Map<String, String> buildMap() {

        return Map.ofEntries(
                entry("city", "u.city"),
                entry("male", "u.male"),
                entry("email", "u.email"),
                entry("active", "u.active"),
                entry("country", "u.country"),
                entry("lastname", "u.lastname"),
                entry("firstname", "u.firstname"),
                entry("roleName", "r.name"),
                entry("userState", "u.user_state"),
                entry("phoneNumber", "u.phone_number"),
                entry("dateRegistration", "u.date_registration"));
    }
}
