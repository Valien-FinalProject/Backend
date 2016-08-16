package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by EddyJ on 8/13/16.
 */
@Entity
@Table(name = "chores")
public class Chore {

    @Id
    @GeneratedValue
    private int id;

    //Do we need to a creator? We definitely need a child this assigned to, right?
    @ManyToOne
    private Parent creator;

    //name
    //start date
    //end date
    //frequency
    //assign
    //complete boolean
    //pending boolean

    @Column
    private String description;

    @OneToOne
    private Point value;

    public Chore() {
    }

    public Chore(Parent creator, String description, Point value) {
        this.creator = creator;
        this.description = description;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Parent getCreator() {
        return creator;
    }

    public void setCreator(Parent creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point getValue() {
        return value;
    }

    public void setValue(Point value) {
        this.value = value;
    }
}
