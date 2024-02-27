package com.beeline.beelineapplication.controllers;

import com.beeline.beelineapplication.inspectors.ResponseController;
import com.beeline.beelineapplication.database.PostgreDataControl;
import com.beeline.beelineapplication.entities.UserInitialInfo;

import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;
import javax.ws.rs.core.Response;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping( value = "/beeline-serv/api/v1/auth" )
public final class AuthController extends ResponseController {
    /*
    отвечает за авторизацию
    */
    @PostMapping ( value = "/signin" )
    public Mono< Response > signin (
            @RequestBody final UserInitialInfo userInitialInfo
    ) {
        return PostgreDataControl.getInstance().checkAuth.apply( userInitialInfo );
    }

    /*
    отвечает за аутентификацию
    */
    @GetMapping( value = "/" )
    public Mono< Response > checkAuth () {
        return super.checkTokenDate(
                super.decode( ( (ServletRequestAttributes) Objects
                        .requireNonNull( RequestContextHolder.getRequestAttributes() ) )
                        .getRequest()
                        .getHeader( "Authorization" ) )
        ) ? Mono.just( super.getResponse( "Your Token is valid", Response.Status.OK ) )
                : Mono.just( super.getResponse( "Your Token is invalid, resign again", Response.Status.NOT_FOUND ) );
    }
}
