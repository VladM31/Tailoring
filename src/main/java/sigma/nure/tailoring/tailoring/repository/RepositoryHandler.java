package sigma.nure.tailoring.tailoring.repository;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import sigma.nure.tailoring.tailoring.tools.Page;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class RepositoryHandler {

    public <T> Iterable<T> getNullIfCollectionNullOrEmpty(Iterable<T> collection) {
        if (collection == null) {
            return null;
        }
        if (!collection.iterator().hasNext()) {
            return null;
        }
        return collection;
    }

    public <T> Iterable<String> getStringIterableFromEnumIterable(@Nullable Iterable<T> enums){
        if(enums == null){
            return null;
        }
        return StreamSupport.stream(enums.spliterator(),false)
                .map(obj -> obj.toString())
                .collect(Collectors.toSet());
    }

    public static final String ORDER_BY_AND_LIMIT = " ORDER BY %s %s LIMIT %s OFFSET %s";

    public String getScriptFromPage(Page page, String orderBy, Page.Direction direction,Long limit,Long offset){
        return String.format(ORDER_BY_AND_LIMIT,
                page.getOrderByOrDefault(orderBy),
                page.getDirectionOrDefault(direction),
                page.getLimitOrDefault(limit),
                page.getOffsetOrDefault(offset));
    }
}
