package com.beeline.beelineapplication.inspectors;

import com.beeline.beelineapplication.entities.User;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class StringOperations extends CollectionsInspector {
    protected StringOperations () {}

    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder encoder = Base64.getUrlEncoder();

    protected StringBuilder newStringBuilder ( final String s ) {
        return new StringBuilder( s );
    }

    /*
    принимает параметр для Cassandra, который является типом TIMESTAMP,
    и добавляет в начало и конец апострафы
    */
    protected String joinWithAstrix ( final Object value ) {
        return "'" + value + "'";
    }

    private String generateToken () {
        final byte[] bytes = new byte[ 24 ];
        this.secureRandom.nextBytes( bytes );
        return this.encoder.encodeToString( bytes );
    }

    protected Date decode ( final String token ) {
        return new Date(
                new String( Base64
                        .getDecoder()
                        .decode( token ) )
                        .split( "@" )[ 2 ]
        );
    }

    protected String generateToken ( final User user ) {
        return Base64
                .getEncoder()
                .encodeToString(
                        String.join( "@",
                                        user.getId().toString(),
                                        user.getPhoneNumber(),
                                        user.getCreatedDate().toString(),
                                        this.generateToken() )
                                .getBytes( StandardCharsets.UTF_8 ) );
    }
}
