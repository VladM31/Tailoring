package sigma.tailoring.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CUSTOMER, ADMINISTRATION;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
