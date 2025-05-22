package com.shashank.bankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.shashank.bankingsystem.model.Transaction;
import com.shashank.bankingsystem.model.User;
import com.shashank.bankingsystem.service.BankService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return bankService.registerUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> loginData) {
        return bankService.loginUser(loginData.get("email"), loginData.get("password"));
    }

    @PostMapping("/deposit/{userId}")
    public User deposit(@PathVariable Long userId, @RequestParam Double amount) {
        return bankService.deposit(userId, amount);
    }

    @PostMapping("/withdraw/{userId}")
    public User withdraw(@PathVariable Long userId, @RequestParam Double amount) {
        return bankService.withdraw(userId, amount);
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromUserId, @RequestParam Long toUserId, @RequestParam Double amount) {
        bankService.transfer(fromUserId, toUserId, amount);
        return "Transfer successful";
    }

    @GetMapping("/transactions/{userId}")
    public List<Transaction> transactions(@PathVariable Long userId) {
        return bankService.getTransactions(userId);
    }
}