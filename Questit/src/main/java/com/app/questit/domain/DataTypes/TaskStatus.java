package com.app.questit.domain.DataTypes;

public enum TaskStatus {
    AVAILABLE("AVAILABLE"),
    DONE("DONE");

    private final String stringRepresentation;


    TaskStatus(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    public String getStringValue(){
        return stringRepresentation;
    }
}
