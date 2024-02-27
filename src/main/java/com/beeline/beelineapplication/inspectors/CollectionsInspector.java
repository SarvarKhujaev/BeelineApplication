package com.beeline.beelineapplication.inspectors;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.*;

public class CollectionsInspector {
    protected CollectionsInspector () {}

    protected <T> List<T> emptyList () {
        return Collections.emptyList();
    }

    public <T> ArrayList<T> newList () {
        return new ArrayList<>();
    }

    public <T> void analyze (
            final Collection<T> someList,
            final Consumer<T> someConsumer ) {
        someList.forEach( someConsumer );
    }

    protected <T, V> void analyze (
            final Map< T, V > someList,
            final BiConsumer<T, V> someConsumer ) {
        someList.forEach( someConsumer );
    }

    protected <T> boolean isCollectionNotEmpty ( final Collection<T> collection ) {
        return collection != null && !collection.isEmpty();
    }
}
