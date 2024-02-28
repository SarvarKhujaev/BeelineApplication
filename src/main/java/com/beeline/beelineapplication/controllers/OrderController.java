package com.beeline.beelineapplication.controllers;

import com.beeline.beelineapplication.constants.postgres.PostgreSqlSchema;
import com.beeline.beelineapplication.constants.postgres.PostgreSqlTables;
import com.beeline.beelineapplication.inspectors.ResponseController;
import com.beeline.beelineapplication.database.PostgreDataControl;
import com.beeline.beelineapplication.entities.Order;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping( value = "/beeline-serv/api/v1/order" )
public final class OrderController extends ResponseController {
    @PostMapping ( value = "/" )
    public Mono< Response > createOrder (
            @RequestBody final Order order
    ) {
        return PostgreDataControl.getInstance().save( order );
    }

    @GetMapping ( value = "/" )
    public Mono< List< Order > > getAllOrders () {
        return Mono.just(
                PostgreDataControl
                        .getInstance()
                        .getAllEntities(
                                PostgreSqlSchema.ENTITIES,
                                PostgreSqlTables.ORDERS,
                                Order::generate
                        )
        );
    }

    @GetMapping ( value = "/filter" )
    public Mono< List< Order > > getFilteredUsers (
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
                                        Order::generate
                                )
                )
                : Mono.just( super.emptyList() );
    }
}
