package sigma.nure.tailoring.tailoring.repository;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class RepositoryHandler {

    public static final String ORDER_BY_AND_LIMIT = " ORDER BY %s %s LIMIT %s OFFSET %s";

    public <T> Iterable<T> getNullIfCollectionNullOrEmpty(Iterable<T> collection) {
        if (collection == null) {
            return null;
        }
        if (!collection.iterator().hasNext()) {
            return null;
        }
        return collection;
    }

    public String getScriptFromPage(Page page, String defaultOrderBy,Page.Direction defaultDirection,
                                   Long defaultLimit,Long defaultOffset) {
        return String.format(ORDER_BY_AND_LIMIT,
                page.getOrderByOrDefault(defaultOrderBy),
                page.getDirectionOrDefault(defaultDirection),
                page.getLimitOrDefault(defaultLimit),
                page.getOffsetOrDefault(defaultOffset));
    }
    
    public <T extends Enum<?>> Iterable<String> getStringIterableFromEnumIterable(@Nullable Iterable<T> enums){
        if(enums == null){
            return null;
        }
        return StreamSupport.stream(enums.spliterator(),false).map(Enum::name).collect(Collectors.toSet());
    }
}
