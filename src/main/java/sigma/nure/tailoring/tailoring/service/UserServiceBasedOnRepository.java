package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.UserSearchCriteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserServiceBasedOnRepository implements UserService{
    private final UserRepository repository;
    private final long minutesForWork;

    public UserServiceBasedOnRepository(UserRepository repository, long minutesForWork) {
        this.repository = repository;
        this.minutesForWork = minutesForWork;
    }

    @Override
    public List<User> findBy(UserSearchCriteria userParameters, Page pageable) {
        return repository.findBy(userParameters,pageable);
    }

    @Override
    public Optional<User> findByWorkCodeAndPhoneNumber(String code, String number) {
        return repository.findByWorkCodeAndPhoneNumber(code,number, LocalDateTime.now().minusMinutes(minutesForWork));
    }

    @Override
    public Optional<Long> saveAndReturnUserId(User user) {
        return repository.saveAndReturnUserId(user);
    }

    @Override
    public boolean isBooked(String email, String phoneNumber) {
        return repository.isBooked(email,phoneNumber,LocalDateTime.now().minusMinutes(minutesForWork));
    }

    @Override
    public boolean update(User user) {
        return repository.update(user);
    }
}
