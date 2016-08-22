package com.theironyard.command;

import java.io.File;

/**
 * Created by EddyJ on 8/13/16.
 */
public class ChildCommand {

    private String name;

    private String username;

    private String password;

    private String phone;

    private File childPicture;

    private String email;

    private int childPoint;

    public ChildCommand() {
    }

    public ChildCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public File getChildPicture() {
        return childPicture;
    }

    public void setChildPicture(File childPicture) {
        this.childPicture = childPicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getChildPoint() {

        return childPoint;
    }

    public void setChildPoint(int childPoint) {
        this.childPoint = childPoint;
    }
}
