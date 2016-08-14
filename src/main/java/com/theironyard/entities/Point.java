package com.theironyard.entities;

import javax.persistence.*;

/**
 * Created by Nigel on 8/13/16.
 */
@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private Integer value;

    @Column
    private String description;

    public Point() {
    }

    public Point(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
