package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.theironyard.utilities.LocalDateTimeConverter;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by Nigel on 8/13/16.
 */
@Entity
@Table(name = "children")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Child extends User{

    @Column
    @JsonIgnore
    private String location;

    @Column
    @JsonIgnore
    private File childPicture;

    @ManyToOne
    @JsonIgnore
    private Parent parent;

    @Column
    private int childPoint;

//    @Column
//    @JsonIgnore
//    Collection<Log> pointLog;

    public Child() {
        setTokenAndExpiration();
    }

    public Child(String name, String username,String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        setTokenAndExpiration();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public File getChildPicture() {
        return childPicture;
    }

    public void setChildPicture(File childPicture) {
        this.childPicture = childPicture;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public boolean isTokenValid(){
        return expiration.isAfter(LocalDateTime.now());
    }

    public int getChildPoint() {
        return childPoint;
    }

    public void setChildPoint(int childPoint) {
        this.childPoint = childPoint;
    }

    //    public Collect<Log> getPointLog() {
//        return pointLog;
//    }
//
//    public void setPointLog(Collect<Log> pointLog) {
//        this.pointLog = pointLog;
//    }

}
