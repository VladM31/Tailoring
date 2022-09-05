package sigma.nure.tailoring.tailoring.repository;


import org.springframework.lang.NonNull;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll(Iterable<Long> ids,
                       String phoneNumberContaining,
                       String emailContaining,
                       String cityContaining,
                       String countryContaining,
                       String firstnameContaining,
                       String lastnameContaining,
                       LocalDateTime afterOrEqualsDataRegistration,
                       LocalDateTime beforeOrEqualsDataRegistration,
                       Boolean activeUser,
                       Boolean male,
                       Iterable<UserState> userStates,
                       Iterable<Role> roles,
                       Page pageable);

    Optional<User> findByUserCodeAndPhoneNumberAndActiveTrueAndDateOfCreationAfter(String code, String number, LocalDateTime dateOfCreation);

    Optional<Long> saveAndReturnUserId(@NonNull User user);

    boolean isBooked(String email, String phoneNumber, LocalDateTime dataAfter);

    boolean update(@NonNull User user);

    boolean updateActiveById(boolean active, Long userId);

    boolean updateUserStateById(UserState userState, Long userId);
}
