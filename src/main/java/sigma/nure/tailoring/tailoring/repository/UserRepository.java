package sigma.nure.tailoring.tailoring.repository;


import org.springframework.lang.NonNull;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.UserSearchCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findBy(UserSearchCriteria userParameters, Page pageable);

    Optional<User> findByUserCodeAndPhoneNumberAndActiveTrueAndDateOfCreationAfter(String code, String number, LocalDateTime dateOfCreation);

    Optional<Long> saveAndReturnUserId(@NonNull User user);

    boolean isBooked(String email, String phoneNumber, LocalDateTime dataAfter);

    boolean update(@NonNull User user);

    boolean updateActiveById(boolean active, Long userId);
}
