package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.StringUtils;
import sigma.tailoring.entities.PartSizeForTemplate;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartSizeTemplateForm {
    public static final String SUCCESSFUL = "";

    private String name;
    private Set<Integer> width;
    private Set<Integer> volume;
    private Set<Integer> length;
    private Set<Integer> height;

    public PartSizeError getPartSizeError() {
        if (StringUtils.isEmptyOrWhitespace(name)) {
            return new PartSizeError("Part size name mustn't be blank");
        }
        if (name.length() > 60) {
            return new PartSizeError("Part size name must be less than 60");
        }
        if (hasElementLessThanOne(width)) {
            return new PartSizeError("Width less than 1");
        }
        if (hasElementLessThanOne(height)) {
            return new PartSizeError("Height less than 1");
        }
        if (hasElementLessThanOne(length)) {
            return new PartSizeError("Length less than 1");
        }
        if (hasElementLessThanOne(height)) {
            return new PartSizeError("Volume less than 1");
        }
        if (CollectionUtils.isEmpty(width)
                && CollectionUtils.isEmpty(volume)
                && CollectionUtils.isEmpty(length)
                && CollectionUtils.isEmpty(height)) {
            return new PartSizeError("Part size with name \"" + name + "\" hasn't any size!");
        }
        return new PartSizeError(SUCCESSFUL);
    }

    public record PartSizeError(String message) {
        public boolean hasError() {
            return !this.message.equals(SUCCESSFUL);
        }
    }

    private boolean hasElementLessThanOne(Set<Integer> integerSet) {
        if (integerSet == null) {
            return false;
        }
        for (int integer : integerSet) {
            if (integer < 1) {
                return true;
            }
        }
        return false;
    }

    public PartSizeForTemplate toPartSizeForTemplate() {
        return new PartSizeForTemplate(name, width, volume, length, height);
    }
}
