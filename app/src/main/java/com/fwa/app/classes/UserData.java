package com.fwa.app.classes;

import java.util.HashMap;

public class UserData {

    private String email;
    private String uid;
    private String familyUid;
    private String shoppingList;
    private String role; // role = member  - role = owner
    private String delete;
    private HashMap<String, String> hashFamily = new HashMap<>();
    private HashMap<String, String> hashMembers = new HashMap<>();
    public UserData(){}



    public UserData(String email, String uid, String delete, String role,
                    String list , String has_uid, String uidFamily, String has_key, String familyEmail){
        this.email = email;
        this.uid = uid;
        this.delete = delete;
        this.role = role;
        this.shoppingList = list;

        if(this.role.equals("member")){
            setHashMember(has_uid,uidFamily);
        }
        if(this.role.equals("owner")){
            setHashMember(has_key,familyEmail);
        }
    }

    public void setHashMember(String uid, String memberUid){
        this.hashFamily.put(uid,memberUid);
    }

    public HashMap<String, String> getHashFamily() {
        return hashFamily;
    }

    public void setHashFamily(HashMap<String, String> hashFamily) {
        this.hashFamily = hashFamily;
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

    public String getFamilyUid() {
        return familyUid;
    }

    public void setFamilyUid(String familyUid) {
        this.familyUid = familyUid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(String shoppingList) {
        this.shoppingList = shoppingList;
    }
}
