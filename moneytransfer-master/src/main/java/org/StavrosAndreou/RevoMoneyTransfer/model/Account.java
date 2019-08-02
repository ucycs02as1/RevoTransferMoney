package org.StavrosAndreou.RevoMoneyTransfer.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;


//Class that shows the Account created, edited, added of the Customer
public class Account {

    private static final AtomicInteger AccCounter = new AtomicInteger();
    private String OwnerName;
    private final int id;
    private BigDecimal balance;
    private Currency AccCurr;

    public Account(String name, BigDecimal balance, Currency currency) {
        this.OwnerName = name;
        this.id = AccCounter.getAndIncrement();
        this.balance = balance;
        this.AccCurr = currency;
    }

    public Account() {
        this.id = AccCounter.getAndIncrement();
    }
    public int getAccID() {
        return id;
    }

    
    public void setOwnerName(String name) {
        this.OwnerName = name;
    }
    public String getOwnerName() {
        return OwnerName;
    }

    
     
    public void setAccCurr(Currency currency) {
        this.AccCurr = currency;
    }
    public Currency getAccCurr() {
        return AccCurr;
    }

    
    
    public void setAccBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getAccBalance() {
        return balance;
    }

    
    public void AccDeposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }
    public void AccWithdrawal(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    
    
    
    @Override
    public boolean equals(Object s) {
        if (this == s) return true;
        if (s == null || getClass() != s.getClass()) return false;

        Account account = (Account) s;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", ownerName='" + OwnerName + '\'' +
                ", balance=" + balance +
                ", accCurr=" + AccCurr +
                '}';
    }

    
}
