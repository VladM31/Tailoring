package sigma.nure.tailoring.tailoring.service;


import sigma.nure.tailoring.tailoring.entities.TelegramMessage;

public interface TelegramBotClient {

    boolean hasPhoneNumber(String phoneNumber);

    boolean sendMessage(TelegramMessage message);
}
