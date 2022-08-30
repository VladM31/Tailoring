package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "phone_number",nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    private String email;
    private String city;
    private String country;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(name = "date_registration",nullable = false)
    private LocalDateTime dateRegistration;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    private boolean male;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_state",nullable = false)
    private UserState userState;
    @ManyToOne
    @JoinColumn(name = "role_id",columnDefinition = "id")
    private Role role;

}
