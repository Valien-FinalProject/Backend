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

    @OneToMany
    private Collection<Child> childCollection;

    //Add total point of all children

    @Column
    private boolean emailOptIn;

    @Column
    private boolean phoneOptIn;

    public Parent() {
        setTokenAndExpiration();
    }

    public Parent(String name,String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        setTokenAndExpiration();
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
