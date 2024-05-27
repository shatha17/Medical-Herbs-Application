package com.example.project_2.Models;

public class User_Account extends AccountDB {

    public User_Account() {
    }

    public User_Account(String username, String email, String password, String accountType, String profile_image, String id) {
        super(username, email, password, accountType, profile_image, id);
    }
}
