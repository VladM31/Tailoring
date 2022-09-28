package sigma.tailoring.repository;


import org.springframework.lang.NonNull;
import sigma.tailoring.entities.User;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.UserSearchCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findBy(UserSearchCriteria userParameters, Page pageable);

    Optional<User> findByWorkCodeAndPhoneNumber(String code, String number, LocalDateTime dateOfCreation);

    Optional<Long> saveAndReturnUserId(@NonNull User user);

    boolean isBooked(String email, String phoneNumber, LocalDateTime dataAfter);

    boolean update(@NonNull User user);
}
