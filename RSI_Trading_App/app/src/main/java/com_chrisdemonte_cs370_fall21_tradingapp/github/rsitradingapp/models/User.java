package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
@IgnoreExtraProperties
public class User {
    private String username;
    private String password;
    private String email;
    private int capital;
    //private StockLight[] stocks;
    private int numStocks;

    //default User for testing purposes
    public User(){

    }
    public User(int num){
        this.username = "Test1";
        this.password = "$Password1";
        this.email = "email@email.com";
        this.numStocks = 2;
        this.capital = 500;
     //   this.stocks = new StockLight[2];
      //  this.stocks[0] = new StockLight(this.username, "APPL", 1);
     //   this.stocks[1] = new StockLight(this.username, "GOOGL", 1);
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.capital = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumStocks() {
        return numStocks;
    }

    public void setNumStocks(int numStocks) {
        this.numStocks = numStocks;
    }

    public int getCapital() {
        return capital;
    }

    public void setCapital(int capital) {
        this.capital = capital;
    }
/*
    public StockLight[] getStocks() {
        return stocks;
    }

    public void setStocks(StockLight[] stocks) {
        this.stocks = stocks;
    }
    public void addStock(String ticker, int amount){
        for (int i = 0; i < this.numStocks; i++){
            if (ticker.contentEquals(this.stocks[i].getTicker())){
                this.stocks[i].changeAmount(amount);
                return;
            }
        }
        StockLight[] newStocks = new StockLight[numStocks + 1];
        for (int i = 0; i < this.numStocks; i++){
            newStocks[i] = this.stocks[i];
        }
        newStocks[this.numStocks] = new StockLight (this.username, ticker, amount);
        this.numStocks++;

    }*/
}
