package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.UserState;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
    private Iterable<Long> ids;
    private String phoneNumber;
    private String email;
    private String city;
    private String country;
    private String firstname;
    private String lastname;
    private String username;
    private Range<LocalDateTime> dataRegistration;
    private Boolean active;
    private Boolean male;
    private Iterable<UserState> userStates;
    private Iterable<Role> roles;

}
