package sigma.nure.tailoring.tailoring.repository;


import org.springframework.jdbc.core.JdbcTemplate;

public class UserCodeRepositoryJdbcTemplatePostgres implements UserCodeRepository{

    private final JdbcTemplate jdbc;

    public UserCodeRepositoryJdbcTemplatePostgres(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public boolean updateCodeToInactiveByUserIdAndCodValue(Long userId, String codeValue) {
        return false;
    }

    @Override
    public boolean save(Long userId, String codeValue) {
        return false;
    }
}
