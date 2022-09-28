package sigma.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderCommentException extends RuntimeException {

    public OrderCommentException(String message) {
        super(message);
    }
}
