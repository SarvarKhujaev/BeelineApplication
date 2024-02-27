package com.beeline.beelineapplication.entities;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Date;
import java.util.UUID;

public class User {
    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public UUID getId() {
        return this.id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setId( final UUID id ) {
        this.id = id;
    }

    public void setCreatedDate( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    private String name;
    private String email;
    private String surname;
    private String phoneNumber;

    private UUID id;

    // дата создания аккаунта
    private Date createdDate;

    public static User generate (
            final ResultSet resultSet
    ) {
        return new User( resultSet );
    }

    protected User (
            final ResultSet resultSet
    ) {
        try {
            this.setId( resultSet.getObject( "id", UUID.class ) );
            this.setCreatedDate( resultSet.getDate( "created_date" ) );

            this.setName( resultSet.getString( "name" ) );
            this.setEmail( resultSet.getString( "email" ) );
            this.setSurname( resultSet.getString( "surname" ) );
            this.setPhoneNumber( resultSet.getString( "phoneNumber" ) );
        } catch ( final SQLException e ) {}
    }

    public User () {}
}
