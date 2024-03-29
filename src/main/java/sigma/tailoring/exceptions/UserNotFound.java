package sigma.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }
}
