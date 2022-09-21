package sigma.nure.tailoring.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderCommentException extends RuntimeException {

    public OrderCommentException(String message) {
        super(message);
    }
}
