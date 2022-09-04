package sigma.nure.tailoring.tailoring.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sigma.nure.tailoring.tailoring.entities.Role;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.tools.Page;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


public class UserRepositoryJdbcTemplatePostgres implements UserRepository {

    private static final String SELECT_USER = "SELECT u.id, u.password, u.city, " +
            "u.country, u.email, u.firstname, u.lastname, u.active, u.male, " +
            "u.user_state AS userState, " +
            "u.phone_number AS phoneNumber, " +
            "u.date_registration AS dateRegistration, " +
            "r.name AS role " +
            "FROM \"user\" u " +
            "LEFT JOIN role r ON r.id = u.role_id ";

    private static final String SELECT_ORDER_BY_LIMIT_OFFSET = SELECT_USER + " ORDER BY %s %s LIMIT %s OFFSET %s";

    private static final String SELECT_USER_BY_ID = SELECT_USER + " WHERE u.id = ? ";

    private static final String SELECT_USER_WHERE_PHONE_NUMBER_AND_ACTIVE_TRUE_AND_STATE_REGISTERED =
            SELECT_USER + " WHERE u.phone_number = ? " +
                    "AND u.active = true " +
                    "AND u.user_state = ? ";

    private static final String SELECT_USER_WHERE_CODE_AND_NUMBER_ID_AND_DATE_OD_CREATION_AFTER = SELECT_USER +
            " RIGHT JOIN user_code c ON u.id = c.user_id " +
            " WHERE " +
            "c.value = ?  " +
            "AND  u.phone_number = ? " +
            "AND c.date_of_creation > ? " +
            "AND c.active = true";

    private static final String SELECT_ID_WHERE_EMAIL_IS_OR_PHONE_NUMBER_IS =
            "SELECT id FROM \"user\" WHERE " +
                    "(email = ? or phone_number = ?) AND " +
                    "(user_state = ? OR user_state = ? AND date_registration < ? )";

    private static final String UPDATE_A_LOT_OF_USER_FIELDS_BY_ID = "UPDATE \"user\" " +
            "SET phone_number = ?, password = ?, " +
            "city = ?, country = ?, email = ?, " +
            "firstname = ?, lastname = ?, male = ? " +
            "WHERE id = ? ";

    private static final String UPDATE_ACTIVE_USER_BY_ID = "UPDATE \"user\" SET active = ?  WHERE id = ? ";

    private static final String UPDATE_USER_STATE_BY_ID = "UPDATE \"user\" SET user_state = ?  WHERE id = ? ";

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insertUser;
    private final RowMapper<User> rowMapper;

    public UserRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.rowMapper = new BeanPropertyRowMapper<>(User.class);
        this.insertUser = new SimpleJdbcInsert(jdbc.getDataSource())
                .withTableName("\"user\"")
                .usingGeneratedKeyColumns("id")
                .usingColumns("phone_number", "password", "city", "country", "email", "firstname",
                        "lastname", "date_registration", "male", "user_state", "role_id");
    }

    @Override
    public List<User> findAll(Page pageable) {
        return jdbc.query(String.format(SELECT_ORDER_BY_LIMIT_OFFSET,
                        pageable.getOrderByOrDefault("date_registration"),
                        pageable.getDirectionOrDefault(Page.Direction.DESC),
                        pageable.getLimitOrDefault(100L),
                        pageable.getOffsetOrDefault(0L)),
                this.rowMapper);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jdbc.queryForStream(SELECT_USER_BY_ID, this.rowMapper, id).findFirst();
    }

    @Override
    public Optional<User> findByPhoneNumberAndActiveTrueAndUserStateRegistered(String number) {
        return jdbc.queryForStream(SELECT_USER_WHERE_PHONE_NUMBER_AND_ACTIVE_TRUE_AND_STATE_REGISTERED,
                this.rowMapper, number, UserState.REGISTERED).findFirst();
    }

    @Override
    public Optional<User> findByUserCodeAndPhoneNumberAndActiveTrueAndDateOfCreationAfter(String code, String number, LocalDateTime dateOfCreation) {
        return jdbc.queryForStream(SELECT_USER_WHERE_CODE_AND_NUMBER_ID_AND_DATE_OD_CREATION_AFTER,
                this.rowMapper, code, number, dateOfCreation).findFirst();
    }

    @Override
    public Optional<Long> saveAndReturnUserId(User user) {
        Map<String,Integer> idByRoleName = this.getMapFindIdByRoleName();
        Map<String, Object> args = new HashMap<>();

        args.put("phone_number", user.getPhoneNumber());
        args.put("password", user.getPassword());
        args.put("city", user.getCity());
        args.put("country", user.getCountry());
        args.put("email", user.getEmail());
        args.put("firstname", user.getFirstname());
        args.put("lastname", user.getLastname());
        args.put("date_registration", Timestamp.valueOf(user.getDateRegistration()));
        args.put("male", user.isMale());
        args.put("user_state", user.getUserState().name());
        args.put("role_id", idByRoleName.get(user.getRole().name()).intValue());

        Long id = (Long) insertUser.executeAndReturnKey(args);

        return Optional.ofNullable(id);
    }

    public Map<String, Integer> getMapFindIdByRoleName() {
        Map<String, Integer> idByRoleName = new HashMap<>();

        jdbc.queryForList("SELECT id,name FROM role")
                .forEach(
                        map -> idByRoleName.put(
                                map.get("name").toString(),
                                Integer.valueOf(map.get("id").toString())
                        ));

        return Collections.unmodifiableMap(idByRoleName);
    }

    @Override
    public boolean isBooked(String email, String phoneNumber, LocalDateTime dataBefore) {

        List<Long> ids = jdbc.query(SELECT_ID_WHERE_EMAIL_IS_OR_PHONE_NUMBER_IS, (r, i) -> r.getLong("id"),
                email, phoneNumber, UserState.REGISTERED.name(), UserState.REGISTRATION.name(), Timestamp.valueOf(dataBefore));

        return !ids.isEmpty();
    }

    @Override
    public boolean update(User user) {
        return jdbc.update(UPDATE_A_LOT_OF_USER_FIELDS_BY_ID, user.getPhoneNumber(), user.getPassword(),
                user.getCity(), user.getCountry(), user.getEmail(), user.getFirstname(),
                user.getLastname(), user.isMale(), user.getId()) != 0;
    }

    @Override
    public boolean updateActiveById(boolean active, Long userId) {
        return jdbc.update(UPDATE_ACTIVE_USER_BY_ID, active, userId) != 0;
    }

    @Override
    public boolean updateUserStateById(UserState userState, Long userId) {
        return jdbc.update(UPDATE_USER_STATE_BY_ID, userState.name(), userId) != 0;
    }

}
