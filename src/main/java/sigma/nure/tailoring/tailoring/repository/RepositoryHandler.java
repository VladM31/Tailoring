package sigma.nure.tailoring.tailoring.repository;

import org.springframework.stereotype.Component;

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
}
