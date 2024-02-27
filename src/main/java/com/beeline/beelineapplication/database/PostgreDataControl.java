package com.beeline.beelineapplication.database;

import com.example.fido.components.LogInspector;
import com.example.fido.constants.PostgreCommands;
import reactor.core.publisher.Mono;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class PostgreDataControl extends LogInspector {
    private Connection connection;
    private static PostgreDataControl INSTANCE = new PostgreDataControl();

    private Connection getConnection() {
        return this.connection;
    }

    public static PostgreDataControl getInstance () {
        return INSTANCE != null ? INSTANCE : ( INSTANCE = new PostgreDataControl() );
    }

    private PostgreDataControl() {
        try {
            final String user = "postgres";
            final String password = "killerbee";

            // подключаемся к базе
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    user,
                    password
            );

            // выводим сообщение об успехе
            super.logging( "Database is created" );
        } catch ( final SQLException e ) {
            super.logging( e );
            this.close();
        }
    }

    // получает название банкноты и возвращает его историю
    public final Function< String, Mono< List< ? > > > getCurrentBanknote = banknoteName -> {
            try {
                final PreparedStatement preparedStatement = this.getConnection()
                        .prepareStatement( PostgreCommands.SELECT_ROWS_FOR_BANKNOTE_NAME );

                preparedStatement.setString( 1, banknoteName );

                final ResultSet resultSet = preparedStatement.executeQuery();

                final List< ? > banknoteList = new ArrayList<>();
                while ( resultSet.next() ) {
                }

                // проверяем что у нас есть записи в БД
                return !banknoteList.isEmpty()
                        ? Mono.just( banknoteList )
                        : Mono.just( Collections.emptyList() );
            } catch ( final SQLException e ) {
                this.close();
                super.logging( e );
                return Mono.error( e );
            }
    };

    // возвращет все записи по всем купюрам с применением пагинации
    public final BiFunction< Integer, Integer, Mono< ? > > getAllBanknotes = ( page, size ) -> {
            try {
                final ResultSet resultSetForCount = this.getConnection()
                        .prepareStatement( PostgreCommands.SELECT_COUNT )
                        .executeQuery();

                final long count = resultSetForCount.next()
                        ? resultSetForCount.getLong( "count" )
                        : 0L;

                resultSetForCount.close();

                // проверяем что у нас есть записи в БД
                if ( count > 0 ) {
                    final PreparedStatement preparedStatement = this.getConnection()
                            .prepareStatement( PostgreCommands.SELECT_ALL_BANKNOTE_WITH_PAGINATION );

                    preparedStatement.setLong( 1, size );
                    preparedStatement.setLong( 2, (long) size * page );

                    final ResultSet resultSet = preparedStatement.executeQuery();

                    resultSet.close();
                    return Mono.empty();
                } return Mono.empty();
            } catch ( final Exception e ) {
                this.close();
                super.logging( e );
                return Mono.error( e );
            }
    };

    // закрывает подключение к базе
    private void close () {
        try {
            this.getConnection().close();
            super.logging( "Database is closed" );
        } catch ( final Exception e ) {
            super.logging( e );
        }
    }
}
