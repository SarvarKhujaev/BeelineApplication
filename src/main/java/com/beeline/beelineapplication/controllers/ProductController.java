package com.beeline.beelineapplication.controllers;

import com.beeline.beelineapplication.constants.postgres.PostgreSqlSchema;
import com.beeline.beelineapplication.constants.postgres.PostgreSqlTables;
import com.beeline.beelineapplication.database.PostgreDataControl;

import com.beeline.beelineapplication.entities.Product;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.ws.rs.core.Response;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping( value = "/beeline-serv/api/v1/product" )
public final class ProductController {
    @PostMapping ( value = "/" )
    public Mono< Response > createOrder (
            @RequestBody final Product product
    ) {
        return PostgreDataControl.getInstance().save( product );
    }

    @GetMapping ( value = "/" )
    public Mono< List< Product > > getAllProducts () {
        return Mono.just(
                PostgreDataControl
                        .getInstance()
                        .getAllEntities(
                                PostgreSqlSchema.ENTITIES,
                                PostgreSqlTables.PRODUCTS,
                                Product::generate
                        )
        );
    }
}
