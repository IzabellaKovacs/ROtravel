package com.example.rotravel.HelperClasses;

import com.example.rotravel.Model.User;

public class ApplicationManager {
    private static ApplicationManager instance;


    private ApplicationManager(){}

    public static ApplicationManager getInstance(){
        if(instance == null){
            instance = new ApplicationManager();
        }

        return instance;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
