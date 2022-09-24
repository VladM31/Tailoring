package sigma.nure.tailoring.tailoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import sigma.nure.tailoring.tailoring.converters.UserServiceSortColumnConverter;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.repository.UserCodeRepository;
import sigma.nure.tailoring.tailoring.repository.UserRepository;
import sigma.nure.tailoring.tailoring.service.PopularTemplateService;
import sigma.nure.tailoring.tailoring.service.PopularTemplateServiceImpl;
import sigma.nure.tailoring.tailoring.service.UserCodeService;
import sigma.nure.tailoring.tailoring.service.UserCodeServiceImpl;
import sigma.nure.tailoring.tailoring.service.UserService;
import sigma.nure.tailoring.tailoring.service.UserServiceImpl;

import sigma.nure.tailoring.tailoring.service.TelegramBotClient;

import java.util.List;
import java.util.Random;

@Configuration
@EnableScheduling
public class ServiceConfig {

    @Bean
    public PopularTemplateService popularTemplateServiceImpl(OrderRepository orderRepository, TailoringTemplateRepository templateRepository) {
        return new PopularTemplateServiceImpl(orderRepository, templateRepository);
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

    public UserService userServiceImpl(UserRepository userRepository,
                                       @Value("${minutes.waiting.for.user.registration}") long minutesForWork,
                                       UserServiceSortColumnConverter converter) {
        return new UserServiceImpl(converter, userRepository, minutesForWork);
    }
}
