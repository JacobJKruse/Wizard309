package com.example.mysqlconnectiontest.Wizard;

import java.util.List;

import com.example.mysqlconnectiontest.Users.User;
import com.example.mysqlconnectiontest.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/students")
    List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PostMapping(path = "/students")
    String createStudent(@RequestBody Student student) {
        if(student == null) {
            return failure;
        }
        studentRepository.save(student);
        return  success;

    }

    @PutMapping("/students/{id}")
    User updateStudent(@PathVariable int id, @RequestBody Student request) {

        User user = userRepository.findById(id);
        if(user == null) {
            return null;
        }
        user.addStudents(request);
        studentRepository.save(request);

        return userRepository.findById(id);

    }

    @DeleteMapping
    String deleteStudent(@PathVariable int id) {
        Student student = studentRepository.findById(id);
        if(student == null) {
            return failure;
        }
        student.getUser().deleteStudent(student);
        studentRepository.deleteById(Long.valueOf(id));

        return success;
    }

}
