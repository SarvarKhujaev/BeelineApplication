package com.beeline.beelineapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.beeline.beelineapplication.kafkaDataSet.KafkaDataControl;

@SpringBootApplication
public class BeelineApplication {
    public static ApplicationContext context;

    public static void main( final String[] args ) {
        context = SpringApplication.run( BeelineApplication.class, args );
        new KafkaDataControl();
    }
}
