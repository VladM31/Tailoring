package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.nure.tailoring.tailoring.tools.UserSortColumn;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserWebSortColumnConverter {
    private final Map<UserSortColumn, String> sortColumnMap;

    public UserWebSortColumnConverter() {
        this.sortColumnMap = new HashMap<>();
        putParameters();
    }

    public String convert(UserSortColumn userSortColumn) {
        return sortColumnMap.getOrDefault(userSortColumn, "dateRegistration");
    }

    private void putParameters() {
        sortColumnMap.put(UserSortColumn.CITY, "city");
        sortColumnMap.put(UserSortColumn.MALE, "male");
        sortColumnMap.put(UserSortColumn.EMAIL, "email");
        sortColumnMap.put(UserSortColumn.ACTIVE, "active");
        sortColumnMap.put(UserSortColumn.COUNTRY, "country");
        sortColumnMap.put(UserSortColumn.LASTNAME, "lastname");
        sortColumnMap.put(UserSortColumn.FIRSTNAME, "firstname");
        sortColumnMap.put(UserSortColumn.ROLE_NAME, "name");
        sortColumnMap.put(UserSortColumn.USER_STATE, "userState");
        sortColumnMap.put(UserSortColumn.PHONE_NUMBER, "phoneNumber");
        sortColumnMap.put(UserSortColumn.DATE_REGISTRATION, "dateRegistration");
    }

}
