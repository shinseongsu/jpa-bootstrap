package persistence.event.load;

import persistence.metadata.MetaModel;

import java.util.List;

public class DefaultLoadEventListener implements LoadEventListener {

    private final MetaModel metaModel;

    public DefaultLoadEventListener(final MetaModel metaModel) {
        this.metaModel = metaModel;
    }

    @Override
    public <T> T onLoad(LoadEvent<T> loadEvent) {
        return metaModel.getEntityLoader(loadEvent.getClazz())
                .find(loadEvent.getClazz(), loadEvent.getEntityId());
    }

    @Override
    public <T> List<T> onLoadAll(LoadEvent<T> loadEvent) {
        return metaModel.getEntityLoader(loadEvent.getClazz())
                .findAll(loadEvent.getClazz());
    }

    @Override
    public <T> T onEagerLoad(LoadEvent<T> loadEvent) {
        return metaModel.getEntityEagerLoader(loadEvent.getClazz())
                .find(loadEvent.getClazz(), loadEvent.getEntityId());
    }
}
