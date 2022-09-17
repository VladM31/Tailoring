package sigma.nure.tailoring.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TriedToTakeSecurityDataException extends RuntimeException {

    public TriedToTakeSecurityDataException(String message) {
        super(message);
    }
}
