package sigma.nure.tailoring.tailoring.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import sigma.nure.tailoring.tailoring.entities.MessageForUser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TelegramBotServiceImplementedOnHttp implements TelegramBotService{
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotServiceImplementedOnHttp.class);
    private final String token;
    private final String urlHasNumber;
    private final String urlSendMessage;
    private final Gson jsonConverter;

    public TelegramBotServiceImplementedOnHttp(@Value("${telegram.bot.connector.token}") String token,
                                               @Value("${telegram.bot.url.has.phone.number}") String urlHasNumber,
                                               @Value("${telegram.bot.url.has.send.message}") String urlSendMessage) {
        this.token = token;
        this.urlSendMessage = urlSendMessage;
        this.urlHasNumber = urlHasNumber + "?token=" + token + "&phoneNumber=";
        this.jsonConverter = new Gson();
    }


    @Override
    public boolean hasPhoneNumber(String phoneNumber) {

        System.out.println(urlHasNumber + phoneNumber);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlHasNumber + phoneNumber))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            LOGGER.warn("Telegram Bot Service: response phone number has error",e);
            return false;
        }

        return response.body().equals("true");
    }

    @Override
    public boolean sendMessage(MessageForUser message) {
        return false;
    }


}
