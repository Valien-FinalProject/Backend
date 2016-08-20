package com.theironyard.command;

import java.io.File;

/**
 * Created by EddyJ on 8/15/16.
 */
public class RewardCommand {

    private String description;

    private String url;

    private int points;

    public RewardCommand() {
    }

    public RewardCommand(String description, String url, int points) {
        this.description = description;
        this.url = url;
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
