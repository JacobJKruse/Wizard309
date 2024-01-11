package com.example.mysqlconnectiontest.CardInventory;

import com.example.mysqlconnectiontest.Card.Card;
import com.example.mysqlconnectiontest.Wizard.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardInventoryRepository extends JpaRepository<CardInventory, CardInventoryKey> {
    List<CardInventory> getAllByWizard(Wizard wizard);

    CardInventory findByWizardAndCard(Wizard wizard, Card card);

    @Transactional
    void deleteByWizardAndCard(Wizard wizard, Card card);

    @Transactional
    void deleteByCard(Card card);
}
