package com.beeline.beelineapplication.kafkaDataSet;

import com.google.gson.Gson;
import com.ssd.mvd.gpstabletsservice.database.CassandraConverter;

public class SerDes extends CassandraConverter {
    private final Gson gson = new Gson();

    private Gson getGson () { return this.gson; }

    protected  <T> String serialize ( final T object ) {
        return this.getGson().toJson( object );
    }

    protected <T> T deserialize ( final String value, final Class<T> clazz ) {
        return this.getGson().fromJson( value, clazz );
    }
}
