package sigma.nure.tailoring.tailoring.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TriedSetCommentForAnotherUserOrderException extends RuntimeException {

    public TriedSetCommentForAnotherUserOrderException(String message) {
        super(message);
    }
}
