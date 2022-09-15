package sigma.nure.tailoring.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range<T> {
    private T from;
    private T to;

    @Nullable
    public static <T> T from(@Nullable Range<T> range) {
        return Optional.ofNullable(range).map(Range::getFrom).orElse(null);
    }

    @Nullable
    public static <T> T to(@Nullable Range<T> range) {
        return Optional.ofNullable(range).map(Range::getTo).orElse(null);
    }
}
