package sigma.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
