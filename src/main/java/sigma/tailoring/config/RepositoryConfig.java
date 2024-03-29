package sigma.tailoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import sigma.tailoring.repository.*;
import sigma.tailoring.repository.*;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository jdbcTemplateUserRepository(JdbcTemplate jdbc, RepositoryHandler handler) {
        return new JdbcTemplateUserRepository(jdbc, handler);
    }

    @Bean
    public MaterialsRepository jdbcTemplateMaterialsRepository(JdbcTemplate jdbc) {
        return new JdbcTemplatePostgresMaterialsRepository(jdbc);
    }

    @Bean
    public UserCodeRepository userCodeRepository(JdbcTemplate jdbc) {
        return new JdbcTemplatePostgresUserCodeRepository(jdbc);
    }

    @Bean
    public OrderCommentsRepository jdbcTemplateOrderCommentsRepository(JdbcTemplate jdbc) {
        return new JdbcTemplateOrderCommentsRepository(jdbc);
    }
}
