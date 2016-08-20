package com.theironyard.command;


import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by chocodonis on 8/14/16.
 */
public class ChoreCommand {

    private Timestamp startDate;

    private Timestamp endDate;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}