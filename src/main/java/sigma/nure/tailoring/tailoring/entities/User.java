package sigma.nure.tailoring.tailoring.entities;

import lombok.*;


import java.time.LocalDateTime;

@Data
@Builder
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
