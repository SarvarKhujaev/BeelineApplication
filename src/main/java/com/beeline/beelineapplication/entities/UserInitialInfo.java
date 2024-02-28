package com.beeline.beelineapplication.entities;

public final class UserInitialInfo extends User {
    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    private String login;
    private String password;
}
