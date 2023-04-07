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

    /**
     * Set the number of tokens for a user
     * @param tokens
     */
    public void setTokens(int tokens){
        this.tokens=tokens;
    }

    /**
     * Get the number of tokens for a user
     * @return int
     */
    public int getTokens(){ return tokens; }


    /**
     * Get the first name of a user
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Get the last name of a user
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Get the email of a user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the password of a user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the username of a user
     */
    public String getUsername() {
        return username;
    }

}
