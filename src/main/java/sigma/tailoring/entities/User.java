package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(this.role);
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}
