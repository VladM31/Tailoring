package sigma.nure.tailoring.tailoring.service;

import org.springframework.lang.NonNull;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.tools.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll(@NonNull Pageable pageable);
    Optional<User> findById(@NonNull Long id);
    Optional<User> findByPhoneNumberAndActiveTrueAndUserStateRegistered(String number);
    Optional<User> findByUserCodeAndPhoneNumberAndActiveTrue(String code, String number);

    boolean save(@NonNull User user);
    boolean isBooked(String email,String phoneNumber);

    boolean update(@NonNull User user);
    boolean updateActiveById(boolean active,Long userId);
    boolean updateUserStateById(UserState userState, Long userId);
}
