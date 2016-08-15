package com.theironyard.command;

import com.theironyard.entities.Point;

/**
 * Created by chocodonis on 8/14/16.
 */
public class ChoreCommand {

    private String description;

    private Point value;

    public ChoreCommand(String description, Point value) {
        this.description = description;
        this.value = value;
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
