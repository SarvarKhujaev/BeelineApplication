package com.beeline.beelineapplication.kafkaDataSet;

import com.google.gson.Gson;
import com.ssd.mvd.websocketservice.WebSocketServiceApplication;
import com.ssd.mvd.websocketservice.constants.LogInspector;
import com.ssd.mvd.websocketservice.entity.*;
import com.ssd.mvd.websocketservice.entity.entityForPapilon.CarTotalData;
import com.ssd.mvd.websocketservice.websocket.MessageSendingService;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

@lombok.Data
public class KafkaDataControl extends LogInspector {
    private Properties properties;
    private final AdminClient client;
    private final Gson gson = new Gson();
    private final MessageSendingService messageSendingService;

    private final String KAFKA_BROKER = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_BROKER" );

    private final String GROUP_ID_FOR_KAFKA = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.GROUP_ID_FOR_KAFKA" );

    private final String NEW_TUPLE_OF_CAR_TOPIC = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.NEW_TUPLE_OF_CAR_TOPIC" );

    private final String TUPLE_OF_CAR_LOCATION_TOPIC = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.TUPLE_OF_CAR_LOCATION_TOPIC" );

    private final String NEW_CAR_TOPIC = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.NEW_CAR_TOPIC" );

    private final String WEBSOCKET_SERVICE_TOPIC_FOR_ONLINE = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.WEBSOCKET_SERVICE_TOPIC_FOR_ONLINE" );

    private final String EXTERNAL_TABLETS_LOCATIONS = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.EXTERNAL_TABLETS_LOCATIONS" );

    private final String REDIS_CAR = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.REDIS_CAR" );

    private final String REDIS_PERSON = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.REDIS_PERSON" );

    private final String TABLETS_GPS_DATA = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.TABLETS_GPS_DATA" );

    private final String NOTIFICATION = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.NOTIFICATION" );

    // notification for front
    private final String SOS_TOPIC = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.SOS_TOPIC" );

    // notification for android
    private final String SOS_TOPIC_FOR_ANDROID_NOTIFICATION = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.SOS_TOPIC_FOR_ANDROID_NOTIFICATION" );

    private final String CAR_TOTAL_DATA = WebSocketServiceApplication
            .context
            .getEnvironment()
            .getProperty( "variables.KAFKA_TOPICS.CAR_TOTAL_DATA" );

    private KafkaStreams kafkaStreams;
    private final StreamsBuilder builder = new StreamsBuilder();

    private final Supplier< Properties > setStreamProperties = () -> {
        this.getProperties().clear();
        this.getProperties().put( StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, this.getKAFKA_BROKER() );
        this.getProperties().put( StreamsConfig.APPLICATION_ID_CONFIG, this.getGROUP_ID_FOR_KAFKA() );
        this.getProperties().put( StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName() );
        this.getProperties().put( StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName() );
        return this.getProperties();
    };

    private final Supplier< Properties > setProperties = () -> {
            this.setProperties( new Properties() );
            this.getProperties().put( AdminClientConfig.CLIENT_ID_CONFIG, this.getGROUP_ID_FOR_KAFKA() );
            this.getProperties().put( AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.getKAFKA_BROKER() );
            return getProperties();
    };

    private final Supplier< String > getNewTopic = () -> {
            List.of( this.getSOS_TOPIC_FOR_ANDROID_NOTIFICATION(),
                            this.getWEBSOCKET_SERVICE_TOPIC_FOR_ONLINE(),
                            this.getTUPLE_OF_CAR_LOCATION_TOPIC(),
                            this.getEXTERNAL_TABLETS_LOCATIONS(),
                            this.getNEW_TUPLE_OF_CAR_TOPIC(),
                            this.getTABLETS_GPS_DATA(),
                            this.getCAR_TOTAL_DATA(),
                            this.getNEW_CAR_TOPIC(),
                            this.getNOTIFICATION(),
                            this.getREDIS_PERSON(),
                            this.getREDIS_CAR(),
                            this.getSOS_TOPIC() )
                    .parallelStream()
                    .forEach( topic -> this.getClient()
                            .createTopics( Collections
                                    .singletonList(
                                            TopicBuilder
                                            .name( topic )
                                            .partitions( 5 )
                                            .replicas( 3 )
                                            .build() ) ) );
            return this.getKAFKA_BROKER();
    };

    public KafkaDataControl() {
        this.messageSendingService = WebSocketServiceApplication
                .context
                .getBean( MessageSendingService.class );
        this.client = KafkaAdminClient.create( this.getSetProperties().get() );
        super.logging( "KafkaDataControl was created" );
        this.getGetNewTopic().get();
        this.start();
    }

