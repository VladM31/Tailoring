package sigma.nure.tailoring.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SetCommentWithoutRightsException extends RuntimeException {

    public SetCommentWithoutRightsException(String message) {
        super(message);
    }
}
