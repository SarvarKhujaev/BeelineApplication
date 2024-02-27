package com.beeline.beelineapplication.entities;

public final class UserInitialInfo extends User {
    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLogin( final String login ) {
        this.login = login;
    }

    public void setPassword( final String password ) {
        this.password = password;
    }

    private String login;
    private String password;

    public UserInitialInfo () {}
}
