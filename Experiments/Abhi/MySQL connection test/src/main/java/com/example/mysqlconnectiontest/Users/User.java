package com.example.mysqlconnectiontest.Users;

import com.example.mysqlconnectiontest.Characters.Characters;
import com.example.mysqlconnectiontest.Wizard.Student;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String userName;
    private String password;
    private int age;


    @OneToMany
    private List<Characters> characters;

    @OneToMany
    @JoinColumn(name = "user_id")
    private  List<Student> students;

    public User(String email, String userName, String password, int age ) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.age = age;
        characters = new ArrayList<>();
        students = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Characters> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Characters> characters) {
        this.characters = characters;
    }

    public User() {
        characters = new ArrayList<>();
        students = new ArrayList<>();
    }

    public void setCharacter(Characters c) {
        this.characters.add(c);
    }


    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> s) {
        students = s;
    }
    public void addStudents(Student s) {
        this.students.add(s);
    }

    public void deleteStudent(Student s) {
        students.remove(s);
    }

}


