package sigma.tailoring.repository;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

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

    public <T> Iterable<String> getStringIterableFromOtherTypeIterable(@Nullable Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(obj -> obj.toString())
                .collect(Collectors.toSet());
    }

}