    private void start () {
            final KStream< String, String > kStreamForGpsTablets = this.getBuilder().stream(
                    this.getTABLETS_GPS_DATA(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForGpsTablets
                    .mapValues( value -> this.getGson().fromJson( value, ReqLocationExchange.class ) )
                    .mapValues( reqLocationExchange -> this.getMessageSendingService().sendMessage( reqLocationExchange ) );

            final KStream< String, String > kStreamForExternalTablets = this.getBuilder().stream(
                    this.getEXTERNAL_TABLETS_LOCATIONS(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForExternalTablets
                    .mapValues( value -> this.getGson().fromJson( value, TabletLocation.class ) )
                    .mapValues( tabletLocation -> this.getMessageSendingService().sendMessage( tabletLocation ) );

            final KStream< String, String > kStreamForCarTotalData = this.getBuilder().stream(
                    this.getCAR_TOTAL_DATA(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForCarTotalData
                    .mapValues( value -> this.getGson().fromJson( value, CarTotalData.class ) )
                    .mapValues( reqLocationExchange -> this.getMessageSendingService().sendMessage( reqLocationExchange ) );

            final KStream< String, String > kStreamForNotification = this.getBuilder().stream(
                    this.getNOTIFICATION(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForNotification
                    .mapValues( value -> this.getGson().fromJson( value, Notification.class ) )
                    .mapValues( notification -> this.getMessageSendingService().sendMessage( notification ) );

            final KStream< String, String > kStreamForTupleCarLocation = this.getBuilder().stream(
                    this.getTUPLE_OF_CAR_LOCATION_TOPIC(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForTupleCarLocation
                    .mapValues( value -> this.getGson().fromJson( value, Position.class ) )
                    .mapValues( position -> this.getMessageSendingService().sendMessage( position, "tupleOfCarLocationTopic" ) );

            final KStream< String, String > newTupleOfCarTopic = this.getBuilder().stream(
                    this.getNEW_TUPLE_OF_CAR_TOPIC(), Consumed.with( Serdes.String(), Serdes.String() ) );

            newTupleOfCarTopic
                    .mapValues( value -> this.getGson().fromJson( value, TupleOfCar.class ) )
                    .mapValues( tupleOfCar -> this.getMessageSendingService().sendMessage( tupleOfCar ) );

            final KStream< String, String > kStreamForPerson = this.getBuilder().stream(
                    this.getREDIS_PERSON(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForPerson
                    .mapValues( value -> this.getGson().fromJson( value, SearchingPersonRedis.class ) )
                    .mapValues( searchingPersonRedis -> this.getMessageSendingService().sendMessage( searchingPersonRedis ) );

            final KStream< String, String > kStream = this.getBuilder().stream( this.getWEBSOCKET_SERVICE_TOPIC_FOR_ONLINE(),
                    Consumed.with( Serdes.String(), Serdes.String() ) );

            kStream
                    .mapValues( value -> this.getGson().fromJson( value, Position.class ) )
                    .mapValues( position -> this.getMessageSendingService().sendMessage( position, "webSocketServiceTopicForOnline" ) );

            final KStream< String, String > kStreamForCar = this.getBuilder().stream(
                    this.getREDIS_CAR(), Consumed.with( Serdes.String(), Serdes.String() ) );

            kStreamForCar
                    .mapValues( value -> this.getGson().fromJson( value, SearchingCarRedis.class ) )
                    .mapValues( searchingCarRedis -> this.getMessageSendingService().sendMessage( searchingCarRedis ) );

            final KStream< String, String > newCarTopic = this.getBuilder().stream(
                    this.getNEW_CAR_TOPIC(), Consumed.with( Serdes.String(), Serdes.String() ) );

            newCarTopic
                    .mapValues( value -> this.getGson().fromJson( value, ReqCar.class ) )
                    .mapValues( reqCar -> this.getMessageSendingService().sendMessage( reqCar ) );

            final KStream< String, String > sos_topic = this.getBuilder().stream(
                    this.getSOS_TOPIC(), Consumed.with( Serdes.String(), Serdes.String() ) );

            sos_topic
                    .mapValues( value -> this.getGson().fromJson( value, SosNotification.class ) )
                    .mapValues( sosNotification -> this.getMessageSendingService().sendMessage( sosNotification ) );

            final KStream< String, String > sos_topic_for_android = this.getBuilder().stream(
                    this.getSOS_TOPIC_FOR_ANDROID_NOTIFICATION(), Consumed.with( Serdes.String(), Serdes.String() ) );

            sos_topic_for_android
                    .mapValues( value -> this.getGson().fromJson( value, SosNotificationForAndroid.class ) )
                    .mapValues( sosNotificationForAndroid -> this.getMessageSendingService().sendMessage( sosNotificationForAndroid ) );

            this.setKafkaStreams( new KafkaStreams( this.getBuilder().build(), this.getSetStreamProperties().get() ) );
            this.getKafkaStreams().start();
    }
}
