package sigma.tailoring.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormException extends RuntimeException {
    private Object form;
    private String formName;
    private String pageName;
    private String errorName;

    public FormException(String message, Object form, String formName, String pageName, String errorName) {
        super(message);
        this.form = form;
        this.formName = formName;
        this.pageName = pageName;
        this.errorName = errorName;
    }


}
