package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.File;

/**
 * Created by Nigel on 8/13/16.
 */
@Entity
@Table(name = "rewards")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reward {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String description;

    @Column
    private File url;

    @Column
    private int points;

    public Reward() {
    }

    public Reward(String description, File url, int points) {
        this.description = description;
        this.url = url;
        this.points = points;
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

    public File getUrl() {
        return url;
    }

    public void setUrl(File url) {
        this.url = url;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
