package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page {
    private String orderBy;
    private Long limit;
    private Long offset;
    private Direction direction;

    public String getOrderByOrDefault(String defaultValue) {
        return orderBy != null ? orderBy : defaultValue;
    }

    public Long getLimitOrDefault(Long defaultValue) {
        return limit != null ? limit : defaultValue;
    }

    public Long getOffsetOrDefault(Long defaultValue) {
        return offset != null ? offset : defaultValue;
    }

    public Direction getDirectionOrDefault(Direction defaultValue) {
        return direction != null ? direction : defaultValue;
    }

    public enum Direction {
        DESC, ASC
    }
}
