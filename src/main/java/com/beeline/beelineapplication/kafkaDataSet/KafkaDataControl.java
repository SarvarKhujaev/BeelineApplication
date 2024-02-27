package com.beeline.beelineapplication.kafkaDataSet;

import com.beeline.beelineapplication.database.PostgreDataControl;
import com.beeline.beelineapplication.BeelineApplication;
import com.beeline.beelineapplication.entities.Order;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.KafkaStreams;

import java.util.function.Supplier;
import java.util.Properties;

public final class KafkaDataControl extends SerDes {
    private Properties getProperties() {
        return this.properties;
    }

    private String getORDER_STORAGE_TOPIC() {
        return this.ORDER_STORAGE_TOPIC;
    }

    private KafkaStreams getKafkaStreams() {
        return this.kafkaStreams;
    }

    private StreamsBuilder getBuilder() {
        return this.builder;
    }

    private String getKAFKA_BROKER() {
        return this.KAFKA_BROKER;
    }

    private String getGROUP_ID_FOR_KAFKA() {
        return this.GROUP_ID_FOR_KAFKA;
    }

    private void setKafkaStreams( final KafkaStreams kafkaStreams ) {
        this.kafkaStreams = kafkaStreams;
    }

    private Properties properties;

    private final String ORDER_STORAGE_TOPIC = BeelineApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_VARIABLES.KAFKA_TOPICS.ORDER_STORAGE_TOPIC" );

    private final String KAFKA_BROKER = BeelineApplication // Хост брокера Kafka
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_VARIABLES.KAFKA_BROKER" );

    private final String GROUP_ID_FOR_KAFKA = BeelineApplication // GROUP ID для Kafka
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_VARIABLES.GROUP_ID_FOR_KAFKA" );

    private KafkaStreams kafkaStreams;
    private final StreamsBuilder builder = new StreamsBuilder();

    private final Supplier< Properties > getStreamProperties = () -> {
        this.getProperties().clear();
        this.getProperties().put( StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, this.getKAFKA_BROKER() );
        this.getProperties().put( StreamsConfig.APPLICATION_ID_CONFIG, this.getGROUP_ID_FOR_KAFKA() );
        this.getProperties().put( StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName() );
        this.getProperties().put( StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName() );
        return this.getProperties();
    };

    public KafkaDataControl() {
        super.logging( this.getClass().getName() + " was created" );

        /*
        запускаем поток для чтения сообщений из топика
         */
        this.start();
    }

    /*
    отвечает за прослушивание топика и обратку всех сообщений
    */
    private void start () {
            final KStream< String, String > kStreamForGpsTablets = this.getBuilder().stream(
                    this.getORDER_STORAGE_TOPIC(), Consumed.with( Serdes.String(), Serdes.String() )
            );

            /*
            считываем сообщения из топика и записываем в БД
             */
            kStreamForGpsTablets
                    .mapValues( value -> super.deserialize( value, Order.class ) )
                    .mapValues( order -> PostgreDataControl.getInstance().save( order ) );

            this.setKafkaStreams( new KafkaStreams( this.getBuilder().build(), this.getStreamProperties.get() ) );
            this.getKafkaStreams().start();
    }
}
