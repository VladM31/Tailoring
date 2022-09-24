package sigma.nure.tailoring.tailoring.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.Range;
import sigma.nure.tailoring.tailoring.tools.UserSearchCriteria;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class JdbcTemplateUserRepository implements UserRepository {

    private static final String SELECT_USER = "SELECT u.id, u.password, u.city, " +
            "u.country, u.email, u.firstname, u.lastname, u.active, u.male, " +
            "u.user_state AS userState, " +
            "u.phone_number AS phoneNumber, " +
            "u.date_registration AS dateRegistration, " +
            "r.name AS role " +
            "FROM \"user\" u " +
            "LEFT JOIN role r ON r.id = u.role_id ";

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
            "firstname = ?, lastname = ?, male = ?, user_state = ?, " +
            "password = ?, role_id = ?, active = ? " +
            "WHERE id = ? ";

    private static final String SELECT_WHERE_FIELDS_ARE = SELECT_USER +
            "WHERE " +
            "(:idsAreNull OR u.id IN(:ids)) AND " +
            "(:phoneNumberContaining::varchar IS NULL OR  u.phone_number LIKE CONCAT ('%',:phoneNumberContaining::varchar, '%')) AND " +
            "(:username::varchar IS NULL OR  u.phone_number = :username::varchar) AND " +
            "(:emailContaining::varchar IS NULL OR  u.email LIKE CONCAT ('%',:emailContaining::varchar, '%')) AND " +
            "(:cityContaining::varchar IS NULL OR  u.city LIKE CONCAT ('%',:cityContaining::varchar, '%')) AND " +
            "(:countryContaining::varchar IS NULL OR  u.country LIKE CONCAT ('%',:countryContaining::varchar, '%')) AND " +
            "(:firstnameContaining::varchar IS NULL OR  u.firstname LIKE CONCAT ('%',:firstnameContaining::varchar, '%')) AND " +
            "(:lastnameContaining::varchar IS NULL OR  u.lastname LIKE CONCAT ('%',:lastnameContaining::varchar, '%')) AND " +
            "(:afterOrEqualsDataRegistration::timestamp IS NULL OR  u.date_registration >= :afterOrEqualsDataRegistration::timestamp) AND " +
            "(:beforeOrEqualsDataRegistration::timestamp IS NULL OR  u.date_registration <= :beforeOrEqualsDataRegistration::timestamp)  AND " +
            "(:activeUser::boolean IS NULL OR  u.active = :activeUser::boolean) AND " +
            "(:male::boolean IS NULL OR  u.male = :male::boolean) AND " +
            "(:userStatesAreNull OR u.user_state IN(:userStates::varchar)) AND " +
            "(:rolesAreNull OR r.name IN(:roles::varchar)) " +
            " ORDER BY :sortColumn :sortDirection LIMIT :limit OFFSET :offset ";

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insertUser;
    private final RowMapper<User> rowMapper;
    private final NamedParameterJdbcTemplate namedJdbc;
    private final RepositoryHandler handler;

    public JdbcTemplateUserRepository(JdbcTemplate jdbc, RepositoryHandler handler) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc.getDataSource());
        this.handler = handler;
        this.rowMapper = new BeanPropertyRowMapper<>(User.class);
        this.insertUser = new SimpleJdbcInsert(jdbc.getDataSource())
                .withTableName("\"user\"")
                .usingGeneratedKeyColumns("id")
                .usingColumns("phone_number", "password", "city", "country", "email", "firstname",
                        "lastname", "date_registration", "male", "user_state", "role_id");
    }

    @Override
    public List<User> findBy(UserSearchCriteria param, Page pageable) {
        final String sqlScriptWithPage = SELECT_WHERE_FIELDS_ARE
                .replaceFirst(":sortColumn", pageable.getOrderByOrDefault("date_registration"))
                .replaceFirst(":sortDirection", pageable.getDirectionOrDefault(Page.Direction.DESC).toString())
                .replaceFirst(":limit", pageable.getLimitOrDefault(100L).toString())
                .replaceFirst(":offset", pageable.getOffsetOrDefault(0L).toString());


        param.setIds(handler.getNullIfCollectionNullOrEmpty(param.getIds()));
        param.setUserStates(handler.getNullIfCollectionNullOrEmpty(param.getUserStates()));
        param.setRoles(handler.getNullIfCollectionNullOrEmpty(param.getRoles()));

        Map<String, Object> args = new HashMap<>();

        args.put("phoneNumberContaining", param.getPhoneNumber());
        args.put("username", param.getUsername());
        args.put("emailContaining", param.getEmail());
        args.put("cityContaining", param.getCity());
        args.put("countryContaining", param.getCountry());
        args.put("firstnameContaining", param.getFirstname());
        args.put("lastnameContaining", param.getLastname());
        args.put("afterOrEqualsDataRegistration", Range.from(param.getDataRegistration()));
        args.put("beforeOrEqualsDataRegistration", Range.to(param.getDataRegistration()));
        args.put("activeUser", param.getActive());
        args.put("male", param.getMale());

        args.put("idsAreNull", param.getIds() == null);
        args.put("ids", param.getIds());
        args.put("userStatesAreNull", param.getUserStates() == null);
        args.put("userStates", handler.getStringIterableFromOtherTypeIterable(param.getUserStates()));
        args.put("rolesAreNull", param.getRoles() == null);
        args.put("roles", handler.getStringIterableFromOtherTypeIterable(param.getRoles()));

        return namedJdbc.query(sqlScriptWithPage, args, rowMapper);
    }

    @Override
    public Optional<User> findByWorkCodeAndPhoneNumber(String code, String number, LocalDateTime dateOfCreation) {
        return jdbc.queryForStream(SELECT_USER_WHERE_CODE_AND_NUMBER_ID_AND_DATE_OD_CREATION_AFTER,
                this.rowMapper, code, number, dateOfCreation).findFirst();
    }

    @Override
    public Optional<Long> saveAndReturnUserId(User user) {
        Map<String, Integer> idByRoleName = this.getMapFindIdByRoleName();
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
        Map<String, Integer> idByRoleName = this.getMapFindIdByRoleName();
        return jdbc.update(UPDATE_A_LOT_OF_USER_FIELDS_BY_ID, user.getPhoneNumber(), user.getPassword(),
                user.getCity(), user.getCountry(), user.getEmail(), user.getFirstname(),
                user.getLastname(), user.isMale(), user.getUserState().name(), user.getId(),
                user.getPassword(), idByRoleName.get(user.getRole().name()), user.isActive()) != 0;
    }

}
