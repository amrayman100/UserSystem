package com.sumerge.program;

public class User {
    private int id;
    private String name;
    private String email;
    private String address;
    public User(){

    }
    public User(int id , String name , String email , String address){
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String toString(){
        return getName() +" "+ getEmail() +" "+ getAddress();
    }
}
