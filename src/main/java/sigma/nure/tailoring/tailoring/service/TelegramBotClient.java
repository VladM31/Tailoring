package sigma.nure.tailoring.tailoring.service;


import sigma.nure.tailoring.tailoring.entities.MessageForUser;

public interface TelegramBotClient {

    boolean hasPhoneNumber(String phoneNumber);

    boolean sendMessage(MessageForUser message);
}