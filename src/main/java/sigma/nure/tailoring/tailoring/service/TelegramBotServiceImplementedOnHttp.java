package sigma.nure.tailoring.tailoring.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import sigma.nure.tailoring.tailoring.entities.MessageForUser;

public class TelegramBotServiceImplementedOnHttp implements TelegramBotService{
    private final String token;
    private final Gson jsonConverter;

    public TelegramBotServiceImplementedOnHttp(@Value("${telegram.bot.connector.token}")String token) {
        this.token = token;
        this.jsonConverter = new Gson();
    }


    @Override
    public boolean hasPhoneNumber(String phoneNumber) {
        return false;
    }

    @Override
    public boolean sendMessage(MessageForUser message) {
        return false;
    }
}
