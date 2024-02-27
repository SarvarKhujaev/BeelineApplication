package com.beeline.beelineapplication.kafkaDataSet;

import com.beeline.beelineapplication.inspectors.LogInspector;
import com.google.gson.Gson;

public class SerDes extends LogInspector {
    private final Gson gson = new Gson();

    protected SerDes () {}

    private Gson getGson () {
        return this.gson;
    }

    protected  <T> String serialize ( final T object ) {
        return this.getGson().toJson( object );
    }

    protected <T> T deserialize ( final String value, final Class<T> clazz ) {
        return this.getGson().fromJson( value, clazz );
    }
}
