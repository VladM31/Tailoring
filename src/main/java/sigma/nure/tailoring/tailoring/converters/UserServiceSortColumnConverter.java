package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceSortColumnConverter {
    private final Map<String, String> sortColumnMap;

    public UserServiceSortColumnConverter() {
        sortColumnMap = new HashMap<>();
        putParameters();
    }

    public String convert(String columnName) {
        return sortColumnMap.getOrDefault(columnName, "u.date_registration");
    }

    private void putParameters() {
        sortColumnMap.put("city", "u.city");
        sortColumnMap.put("male", "u.male");
        sortColumnMap.put("email", "u.email");
        sortColumnMap.put("active", "u.active");
        sortColumnMap.put("country", "u.country");
        sortColumnMap.put("lastname", "u.lastname");
        sortColumnMap.put("firstname", "u.firstname");
        sortColumnMap.put("roleName", "r.name");
        sortColumnMap.put("userState", "u.user_state");
        sortColumnMap.put("phoneNumber", "u.phone_number");
        sortColumnMap.put("dateRegistration", "u.date_registration");
    }
}
