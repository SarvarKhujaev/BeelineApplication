package com.beeline.beelineapplication.controllers;

import com.beeline.beelineapplication.constants.postgres.PostgreSqlSchema;
import com.beeline.beelineapplication.constants.postgres.PostgreSqlTables;
import com.beeline.beelineapplication.database.PostgreDataControl;
import com.beeline.beelineapplication.entities.UserInitialInfo;
import com.beeline.beelineapplication.entities.User;

import com.beeline.beelineapplication.inspectors.ResponseController;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping( value = "/beeline-serv/api/v1/user" )
public final class UserController extends ResponseController {
    @PostMapping ( value = "/" )
    public Mono< Response > createUser (
            @RequestBody final UserInitialInfo userInitialInfo
    ) {
        return PostgreDataControl.getInstance().save( userInitialInfo );
    }

    @GetMapping ( value = "/" )
    public Mono< List< User > > getAllUsers () {
        return Mono.just(
                PostgreDataControl
                        .getInstance()
                        .getAllEntities(
                                PostgreSqlSchema.ENTITIES,
                                PostgreSqlTables.USERS,
                                User::generate
                        )
        );
    }

    @GetMapping ( value = "/filter" )
    public Mono< List< User > > getFilteredUsers (
            @RequestParam final Map< String, Object > params
    ) {
        return super.isCollectionNotEmpty( params )
                ? Mono.just(
                        PostgreDataControl
                            .getInstance()
                            .getAllEntities(
                                    PostgreSqlSchema.ENTITIES,
                                    PostgreSqlTables.USERS,
                                    params,
                                    User::generate
                            )
                )
                : Mono.just( super.emptyList() );
    }
}
