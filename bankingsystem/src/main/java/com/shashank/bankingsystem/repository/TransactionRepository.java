package com.shashank.bankingsystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shashank.bankingsystem.model.Transaction;
import com.shashank.bankingsystem.model.User;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
}