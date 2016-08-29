package com.theironyard.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.File;
import java.util.Collection;

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

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(length = 5000)
    private String imageUrl;

    @Column(length = 5000)
    private String url;

    @Column
    private int points;

    @ManyToMany
    @JsonIgnore
    private Collection<Child> rewardsCashed;

    public Reward() {
    }

    public Reward(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Collection<Child> getRewardsCashed() {
        return rewardsCashed;
    }

    public void setRewardsCashed(Collection<Child> rewardsCashed) {
        this.rewardsCashed = rewardsCashed;
    }
}
