package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.security.Principal;

public class Account {

   private BigDecimal balance;
   private int accountId;

    public int getAccountId() {
        return accountId;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
