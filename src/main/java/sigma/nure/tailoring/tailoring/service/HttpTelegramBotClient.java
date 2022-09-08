package sigma.nure.tailoring.tailoring.service;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sigma.nure.tailoring.tailoring.entities.MessageForUser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpTelegramBotClient implements TelegramBotClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpTelegramBotClient.class);

    private final Gson jsonConverter;
    private final String token;
    private final String tokenParamName;
    private final String phoneNumberParamName;
    private final String jsonParamName;
    private final String urlHasNumber;
    private final String urlSendMessage;

    public HttpTelegramBotClient(String token, String tokenParamName, String phoneNumberParamName,
                                 String jsonParamName, String urlHasNumber, String urlSendMessage) {
        this.token = token;
        this.tokenParamName = tokenParamName;
        this.phoneNumberParamName = phoneNumberParamName;
        this.jsonParamName = jsonParamName;
        this.urlSendMessage = urlSendMessage;
        this.urlHasNumber = urlHasNumber;
        this.jsonConverter = new Gson();
    }

    @Override
    public boolean hasPhoneNumber(String phoneNumber) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(this.urlHasNumber +
                        "?" + this.tokenParamName + "=" + this.token +
                        "&" + this.phoneNumberParamName + "=" + phoneNumber))
                .GET()
                .build();

        return send(httpRequest,
                "Telegram Bot Client: response phone number has error");
    }

    @Override
    public boolean sendMessage(MessageForUser message) {
        final Map<String, String> values = Map.of(
                this.tokenParamName, this.token,
                this.jsonParamName, this.jsonConverter.toJson(message)
        );

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(this.urlSendMessage))
                .POST(ofFormData(values))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        return send(httpRequest,
                "Telegram Bot Client: response send message has error");
    }

    private static boolean send(HttpRequest httpRequest, String messageError) {
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
