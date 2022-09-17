package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseException extends RuntimeException {
    private HttpStatus status;
    private String errorMessage;
    private String answerMessage;

    public ResponseException bindingResultToMessage(BindingResult bindingResult) {
        String bindingResultMessage = bindingResult
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.mapping(e -> e.getDefaultMessage(),
                                Collectors.joining("\n"))
                );

        answerMessage = answerMessage == null ?
                bindingResultMessage : answerMessage + "\n" + bindingResultMessage;

        return this;
    }

    public void log(Logger logger) {
        final int codeError = Optional.ofNullable(status).map(s -> s.value()).orElse(500);

        if (codeError >= 400 && codeError <= 499) {
            logger.warn(this.answerMessage, this);
            return;
        }
        logger.error(this.errorMessage, this);
    }

    public String getMessage() {
        final int codeError = Optional.ofNullable(status).map(s -> s.value()).orElse(500);

        if (codeError >= 400 && codeError <= 499) {
            return this.answerMessage;
        }
        return this.errorMessage;
    }


}
