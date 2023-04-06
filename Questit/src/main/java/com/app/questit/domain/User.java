package com.app.questit.domain;

public class User extends Entity<Long>{
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String username;
    private int tokens;

    public User(String first_name, String last_name, String email, String password, String username) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.tokens=300;


    }
    public void setTokens(int tokens){
        this.tokens=tokens;
    }
    public int getTokens(){ return tokens; }


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

}
