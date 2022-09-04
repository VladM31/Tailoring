package sigma.nure.tailoring.tailoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.repository.UserRepositoryJdbcTemplatePostgres;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository JdbcTemplateUserRepository (JdbcTemplate jdbc,DataSource dataSource){
        return new UserRepositoryJdbcTemplatePostgres(jdbc,dataSource);
    }
}
