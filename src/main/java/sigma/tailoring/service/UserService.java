package sigma.tailoring.service;

import org.springframework.lang.NonNull;
import sigma.tailoring.entities.User;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.UserSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findBy(UserSearchCriteria userParameters, Page pageable);

    Optional<User> findByWorkCodeAndPhoneNumber(String code, String number);

    Optional<Long> saveAndReturnUserId(@NonNull User user);

    boolean isBooked(String email, String phoneNumber);

    boolean update(@NonNull User user);
}
