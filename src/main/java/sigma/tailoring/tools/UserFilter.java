package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.converters.UserWebSortColumnConverter;
import sigma.tailoring.entities.Role;
import sigma.tailoring.entities.UserState;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilter {
    private Long[] ids;
    private String phoneNumber;
    private String email;
    private String city;
    private String country;
    private String firstname;
    private String lastname;
    private Range<LocalDateTime> dateRegistration;
    private Boolean active;
    private Boolean male;
    private UserState[] userStates;
    private Role[] roles;

    private boolean desc;
    private Long limit;
    private Long offset;
    private UserSortColumn userSortColumn;

    public Page toPage(UserWebSortColumnConverter sortColumnConvertor) {
        return Page.builder()
                .limit(limit)
                .offset(offset)
                .direction(desc ? Page.Direction.DESC : Page.Direction.ASC)
                .orderBy(sortColumnConvertor
                        .convert(userSortColumn))
                .build();
    }

    public UserSearchCriteria toUserSearchCriteria(HandlerFilter handlerFilter) {
        return UserSearchCriteria.builder()
                .ids(handlerFilter.toList(this.ids))
                .phoneNumber(handlerFilter.checkString(this.phoneNumber))
                .email(handlerFilter.checkString(this.email))
                .city(handlerFilter.checkString(this.city))
                .country(handlerFilter.checkString(this.country))
                .firstname(handlerFilter.checkString(this.firstname))
                .lastname(handlerFilter.checkString(this.lastname))
                .dataRegistration(this.dateRegistration)
                .active(this.active)
                .male(this.male)
                .userStates(handlerFilter.toList(this.userStates))
                .roles(handlerFilter.toList(this.roles))
                .build();
    }


}
