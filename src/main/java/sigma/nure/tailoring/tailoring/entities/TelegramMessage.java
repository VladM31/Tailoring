package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TelegramMessage {
    private String phoneNumber;
    private String describe;
    private List<MessageEntity> messageEntities;

    public boolean isEmptyNumber() {
        return phoneNumber.trim().isEmpty();
    }

}
