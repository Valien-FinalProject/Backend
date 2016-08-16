package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Nigel on 8/13/16.
 */
@Entity
@Table(name = "parents")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Parent extends User{

    @Column
    private String parentPhone;

    @OneToMany
    private Collection<Child> childCollection;

    //Add total point of all children

    @Column
    @ColumnDefault("false")
    boolean emailOptIn;

    @Column
    @ColumnDefault("false")
    boolean phoneOptIn;

    public Parent() {
    }

    public Parent(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        setTokenAndExpiration();
    }


    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public Collection<Child> getChildCollection() {
        return childCollection;
    }


    public void setChildCollection(Collection<Child> childCollection) {
        this.childCollection = childCollection;
    }
    public void addChild(Child child) {
        childCollection.add(child);
    }

    public boolean isEmailOptIn() {
        return emailOptIn;
    }

    public void setEmailOptIn(boolean emailOptIn) {
        this.emailOptIn = emailOptIn;
    }

    public boolean isPhoneOptIn() {
        return phoneOptIn;
    }

    public void setPhoneOptIn(boolean phoneOptIn) {
        this.phoneOptIn = phoneOptIn;
    }
}
