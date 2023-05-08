package com.fwa.app.classes;

public class FamilyMember{
    private String email;
    private String uid;
    private String delete;

    public FamilyMember(){}

    public FamilyMember(String email,String uid,String delete){
        this.email = email;
        this.uid = uid;
        this.delete = delete;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}