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
    private String childPhone;

    @Column
    @JsonIgnore
    private String location;

    @Column
    @JsonIgnore
    private File childPicture;

    @ManyToOne
    @JsonIgnore
    private Parent parent;

    @OneToOne
    private int childPoint;

//    @Column
//    @JsonIgnore
//    Collection<Log> pointLog;

    @Column(nullable = false, unique = true)
    @ColumnDefault("'abcabcabcabcabc'")
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @ColumnDefault("'1992-01-01'")
    private LocalDateTime expiration;

    public Child() {
        setTokenAndExpiration();
    }

    public Child(String name, String username,String password, Parent parent) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.parent = parent;
        setTokenAndExpiration();
    }

    public String getChildPhone() {
        return childPhone;
    }

    public void setChildPhone(String childPhone) {
        this.childPhone = childPhone;
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
