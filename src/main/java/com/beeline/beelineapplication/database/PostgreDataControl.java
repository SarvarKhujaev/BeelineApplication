package com.beeline.beelineapplication.database;

import java.sql.*;
import java.util.List;
import java.util.UUID;
import java.text.MessageFormat;

import java.util.function.Supplier;
import java.util.function.Function;

import javax.ws.rs.core.Response;
import reactor.core.publisher.Mono;

import com.beeline.beelineapplication.entities.Order;
import com.beeline.beelineapplication.entities.Product;
import com.beeline.beelineapplication.BeelineApplication;
import com.beeline.beelineapplication.constants.postgres.*;
import com.beeline.beelineapplication.entities.UserInitialInfo;
import com.beeline.beelineapplication.inspectors.ErrorController;

public final class PostgreDataControl extends ErrorController {
    private Connection connection;
    private static PostgreDataControl INSTANCE = new PostgreDataControl();

    private Connection getConnection() {
        return this.connection;
    }

    public static PostgreDataControl getInstance () {
        return INSTANCE != null ? INSTANCE : ( INSTANCE = new PostgreDataControl() );
    }

    /*
    создаем Singleton instance и подключаемся к БД
    */
    private PostgreDataControl() {
        try {
            // подключаемся к базе
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    BeelineApplication
                            .context
                            .getEnvironment()
                            .getProperty( "variables.POSTGRES_VARIABLES.USER" ),
                    BeelineApplication
                            .context
                            .getEnvironment()
                            .getProperty( "variables.POSTGRES_VARIABLES.PASSWORD" )
            );

            // выводим сообщение об успехе
            super.logging( "Database is created" );
        } catch ( final SQLException e ) {
            super.analyzeError( e );
        }
    }

    /*
    получает запрос на INSERT, UPDATE или DELETE
    и возвращает ответку с дефолтным сообщением или ошибкой
    */
    private synchronized Response completeQuery (
            final String query,
            final Supplier< String > defaultMessage
    ) {
        try ( final Statement statement = this.getConnection().createStatement() ) {
            statement.execute( query );
        } catch ( final SQLException exception ) {
            super.analyzeError( exception );
            return super.getResponse( exception );
        }

        return super.getResponse( defaultMessage.get(), Response.Status.ACCEPTED );
    }

    public Mono< Response > save ( final Order order ) {
            /*
            подсчитываем общую сумму заказа
            */
            final long totalPrice = order.getProductList()
                    .stream()
                    .map( Product::getPrice )
                    .count();

            final StringBuilder stringBuilder = super.newStringBuilder( "(" );

            super.analyze(
                    order.getProductList(),
                    product -> stringBuilder.append( product.getId() ).append( ", " )
            );

            stringBuilder.append( ")" );

            return Mono.just(
                    MessageFormat.format(
                            """
                            {0} {1} {2}

                            {3} {4}.{5}
                            (
                                total_order_sum,
                                total_count_of_products_in_order,
            
                                product_list,
                                userId
                            ) VALUES ( {6}, {7}, {8}, {9} );
                            
                            {10} {11}.{12}
                            SET {13} = {13} - 1, {14} = {14} + 1
                            WHERE id IN {15};

                            {16}
                            """,
                            PostgreCommands.BEGIN_TRANSACTION,
                            PostgreCommands.ISOLATION_LEVEL,
                            PostgreTransactionTypes.REPEATABLE_READ,

                            PostgreCommands.INSERT,
                            PostgreSqlSchema.ENTITIES,
                            PostgreSqlTables.ORDERS,

                            totalPrice,
                            order.getProductList().size(),

                            order.getProductList(),
                            super.joinWithAstrix( order.getUserId().toString() ),

                            PostgreCommands.UPDATE,
                            PostgreSqlSchema.ENTITIES,
                            PostgreSqlTables.PRODUCTS,

                            "totalCount",
                            "productWasSoldCount",

                            stringBuilder.toString(),

                            PostgreCommands.COMMIT_TRANSACTION
                    )
            ).map( query -> this.completeQuery( query, () -> "Order from: " + order.getUserId() + " was saved" ) );
    }

    public Mono< Response > save( final Product product ) {
        return Mono.just(
                MessageFormat.format(
                            """
                            {0} {1}.{2}
                            (
                                category,
                                description,
                                productName,
                                price
                            ) VALUES ( {3}, {4}, {5}, {6} );
                            """,
                            PostgreCommands.INSERT,

                            PostgreSqlSchema.ENTITIES,
                            PostgreSqlTables.PRODUCTS,

                            super.joinWithAstrix( product.getCategory() ),
                            super.joinWithAstrix( product.getDescription() ),
                            super.joinWithAstrix( product.getProductName() ),

                            product.getPrice()
                )
        ).map( query -> this.completeQuery( query, () -> product.getProductName() + " was saved" ) );
    }

    public Mono< Response > save ( final UserInitialInfo user ) {
        return Mono.just(
                MessageFormat.format(
                        """
                        {0} {1} {2}
                        {3} {4}.{5}
                        (
                            id,
                            name,
                            email,
                            surname,
                            phoneNumber
                        ) VALUES ( {6}, {7}, {8}, {9}, {10} );

                        {11} {12}.{13}
                        (
                            user_id,
                            login,
                            password
                        ) VALUES ( {6}, {14}, {15} );
        
                        {16}
                        """,
                        PostgreCommands.BEGIN_TRANSACTION,
                        PostgreCommands.ISOLATION_LEVEL,
                        PostgreTransactionTypes.REPEATABLE_READ,

                        PostgreCommands.INSERT,
                        PostgreSqlSchema.ENTITIES,
                        PostgreSqlTables.USERS,

                        super.joinWithAstrix( UUID.randomUUID().toString() ),
                        super.joinWithAstrix( user.getName() ),
                        super.joinWithAstrix( user.getEmail() ),
                        super.joinWithAstrix( user.getSurname() ),
                        super.joinWithAstrix( user.getPhoneNumber() ),

                        PostgreCommands.INSERT,
                        PostgreSqlSchema.AUTH_SCHEMA,
                        PostgreSqlTables.USER_AUTHORIZATION,

                        super.joinWithAstrix( user.getLogin() ),
                        super.joinWithAstrix( user.getPassword() ),

                        PostgreCommands.COMMIT_TRANSACTION
                )
        ).map( query -> this.completeQuery( query, () -> user.getName() + " was saved" ) );
    }

    public final Function< UserInitialInfo, Mono< Response > > checkAuth = userInitialInfo -> {
        try ( final Statement statement = this.connection.createStatement() ) {
            final ResultSet resultSet = statement.executeQuery(
                    MessageFormat.format(
                            """
                             {0} {1}.{2}
                             {3} {4}.{5} ON {1}.{2}.{6} = {4}.{5}.{7}
                             WHERE login = {8} AND password = {9} -- делаем выборку по логину пользователя
                             """,
                            PostgreCommands.SELECT,

                            PostgreSqlSchema.AUTH_SCHEMA,
                            PostgreSqlTables.USER_AUTHORIZATION,

                            PostgreJoins.INNER_JOIN,

                            PostgreSqlSchema.ENTITIES,
                            PostgreSqlTables.USERS,

                            "user_id",
                            "id",

                            super.joinWithAstrix( userInitialInfo.getLogin() ),
                            super.joinWithAstrix( userInitialInfo.getPassword() )
                    )
            );

            if ( resultSet.next() ) {
                userInitialInfo.setId( resultSet.getObject( "id", UUID.class ) );
                userInitialInfo.setCreatedDate( super.newDate() );

                resultSet.close();

                return Mono.just( super.getResponse( super.generateToken( userInitialInfo ), Response.Status.OK ) );
            }

            resultSet.close();
            return Mono.just( super.getResponse( "Wrong Login Or Password", Response.Status.NO_CONTENT ) );
        } catch ( final SQLException exception ) {
            super.analyzeError( exception );
            return Mono.just( super.getResponse( exception ) );
        }
    };

    public <T> List<T> getAllEntities (
            final PostgreSqlSchema schema,
            final PostgreSqlTables table,
            final Function< ResultSet, T > function
    ) {
        final List< T > list = super.newList();

        try ( final Statement statement = this.getConnection().createStatement() ) {
            final ResultSet resultSet = statement.executeQuery(
                    MessageFormat.format(
                            """
                             {0} {1}.{2};
                             """,
                            PostgreCommands.SELECT,
                            schema,
                            table
                    )
            );

            while ( resultSet.next() ) {
                list.add( function.apply( resultSet ) );
            }

            return list;
        } catch ( final SQLException exception ) {
            super.logging( exception );
            return list;
        }
    }

    // закрывает подключение к базе
    public void close () {
        try {
            INSTANCE = null;
            this.getConnection().close();
            super.logging( "Database is closed" );
        } catch ( final Exception e ) {
            super.logging( e );
        }
    }
}
