package com.fwa.app.authapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserTwo {
    public String name;
    public String email;
    public String age;
    //public String id;

    public UserTwo() {
    }

    //public UserTwo(String id, String name, String email, String age) {
    public UserTwo(String name, String email, String age) {
        //this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAge() {
        return age;
    }

    //public String getId(){ return id; }

}
