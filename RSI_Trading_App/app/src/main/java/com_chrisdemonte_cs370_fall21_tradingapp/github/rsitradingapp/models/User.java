package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private int capital;
    private ArrayList<Stock> stocks;
    private int numStocks;

    //default User for testing purposes
    public User(){
        this.username = "Test1";
        this.password = "$Password1";
        this.email = "email@email.com";
        this.stocks = new ArrayList<Stock>();
        this.numStocks = 0;
        this.addStock(new Stock(1));
        this.addStock(new Stock(2));
        this.capital = 0;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.stocks = new ArrayList<Stock>();
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

    public ArrayList<Stock> getStocks() {
        return stocks;
    }
    public void addStock (Stock stock){
        this.stocks.add(stock);
        this.numStocks++;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        stocks = stocks;
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
}
