package com.theironyard.entities;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by EddyJ on 8/13/16.
 */
@Entity
@Table(name = "chores")
public class Chore {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(updatable = true, insertable = true)
    private Timestamp startDate;

    @Column(updatable = true, insertable = true)
    private Timestamp endDate;

    //frequency
    /*I think this is going to be an enum of the days of the week.*/

    @ManyToOne
    private Parent creator;

    @ManyToOne
    private Child childAssigned;

    @Column
    @ColumnDefault("false")
    private boolean complete;

    @Column
    private String description;

    @Column
    private int value;

    @Column
    @ColumnDefault("false")
    private boolean pending;

    public Chore() {
    }

    public Chore(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Child getChildAssigned() {
        return childAssigned;
    }

    public void setChildAssigned(Child childAssigned) {
        this.childAssigned = childAssigned;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Parent getCreator() {
        return creator;
    }

    public void setCreator(Parent creator) {
        this.creator = creator;
    }

    public Date createNewStartDate(long date){
        Date newDate = new Date(date);

        return newDate;
    }

    public Date createNewEndDate(long date){
        Date newDate = new Date(date);

        return newDate;
    }


}
