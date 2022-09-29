package sigma.tailoring.service;


import sigma.tailoring.entities.TelegramMessage;

public interface TelegramBotClient {

    boolean hasPhoneNumber(String phoneNumber);

    boolean sendMessage(TelegramMessage message);
}
