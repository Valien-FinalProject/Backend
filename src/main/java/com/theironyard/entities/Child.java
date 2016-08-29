package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.theironyard.utilities.LocalDateTimeConverter;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @ManyToMany
    @JsonIgnore
    private Collection<Reward> cashedInRewards;

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

    public int getChildPoint() {
        return childPoint;
    }

    public void setChildPoint(int childPoint) {
        this.childPoint = childPoint;
    }

    public Collection<Reward> getCashedInRewards() {
        return cashedInRewards;
    }

    public void setCashedInRewards(Collection<Reward> cashedInRewards) {
        this.cashedInRewards = cashedInRewards;
    }

//    public Collect<Log> getPointLog() {
//        return pointLog;
//    }
//
//    public void setPointLog(Collect<Log> pointLog) {
//        this.pointLog = pointLog;
//    }

}
