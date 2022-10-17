package com.fwa.app.classes;

public class User {

    public String name, age, email;

    public User(){}

    public User(String name, String age, String email){
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}
