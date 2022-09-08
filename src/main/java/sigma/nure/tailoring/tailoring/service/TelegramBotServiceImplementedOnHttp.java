package sigma.nure.tailoring.tailoring.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import sigma.nure.tailoring.tailoring.entities.MessageForUser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TelegramBotServiceImplementedOnHttp implements TelegramBotService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBotServiceImplementedOnHttp.class);
    private final String token;
    private final String urlHasNumber;
    private final String urlSendMessage;
    private final Gson jsonConverter;

    public TelegramBotServiceImplementedOnHttp(String token,
                                               String urlHasNumber,
                                               String urlSendMessage) {
        this.token = token;
        this.urlSendMessage = urlSendMessage;
        this.urlHasNumber = urlHasNumber + "?token=" + token + "&phoneNumber=";
        this.jsonConverter = new Gson();
    }


    @Override
    public boolean hasPhoneNumber(String phoneNumber) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(urlHasNumber + phoneNumber))
                .GET()
                .build();

        return sendRequestAndReturnBooleanAnswer(httpRequest,
                "Telegram Bot Service: response phone number has error");
    }

    @Override
    public boolean sendMessage(MessageForUser message) {
        final Map<String, String> values = new HashMap<>();
        values.put("token", this.token);
        values.put("json", this.jsonConverter.toJson(message));

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(this.urlSendMessage))
                .POST(ofFormData(values))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return sendRequestAndReturnBooleanAnswer(httpRequest,
                "Telegram Bot Service: response send message has error");
    }

    private static boolean sendRequestAndReturnBooleanAnswer(HttpRequest httpRequest, String messageError) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;

        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            LOGGER.warn(messageError, e);
            return false;
        }

        return response.body().equals("true");
    }

    private static HttpRequest.BodyPublisher ofFormData(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

}
