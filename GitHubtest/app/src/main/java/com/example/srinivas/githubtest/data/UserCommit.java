package com.example.srinivas.githubtest.data;

/**
 * Created by Srinivas on 06-06-2016.
 */
public class UserCommit {

    private String name;
    private String email;
    private String message;
    private String date;

    public UserCommit() {

    }

    public UserCommit(String name,String email,String message,String date) {
        this.name=name;
        this.email=email;
        this.message=message;
        this.date=date;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public void setMessage(String message) {
        this.message=message;
    }

    public void setDate(String date) {
        this.date=date;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
