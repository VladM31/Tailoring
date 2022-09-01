package sigma.nure.tailoring.tailoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.service.UserService;
import sigma.nure.tailoring.tailoring.service.UserServiceJdbcTemplatePostgres;

@Configuration
public class ServiceConfig {

    @Bean
    public UserService userRepositoryJdbcTemplatePostgres( UserRepository repository,
            @Value("${minutes.waiting.for.user.registration}") long waitUserRegistration){
        return new UserServiceJdbcTemplatePostgres(repository,waitUserRegistration);
    }
}
