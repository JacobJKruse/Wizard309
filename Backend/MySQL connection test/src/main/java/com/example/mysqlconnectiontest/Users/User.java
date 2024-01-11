package com.example.mysqlconnectiontest.Users;


import com.example.mysqlconnectiontest.Wizard.Wizard;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    @Column(name="user_name", unique = true)
    private String userName;
    @Column(name="password")
    private String password;
    private int age;
    private UserLevel  userLevel;

    @OneToMany
    @JoinColumn(name = "user_id")
    private  List<Wizard> wizards;

    public User(String email, String userName, String password, int age, UserLevel userLevel ) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.userLevel = userLevel;
        wizards = new ArrayList<>();
    }


    public void setId(int id) {
        this.id = id;
    }

    public User() {

        wizards = new ArrayList<>();
    }


    public void setWizards(List<Wizard> s) {
        wizards = s;
    }
    public void addWizard(Wizard s) {
        this.wizards.add(s);
    }

    public void deleteWizard(Wizard s) {
        wizards.remove(s);
    }

}


