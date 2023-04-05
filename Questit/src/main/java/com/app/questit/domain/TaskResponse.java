package com.app.questit.domain;

public class TaskResponse extends Entity<Long>{
    private String response;
    private long task_id;
    private long user_id;
    private boolean favorite;

    public TaskResponse(String response, long task_id, long user_id) {
        this.response = response;
        this.task_id = task_id;
        this.user_id = user_id;
        this.favorite=false;

    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    public String getResponse() {
        return response;
    }

    public long getTask_id() {
        return task_id;
    }

    public long getUser_id() {
        return user_id;
    }
}
