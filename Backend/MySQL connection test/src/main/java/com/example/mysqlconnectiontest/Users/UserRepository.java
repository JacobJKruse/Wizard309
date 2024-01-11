package com.example.mysqlconnectiontest.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    User findByuserNameAndPassword(String userName, String password);

    @Transactional
    void deleteById(int id);


}

