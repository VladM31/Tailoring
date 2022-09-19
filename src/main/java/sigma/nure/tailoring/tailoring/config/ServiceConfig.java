package sigma.nure.tailoring.tailoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.service.PopularTemplateService;
import sigma.nure.tailoring.tailoring.service.PopularTemplateServiceImpl;
import sigma.nure.tailoring.tailoring.service.UserService;
import sigma.nure.tailoring.tailoring.service.UserServiceImpl;

@Configuration
@EnableScheduling
public class ServiceConfig {

    @Bean
    public PopularTemplateService popularTemplateServiceImpl(OrderRepository orderRepository, TailoringTemplateRepository templateRepository) {
        return new PopularTemplateServiceImpl(orderRepository, templateRepository);
    }

    @Bean
    public UserService userServiceImpl(UserRepository userRepository,
                                       @Value("${minutes.waiting.for.user.registration}") long minutesForWork){
        return new UserServiceImpl(userRepository,minutesForWork);
    }
}
