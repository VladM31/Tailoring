package sigma.nure.tailoring.tailoring.repository;

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

    public <T extends Enum<?>> Iterable<String> getStringIterableFromEnumIterable(@Nullable Iterable<T> enums){
        if(enums == null){
            return null;
        }
        return StreamSupport.stream(enums.spliterator(),false).map(Enum::name).collect(Collectors.toSet());
    }
}
