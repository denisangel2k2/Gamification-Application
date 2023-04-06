package com.app.questit.domain.DataTypes;

public enum QuestType {
    MATH("MATH"),
    PICK_PICTURE("PICK_PICTURE");

    private final String stringRepresentation;


    QuestType(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }
    public String getStringValue(){
        return stringRepresentation;
    }
}
