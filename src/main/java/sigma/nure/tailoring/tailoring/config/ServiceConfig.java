package sigma.nure.tailoring.tailoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import sigma.nure.tailoring.tailoring.converters.FileConverter;
import sigma.nure.tailoring.tailoring.converters.OrderServiceSortColumnConverter;
import sigma.nure.tailoring.tailoring.converters.UserServiceSortColumnConverter;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.repository.UserCodeRepository;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.service.*;




@Configuration
@EnableScheduling
public class ServiceConfig {

    @Bean
    public PopularTemplateService popularTemplateServiceImpl(OrderRepository orderRepository, TailoringTemplateRepository templateRepository) {
        return new PopularTemplateServiceImpl(orderRepository, templateRepository);
    }

    @Bean
    public UserCodeService userCodeService(UserCodeRepository userCodeRepository) {
        return new UserCodeServiceImpl(userCodeRepository);
    }

    @Bean
    public UserService userServiceImpl(UserRepository userRepository,
                                       @Value("${minutes.waiting.for.user.registration}") long minutesForWork,
                                       UserServiceSortColumnConverter converter) {
        return new UserServiceImpl(converter, userRepository, minutesForWork);
    }

    @Bean
    public TailoringOrderService tailoringOrderServiceImpl(
            OrderServiceSortColumnConverter sortColumnConverter,
            OrderRepository orderRepository,
            @Value("${order.image.directory}") String directory,
            FileConverter fileConverter) {
        return new TailoringOrderServiceImpl(sortColumnConverter, orderRepository, fileConverter, directory);
    }
}
