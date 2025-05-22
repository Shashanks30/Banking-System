package com.shashank.bankingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shashank.bankingsystem.model.Transaction;
import com.shashank.bankingsystem.model.User;
import com.shashank.bankingsystem.repository.TransactionRepository;
import com.shashank.bankingsystem.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public User registerUser(User user) {
        user.setBalance(0.0);
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isPresent() && opt.get().getPassword().equals(password)) {
            return opt.get();
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User deposit(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBalance(user.getBalance() + amount);

        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setType("deposit");
        txn.setAmount(amount);
        transactionRepository.save(txn);

        return userRepository.save(user);
    }

    public User withdraw(Long userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        if (user.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        user.setBalance(user.getBalance() - amount);

        Transaction txn = new Transaction();
        txn.setUser(user);
        txn.setType("withdraw");
        txn.setAmount(amount);
        transactionRepository.save(txn);

        return userRepository.save(user);
    }

    public void transfer(Long fromUserId, Long toUserId, Double amount) {
        User fromUser = userRepository.findById(fromUserId).orElseThrow();
        User toUser = userRepository.findById(toUserId).orElseThrow();

        if (fromUser.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + amount);

        userRepository.save(fromUser);
        userRepository.save(toUser);

        Transaction txnFrom = new Transaction();
        txnFrom.setUser(fromUser);
        txnFrom.setType("transfer_out");
        txnFrom.setAmount(amount);
        transactionRepository.save(txnFrom);

        Transaction txnTo = new Transaction();
        txnTo.setUser(toUser);
        txnTo.setType("transfer_in");
        txnTo.setAmount(amount);
        transactionRepository.save(txnTo);
    }

    public List<Transaction> getTransactions(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return transactionRepository.findByUser(user);
    }
}