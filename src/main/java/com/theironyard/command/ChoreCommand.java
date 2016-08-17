package com.theironyard.command;

import java.time.LocalDateTime;

/**
 * Created by chocodonis on 8/14/16.
 */
public class ChoreCommand {

    LocalDateTime startDate;

    LocalDateTime endDate;

    private String name;

    private String description;

    private int value;

    public ChoreCommand() {
    }

    public ChoreCommand(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
