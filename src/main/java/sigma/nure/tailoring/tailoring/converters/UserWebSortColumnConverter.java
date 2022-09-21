package sigma.nure.tailoring.tailoring.converters;

import org.springframework.stereotype.Component;
import sigma.nure.tailoring.tailoring.tools.UserSortColumn;

import java.util.Map;
import static java.util.Map.entry;

@Component
public class UserWebSortColumnConverter {
    private final Map<UserSortColumn, String> sortColumnMap;

    public UserWebSortColumnConverter() {
        this.sortColumnMap = buildMap();
    }

    public String convert(UserSortColumn userSortColumn) {
        return sortColumnMap.getOrDefault(userSortColumn, "dateRegistration");
    }

    private Map<UserSortColumn, String> buildMap() {
        return Map.ofEntries(
                entry(UserSortColumn.CITY, "city"),
                entry(UserSortColumn.MALE, "male"),
                entry(UserSortColumn.EMAIL, "email"),
                entry(UserSortColumn.ACTIVE, "active"),
                entry(UserSortColumn.COUNTRY, "country"),
                entry(UserSortColumn.LASTNAME, "lastname"),
                entry(UserSortColumn.FIRSTNAME, "firstname"),
                entry(UserSortColumn.ROLE_NAME, "roleName"),
                entry(UserSortColumn.USER_STATE, "userState"),
                entry(UserSortColumn.PHONE_NUMBER, "phoneNumber"),
                entry(UserSortColumn.DATE_REGISTRATION, "dateRegistration")
        );
    }

}
