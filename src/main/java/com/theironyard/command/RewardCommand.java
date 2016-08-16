package com.theironyard.command;

import java.io.File;

/**
 * Created by EddyJ on 8/15/16.
 */
public class RewardCommand {

    private String description;

    private File url;

    private int pointvalue;

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

    public int getPointvalue() {
        return pointvalue;
    }

    public void setPointvalue(int pointvalue) {
        this.pointvalue = pointvalue;
    }
}
