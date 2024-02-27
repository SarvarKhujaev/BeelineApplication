package com.beeline.beelineapplication.controllers;

import com.beeline.beelineapplication.constants.postgres.PostgreSqlSchema;
import com.beeline.beelineapplication.constants.postgres.PostgreSqlTables;
import com.beeline.beelineapplication.database.PostgreDataControl;
import com.beeline.beelineapplication.entities.Order;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping( value = "/beeline-serv/api/v1/order" )
public final class OrderController {
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
}
