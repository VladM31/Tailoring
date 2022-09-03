package sigma.nure.tailoring.tailoring.repository;


import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

public class UserCodeRepositoryJdbcTemplatePostgres implements UserCodeRepository{

    private final JdbcTemplate jdbc;

    public UserCodeRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    private static final String UPDATE_INACTIVE_BY_USER_ID_AND_CODE_VALUE =
            "UPDATE user_code SET active = false WHERE user_id = ? AND value = ?";
    @Override
    public boolean updateCodeToInactiveByUserIdAndCodValue(Long userId, String codeValue) {
        return jdbc.update(UPDATE_INACTIVE_BY_USER_ID_AND_CODE_VALUE,userId,codeValue) != 0;
    }

    private static final String INSERT_USER_CODE = "INSERT INTO user_code(user_id,value,date_of_creation) VALUES(?,?,?)";
    @Override
    public boolean save(Long userId, String codeValue) {
        return jdbc.update(INSERT_USER_CODE,userId,codeValue, LocalDateTime.now()) != 0;
    }
}
