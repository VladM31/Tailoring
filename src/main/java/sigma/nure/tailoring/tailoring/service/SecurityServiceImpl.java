package sigma.nure.tailoring.tailoring.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import sigma.nure.tailoring.tailoring.entities.TelegramMessage;
import sigma.nure.tailoring.tailoring.entities.User;
import sigma.nure.tailoring.tailoring.entities.UserState;
import sigma.nure.tailoring.tailoring.tools.ConfirmRegistrationForm;
import sigma.nure.tailoring.tailoring.tools.Page;
import sigma.nure.tailoring.tailoring.tools.RegistrationUserForm;
import sigma.nure.tailoring.tailoring.tools.UserSearchCriteria;

import java.util.List;

public class SecurityServiceImpl implements SecurityService {
    private static final String MESSAGE_TEXT = "%s %s, your confirmation code is %s";
    private static final User USER_NOT_FOUND = null;

    private final UserService userService;
    private final UserCodeService userCodeService;
    private final TelegramBotClient botClient;

    public SecurityServiceImpl(UserService userService, UserCodeService userCodeService, TelegramBotClient botClient) {
        this.userService = userService;
        this.userCodeService = userCodeService;
        this.botClient = botClient;
    }

    @Override
    public boolean saveUser(RegistrationUserForm registrationUserForm) {
        if (userService.isBooked(registrationUserForm.getEmail(), registrationUserForm.getPhoneNumber())) {
            return false;
        }

        User user = registrationUserForm.toUser();

        Long userId = userService.saveAndReturnUserId(user).orElseThrow();

        final String code = userCodeService.generateCode();

        userCodeService.insertCode(userId, code);

        TelegramMessage message = getCodeMessage(user, code);

        botClient.sendMessage(message);

        return true;
    }

    @Override
    public boolean confirmRegistration(ConfirmRegistrationForm confirmRegistrationForm) {
        User user = userService
                .findByWorkCodeAndPhoneNumber(confirmRegistrationForm.getCode(), confirmRegistrationForm.getNumber())
                .orElse(USER_NOT_FOUND);

        if (user == USER_NOT_FOUND) {
            return false;
        }

        user.setUserState(UserState.REGISTERED);

        userCodeService.updateCode(user.getId(), confirmRegistrationForm.getCode());

        return userService.update(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userService.findBy(UserSearchCriteria.builder().username(username).build(), new Page())
                .stream()
                .findFirst()
                .orElse(USER_NOT_FOUND);
    }

    private TelegramMessage getCodeMessage(User user, String code) {
        TelegramMessage message = new TelegramMessage();

        message.setPhoneNumber(user.getPhoneNumber());

        String text = MESSAGE_TEXT.formatted(user.getFirstname(), user.getLastname(), code);

        message.setDescribe(text);

        MessageEntity style = MessageEntity.builder()
                .type("spoiler")
                .offset(text.length() - code.length())
                .length(code.length())
                .build();

        message.setMessageEntities(List.of(style));

        return message;
    }


}