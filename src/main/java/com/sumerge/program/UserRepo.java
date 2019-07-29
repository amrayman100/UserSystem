package com.sumerge.program;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserRepo {

    static public ArrayList<User> UserList;

    public UserRepo(){
        UserList = new ArrayList<User>();
        UserList.add(new User(0,"Amr","amr@email.com","Dokki"));
        UserList.add(new User(1,"Ayman","ayman@email.com","Maadi"));
        UserList.add(new User(2,"Kamal","kamal@email.com","October"));
        UserList.add(new User(3,"Ibrahim","ibrahim@email.com","Mohendessin"));
    }

    public ArrayList<User>getUserList(){
        ArrayList<String> info = new ArrayList<String>();
        for (User user : UserList) {
             info.add(user.toString());

        }
        return UserList;

    }

    public User getUserbyId(int id){
        return UserList.get(id);

    }

    public User getUserbyName(String name){
        for (User user : UserList) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public User getUserbyemail(String email){
        for (User user : UserList) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public User getUserbyAddress(String address){
        for (User user : UserList) {
            if (user.getAddress().equals(address)) {
                return user;
        }
        }
        return null;
    }


    public void addUser(User u){
        UserList.add(u);
    }

    public ArrayList<User> deleteUser(int id){
        for (User user : UserList) {
            if (user.getId() == id) {
                UserList.remove(user);
                break;
            }
        }
        return UserList;

    }


    public User Update(User u , int id){
        int i = 0;
        for (User user : UserList) {

            if (user.getId() == u.getId()) {
                UserList.set(i,u);


                break;
            }
            i++;
        }

        return u;
    }







}
