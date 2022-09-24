package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserForm {
    private static final boolean ACTIVE = true;
    @Pattern(regexp = "\\d{10,15}", message = "Phone number length must be between 10 and 15", groups = OnSave.class)
    @Size(min = 10, max = 15, message = "Phone number length must be between 10 and 15", groups = OnSave.class)
    @NotBlank(message = "Phone number mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Phone number mustn't be null", groups = OnSave.class)
    private String phoneNumber;
    @Size(min = 8, max = 60, message = "Password length must be between 8 and 60", groups = OnSave.class)
    @NotBlank(message = "Password mustn't be blank", groups = OnSave.class)
    @NotNull(groups = OnSave.class, message = "Password mustn't be null")
    private String password;
    @Email(message = "Email is not correct")
    @Size(max = 60, message = "Email length must be less than 61", groups = OnSave.class)
    @NotBlank(message = "Email mustn't be blank", groups = OnSave.class)
    private String email;
    @Size(max = 60, message = "City length must be less than 61", groups = OnSave.class)
    @NotBlank(message = "City mustn't be blank", groups = OnSave.class)
    private String city;
    @Size(max = 60, message = "Country length must less than 61", groups = OnSave.class)
    @NotBlank(message = "Country mustn't be blank", groups = OnSave.class)
    private String country;
    @Size(min = 2, max = 60, message = "Firstname length must be between 2 and 60", groups = OnSave.class)
    @NotBlank(message = "Firstname number mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Firstname mustn't be null", groups = OnSave.class)
    private String firstname;
    @Size(min = 2, max = 60, message = "Lastname length must be between 2 and 60", groups = OnSave.class)
    @NotBlank(message = "Lastname number mustn't be blank", groups = OnSave.class)
    @NotNull(message = "Lastname mustn't be null", groups = OnSave.class)
    private String lastname;
    @NotNull(message = "Gender mustn't be null", groups = OnSave.class)
    private Boolean male;

    public User toUser() {
        User user = new User();

        user.setPhoneNumber(this.phoneNumber);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setCity(this.city);
        user.setCountry(this.country);
        user.setFirstname(this.firstname);
        user.setLastname(this.lastname);
        user.setMale(this.male);

        user.setActive(ACTIVE);
        user.setUserState(UserState.REGISTERED);
        user.setRole(Role.CUSTOMER);

        return user;
    }
}
