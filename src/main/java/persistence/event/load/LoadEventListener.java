package persistence.event.load;

import persistence.sql.entity.collection.CollectionLoader;

import java.util.List;

public interface LoadEventListener {

    <T> T onLoad(LoadEvent<T> loadEvent);

    <T> List<T> onLoadAll(LoadEvent<T> loadEvent);

    <T> T onEagerLoad(LoadEvent<T> loadEvent);
}
