package sigma.nure.tailoring.tailoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import sigma.nure.tailoring.tailoring.repository.RepositoryHandler;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.repository.UserRepositoryJdbcTemplatePostgres;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository jdbcTemplateUserRepository(JdbcTemplate jdbc, RepositoryHandler handler) {
        return new UserRepositoryJdbcTemplatePostgres(jdbc, handler);
    }
}
