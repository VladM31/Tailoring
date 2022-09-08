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
public class UserParameters {
    private Iterable<Long> ids;
    private String phoneNumberContaining;
    private String emailContaining;
    private String cityContaining;
    private String countryContaining;
    private String firstnameContaining;
    private String lastnameContaining;
    private LocalDateTime afterOrEqualsDataRegistration;
    private LocalDateTime beforeOrEqualsDataRegistration;
    private Boolean activeUser;
    private Boolean male;
    private Iterable<UserState> userStates;
    private Iterable<Role> roles;

}
