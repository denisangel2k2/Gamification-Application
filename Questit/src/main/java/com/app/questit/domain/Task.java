package com.app.questit.domain;


import com.app.questit.domain.DataTypes.TaskStatus;

public class Task extends Entity<Long>{
    private String description;
    private TaskStatus status;
    private long asker_id;
    private Long responder_id;

    public Task(String description, long asker_id) {
        this.description = description;
        this.asker_id = asker_id;
        this.status=TaskStatus.AVAILABLE;
        this.responder_id=null;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setAsker_id(Long asker_id) {
        this.asker_id = asker_id;
    }

    public void setResponder_id(long responder_id) {
        this.responder_id = responder_id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public long getAsker_id() {
        return asker_id;
    }

    public Long getResponder_id() {
        return responder_id;
    }


}
