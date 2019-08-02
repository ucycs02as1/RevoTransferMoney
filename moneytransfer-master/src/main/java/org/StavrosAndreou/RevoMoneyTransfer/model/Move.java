package org.StavrosAndreou.RevoMoneyTransfer.model;

import java.math.BigDecimal;

import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

//move class which handles the move of funds from one account to another
public class Move {

    private static final AtomicInteger MoveCounter = new AtomicInteger();
    private BigDecimal MoveAmount;
    private final int id;
    private int MoveSourceAccID;
    private int MoveDestinationAccID;
    //move currency
    private Currency MoveCurr;
    //keeping the comments of each of the move
    private String MoveComment;
    //creating a class to hold the moving of funds status
    private StatusMove status;

    public Move(int sourceAccountId, int destinationAccountId, BigDecimal amount, Currency currency, String comment) {
    	this.id = MoveCounter.getAndIncrement();
    	this.MoveSourceAccID = sourceAccountId;
        this.MoveDestinationAccID = destinationAccountId;
        this.MoveAmount = amount;
        this.MoveCurr = currency;
        this.MoveComment = comment;
        this.status = StatusMove.DONE;
        
        
    }

    public Move() {
        this.id = MoveCounter.getAndIncrement();
        this.status = StatusMove.DONE;
    }

    public int getId() {
        return id;
    }

    public int getMoveSourceAccID() {
        return MoveSourceAccID;
    }

    public void setMoveSourceAccID(int sourceAccountId) {
        this.MoveSourceAccID = sourceAccountId;
    }

    public int getMoveDestinationAccID() {
        return MoveDestinationAccID;
    }

    public void setMoveDestinationAccID(int destinationAccountId) {
        this.MoveDestinationAccID = destinationAccountId;
    }

    public BigDecimal getMoveAmount() {
        return MoveAmount;
    }

    public void setMoveAmount(BigDecimal amount) {
        this.MoveAmount = amount;
    }

    public Currency getMoveCurr() {
        return MoveCurr;
    }

    public void setMoveCurr(Currency currency) {
        this.MoveCurr = currency;
    }

    public String getMoveComment() {
        return MoveComment;
    }

    public void setMoveComment(String comment) {
        this.MoveComment = comment;
    }

    public StatusMove getStatus() {
        return status;
    }

    public void setStatus(StatusMove status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object s) {
        if (this == s) return true;
        if (s == null || getClass() != s.getClass()) return false;

        Move move = (Move) s;

        return id == move.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Move{" +
                "id=" + id +
                ", MoveSourceAccID=" + MoveSourceAccID +
                ", MoveDestinationAccID=" + MoveDestinationAccID +
                ", MoveAmount=" + MoveAmount +
                ", MoveCurr=" + MoveCurr +
                ", MoveComment='" + MoveComment + '\'' +
                ", status=" + status +
                '}';
    }

    public enum StatusMove {
        DONE,//status that the move is done
        FAILED, //status that move has failed
        EXECUTED //status that move is executed
        
    }
}
