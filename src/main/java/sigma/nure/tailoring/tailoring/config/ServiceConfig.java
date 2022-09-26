package sigma.nure.tailoring.tailoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import sigma.nure.tailoring.tailoring.converters.UserServiceSortColumnConverter;
import sigma.nure.tailoring.tailoring.converters.FileConverter;
import sigma.nure.tailoring.tailoring.converters.TailoringTemplateConvertor;
import sigma.nure.tailoring.tailoring.converters.TailoringTemplateSortColumnConverter;
import sigma.nure.tailoring.tailoring.repository.MaterialsRepository;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.repository.UserCodeRepository;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.service.*;



import sigma.nure.tailoring.tailoring.service.PopularTemplateService;
import sigma.nure.tailoring.tailoring.service.PopularTemplateServiceImpl;
import sigma.nure.tailoring.tailoring.service.TailoringTemplateService;
import sigma.nure.tailoring.tailoring.service.TailoringTemplateServiceImpl;

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
    public TailoringTemplateService tailoringTemplateServiceImpl(
            FileConverter fileConverter,
            TailoringTemplateRepository templateRepository,
            @Value("${template.image.directory}") String imagesDirectory,
            TailoringTemplateSortColumnConverter tailoringTemplateSortColumnConverter,
            TailoringTemplateConvertor tailoringTemplateConvertor,
            MaterialsRepository materialsRepository) {

        return new TailoringTemplateServiceImpl(fileConverter, templateRepository, imagesDirectory,
                materialsRepository, tailoringTemplateSortColumnConverter, tailoringTemplateConvertor);
    }
}
