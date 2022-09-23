package sigma.nure.tailoring.tailoring.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(value = RUNTIME)
public @interface TailoringTemplateFormValidList {
    TailoringTemplateFormValid[] value();
}