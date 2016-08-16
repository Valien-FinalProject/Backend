package com.theironyard.command;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;

/**
 * Created by Nigel on 8/13/16.
 */
public class ParentCommand {

    private String name;

    private String email;

    private String username;

    private String password;

    private boolean emailOptin;

    private boolean phoneOptIn;

    public ParentCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailOptin() {
        return emailOptin;
    }

    public void setEmailOptin(boolean emailOptin) {
        this.emailOptin = emailOptin;
    }

    public boolean isPhoneOptIn() {
        return phoneOptIn;
    }

    public void setPhoneOptIn(boolean phoneOptIn) {
        this.phoneOptIn = phoneOptIn;
    }
}
