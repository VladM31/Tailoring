package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.UserSearchCriteria;
import sigma.nure.tailoring.tailoring.tools.UserSortColumn;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Map<String,String> SORT_COLUMN_CONVECTOR = new HashMap<>();
    private final UserRepository repository;
    private final long minutesForWork;

    public UserServiceImpl(UserRepository repository, long minutesForWork) {
        this.repository = repository;
        this.minutesForWork = minutesForWork;
    }

    @Override
    public List<User> findBy(UserSearchCriteria userParameters, Page pageable) {
        pageable.setOrderBy(SORT_COLUMN_CONVECTOR.getOrDefault(pageable.getOrderBy(),"u.date_registration"));
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

    {
        SORT_COLUMN_CONVECTOR.put("city","u.city");
        SORT_COLUMN_CONVECTOR.put("male","u.male");
        SORT_COLUMN_CONVECTOR.put("email","u.email");
        SORT_COLUMN_CONVECTOR.put("active","u.active");
        SORT_COLUMN_CONVECTOR.put("country","u.country");
        SORT_COLUMN_CONVECTOR.put("lastname","u.lastname");
        SORT_COLUMN_CONVECTOR.put("firstname","u.firstname");
        SORT_COLUMN_CONVECTOR.put("roleName","r.name");
        SORT_COLUMN_CONVECTOR.put("userState","u.user_state");
        SORT_COLUMN_CONVECTOR.put("phoneNumber","u.phone_number");
        SORT_COLUMN_CONVECTOR.put("dateRegistration","u.date_registration");
    }
}
