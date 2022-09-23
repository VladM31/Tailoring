package sigma.nure.tailoring.tailoring.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import sigma.nure.tailoring.tailoring.entities.MessageForUser;
import sigma.nure.tailoring.tailoring.repository.OrderRepository;
import sigma.nure.tailoring.tailoring.repository.TailoringTemplateRepository;
import sigma.nure.tailoring.tailoring.service.HttpTelegramBotClient;
import sigma.nure.tailoring.tailoring.service.PopularTemplateService;
import sigma.nure.tailoring.tailoring.service.PopularTemplateServiceImpl;
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
}
