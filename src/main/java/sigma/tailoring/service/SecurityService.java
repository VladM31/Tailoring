package sigma.tailoring.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sigma.tailoring.tools.ConfirmRegistrationForm;
import sigma.tailoring.tools.RegistrationUserForm;

public interface SecurityService extends UserDetailsService {
    boolean saveUser(RegistrationUserForm registrationUserForm);

    boolean confirmRegistration(ConfirmRegistrationForm confirmRegistrationForm);
}
