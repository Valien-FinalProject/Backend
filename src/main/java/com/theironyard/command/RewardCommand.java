package com.theironyard.command;

import com.theironyard.entities.Point;

import java.io.File;

/**
 * Created by EddyJ on 8/15/16.
 */
public class RewardCommand {

    private String description;

    private File url;

    private Point pointvalue;

    public RewardCommand() {
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

    public Point getPointvalue() {
        return pointvalue;
    }

    public void setPointvalue(Point pointvalue) {
        this.pointvalue = pointvalue;
    }
}
