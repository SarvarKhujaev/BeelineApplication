package com.beeline.beelineapplication.inspectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogInspector extends DataValidateInspector {
    protected LogInspector () {}

    private final Logger LOGGER = LogManager.getLogger( "LOGGER_WITH_JSON_LAYOUT" );

    private Logger getLOGGER () {
        return this.LOGGER;
    }

    protected void logging ( final String message ) {
        this.getLOGGER().info( message );
    }

    protected void logging ( final Throwable error ) {
        this.getLOGGER().error( "Error: " + error );
    }
}
