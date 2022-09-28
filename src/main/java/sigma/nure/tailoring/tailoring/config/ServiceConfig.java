package sigma.nure.tailoring.tailoring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import sigma.nure.tailoring.tailoring.converters.FileConverter;
import sigma.nure.tailoring.tailoring.converters.OrderServiceSortColumnConverter;
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
    public TailoringTemplateService tailoringTemplateServiceImpl(
            FileConverter fileConverter,
            TailoringTemplateRepository templateRepository,
            @Value("${template.image.directory}") String imagesDirectory,
            TailoringTemplateSortColumnConverter tailoringTemplateSortColumnConverter,
            TailoringTemplateConvertor tailoringTemplateConvertor) {
        return new TailoringTemplateServiceImpl(fileConverter, templateRepository, imagesDirectory,
                tailoringTemplateSortColumnConverter, tailoringTemplateConvertor);
    }

    @Bean
    public TelegramBotClient httpTelegramBotClient(@Value("${telegram.bot.connector.token}") String token,
                                                   @Value("${token.param.name}") String tokenParamName,
                                                   @Value("${phone.number.param.name}") String phoneNumberParamName,
                                                   @Value("${json.param.name}") String jsonParamName,
                                                   @Value("${telegram.bot.url.has.phone.number}") String telegramBotDbUrl,
                                                   @Value("${telegram.bot.url.has.send.message}") String telegramBotUrl) {
        return new HttpTelegramBotClient(token, tokenParamName, phoneNumberParamName,
                jsonParamName, telegramBotDbUrl, telegramBotUrl);
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
    public CommentService commentServiceImpl(OrderCommentsRepository orderCommentsRepository,
                                             OrderRepository orderRepository) {
        return new CommentServiceImpl(orderCommentsRepository, orderRepository);
    }

    @Bean

    public SecurityService securityServiceImpl(UserService userService, UserCodeService userCodeService, TelegramBotClient telegramBotClient) {
        return new SecurityServiceImpl(userService, userCodeService, telegramBotClient);
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
