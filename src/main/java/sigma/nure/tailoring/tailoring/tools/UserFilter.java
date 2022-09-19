package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private Range<LocalDateTime> dataRegistration;
    private Boolean active;
    private Boolean male;
    private UserState[] userStates;
    private Role[] roles;

    private boolean desc;
    private Long limit;
    private Long offset;
    private UserSortColumn userSortColumn;

    public UserList filtering(UserService userService,HandlerFilter handlerFilter, Map<UserSortColumn,String> sortColumnConvector){
        return new UserList(
                userService.findBy(
                        toUserSearchCriteria(handlerFilter),
                        toPage(sortColumnConvector)
                ));
    }

    private Page toPage(Map<UserSortColumn,String> sortColumnConvector){
        return Page.builder()
                .limit(limit)
                .offset(offset)
                .direction(desc ? Page.Direction.DESC : Page.Direction.ASC)
                .orderBy(sortColumnConvector
                        .get(userSortColumn != null ? userSortColumn : UserSortColumn.DATE_REGISTRATION))
                .build();
    }

    private UserSearchCriteria toUserSearchCriteria(HandlerFilter handlerFilter){
        return UserSearchCriteria.builder()
                .ids(handlerFilter.toList(this.ids))
                .phoneNumber(handlerFilter.checkString(this.phoneNumber))
                .email(handlerFilter.checkString(this.email))
                .city(handlerFilter.checkString(this.city))
                .country(handlerFilter.checkString(this.country))
                .firstname(handlerFilter.checkString(this.firstname))
                .lastname(handlerFilter.checkString(this.lastname))
                .dataRegistration(this.dataRegistration)
                .active(this.active)
                .male(this.male)
                .userStates(handlerFilter.toList(this.userStates))
                .roles(handlerFilter.toList(this.roles))
                .build();
    }


}