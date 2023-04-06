package com.app.questit.domain;


import com.app.questit.domain.DataTypes.QuestType;
import com.app.questit.domain.DataTypes.TaskStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quest extends Entity<Long>{
    private TaskStatus status;
    private Long responder_id;
    private QuestType questType;
    private int tokens;
    private String description;

    public Quest(QuestType questType, int tokens) {
        this.questType = questType;
        this.status=TaskStatus.AVAILABLE;
        this.responder_id=null;
        this.tokens=tokens;
        initDescription();
    }

    private void initDescription(){
        List<String> names=new ArrayList<>();
        names.addAll(List.of("Luna", "Max", "Bella", "Charlie", "Lucy", "Oliver", "Daisy", "Rocky", "Sophie", "Milo"));
        //choose a random item from the list
        Random random = new Random();
        String name = names.get(random.nextInt(names.size()));

        switch (questType){

            case MATH:
                this.description=name+" needs help with math, help it by guessing the lost number!";
                break;
            case PICK_PICTURE:
                this.description=name+" is an artist, do you think you can guess its next masterpiece?";
                break;
        }

    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getTokens() {
        return tokens;
    }
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setResponder_id(long responder_id) {
        this.responder_id = responder_id;
    }


    public QuestType getQuestType() {
        return questType;
    }
    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }
    public TaskStatus getStatus() {
        return status;
    }


    public Long getResponder_id() {
        return responder_id;
    }


}
