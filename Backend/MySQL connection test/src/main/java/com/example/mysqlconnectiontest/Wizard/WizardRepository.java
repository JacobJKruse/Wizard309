package com.example.mysqlconnectiontest.Wizard;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WizardRepository extends JpaRepository<Wizard, Long> {
    Wizard findById(int id);

    Wizard findByUserId(int id);

}
