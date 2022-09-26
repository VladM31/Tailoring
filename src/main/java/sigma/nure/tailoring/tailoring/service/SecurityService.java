package sigma.nure.tailoring.tailoring.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sigma.nure.tailoring.tailoring.tools.ConfirmRegistrationForm;
import sigma.nure.tailoring.tailoring.tools.RegistrationUserForm;

public interface SecurityService extends UserDetailsService {
    boolean saveUser(RegistrationUserForm registrationUserForm);

    boolean confirmRegistration(ConfirmRegistrationForm confirmRegistrationForm);
}
