package com.theironyard.entities;

import org.hibernate.annotations.ColumnDefault;

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

    @ManyToOne
    private Parent creator;

    //Add name of description

    @Column
    private String description;

    @OneToOne
    private Point value;

    @Column
    @ColumnDefault("false")
    private boolean completed;

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

    public void setId(int id) {
        this.id = id;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
