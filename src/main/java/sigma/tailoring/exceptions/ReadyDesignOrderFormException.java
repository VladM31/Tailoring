package sigma.tailoring.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class ReadyDesignOrderFormException extends RuntimeException {
    private Map<String, Object> attributes;
    private String pageName;

    public ReadyDesignOrderFormException(String message, String pageName, Map<String, Object> attributes) {
        this(message);
        this.attributes = attributes;
        this.pageName = pageName;
    }

    public ReadyDesignOrderFormException(String message) {
        super(message);
    }
}
