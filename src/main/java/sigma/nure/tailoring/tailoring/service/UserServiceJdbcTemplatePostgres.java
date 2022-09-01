package sigma.nure.tailoring.tailoring.service;

import org.springframework.beans.factory.annotation.Value;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.tools.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserServiceJdbcTemplatePostgres implements UserService{

    private final UserRepository repository;
    private final long waitUserRegistration;

    public UserServiceJdbcTemplatePostgres(UserRepository repository,
                                            long waitUserRegistration) {
        this.repository = repository;
        this.waitUserRegistration = waitUserRegistration;
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> findByPhoneNumberAndActiveTrueAndUserStateRegistered(String number) {
        return repository.findByPhoneNumberAndActiveTrueAndUserStateRegistered(number);
    }

    @Override
    public Optional<User> findByUserCodeAndPhoneNumberAndActiveTrue(String code, String number) {
        return repository.findByUserCodeAndPhoneNumberAndActiveTrueAndDateOfCreationAfter(code,number,
                LocalDateTime.now().minusMinutes(this.waitUserRegistration));
    }

    @Override
    public boolean save(User user) {
        return repository.save(user);
    }

    @Override
    public boolean isBooked(String email, String phoneNumber) {
        return repository.isBooked(email,phoneNumber,LocalDateTime.now().minusMinutes(this.waitUserRegistration));
    }

    @Override
    public boolean update(User user) {
        return repository.update(user);
    }

    @Override
    public boolean updateActiveById(boolean active, Long userId) {
        return repository.updateActiveById(active,userId);
    }

    @Override
    public boolean updateUserStateById(UserState userState, Long userId) {
        return repository.updateUserStateById(userState,userId);
    }
}
