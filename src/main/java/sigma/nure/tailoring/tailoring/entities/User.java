package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter

@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String phoneNumber;
    private String password;
    private String email;
    private String city;
    private String country;
    private String firstname;
    private String lastname;
    private LocalDateTime dateRegistration;
    private boolean active;
    private boolean male;
    private UserState userState;
    private Role role;
}
