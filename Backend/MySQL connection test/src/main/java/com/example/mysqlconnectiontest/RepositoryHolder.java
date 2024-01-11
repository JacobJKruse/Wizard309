package com.example.mysqlconnectiontest;

import com.example.mysqlconnectiontest.Enemy.EnemyRepository;
import com.example.mysqlconnectiontest.Wizard.WizardRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryHolder {

    @Getter
    private static WizardRepository wizardRepository;
    @Getter
    private static EnemyRepository enemyRepository;

    @Autowired
    public RepositoryHolder(WizardRepository wizardRepository, EnemyRepository enemyRepository) {
        RepositoryHolder.wizardRepository = wizardRepository;
        RepositoryHolder.enemyRepository = enemyRepository;
    }

}

