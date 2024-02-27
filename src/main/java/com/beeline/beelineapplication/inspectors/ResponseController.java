package com.beeline.beelineapplication.inspectors;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public class ResponseController extends Archive {
    protected ResponseController () {}

    protected Response getResponse (
            final String message,
            final Response.Status status
    ) {
        return Response.ok()
                .status( status )
                .entity( message )
                .build();
    }

    protected Response getResponse (
            final SQLException exception
    ) {
        return Response
                .status( Response.Status.INTERNAL_SERVER_ERROR )
                .entity( super.CONNECTION_ERRORS.getOrDefault( exception.getSQLState(), exception.getMessage() ) )
                .build();
    }
}
