package com.beeline.beelineapplication.inspectors;

import com.beeline.beelineapplication.database.PostgreDataControl;
import java.sql.SQLException;

/*
отвечает за работу с ошибками
*/
public class ErrorController extends ResponseController {
    protected ErrorController () {}

    protected <T> void analyzeError (
            final SQLException exception
    ) {
        super.logging( exception );

        /*
        если ошибка связана с соединением к БД
         */
        if ( super.CONNECTION_ERRORS.containsKey( exception.getSQLState() ) ) {
            /*
            то буем стараться переподключиться
             */
            PostgreDataControl.getInstance().close();
        }
    }
}
