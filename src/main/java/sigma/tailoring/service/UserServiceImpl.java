package sigma.tailoring.service;

import sigma.tailoring.converters.UserServiceSortColumnConverter;
import sigma.tailoring.entities.User;
import sigma.tailoring.repository.UserRepository;
import sigma.tailoring.tools.Page;
import sigma.tailoring.tools.UserSearchCriteria;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserServiceSortColumnConverter converter;
    private final UserRepository repository;
    private final long minutesForWork;

    public UserServiceImpl(UserServiceSortColumnConverter converter, UserRepository repository, long minutesForWork) {
        this.converter = converter;
        this.repository = repository;
        this.minutesForWork = minutesForWork;
    }

    @Override
    public List<User> findBy(UserSearchCriteria userParameters, Page pageable) {
        pageable.setOrderBy(converter.convert(pageable.getOrderBy()));
        return repository.findBy(userParameters, pageable);
    }

    @Override
    public Optional<User> findByWorkCodeAndPhoneNumber(String code, String number) {
        return repository.findByWorkCodeAndPhoneNumber(code, number, LocalDateTime.now().minusMinutes(minutesForWork));
    }

    @Override
    public Optional<Long> saveAndReturnUserId(User user) {
        return repository.saveAndReturnUserId(user);
    }

    @Override
    public boolean isBooked(String email, String phoneNumber) {
        return repository.isBooked(email, phoneNumber, LocalDateTime.now().minusMinutes(minutesForWork));
    }

    @Override
    public boolean update(User user) {
        return repository.update(user);
    }


}
