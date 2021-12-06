package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models;

import com.google.firebase.database.IgnoreExtraProperties;

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

    }
    public User(int num){
        this.username = "Test1";
        this.password = "$Password1";
        this.email = "email@email.com";
        this.numStocks = 0;
        this.capital = 500;
        this.stocks = new ArrayList<Stock>();
        this.addStock(new Stock(1));
        this.addStock(new Stock(2));

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.capital = 0;
        this.numStocks = 0;
        this.stocks = new ArrayList<Stock>();
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

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }
    public void addStock(Stock stock){
        for (int i = 0; i < numStocks; i++){
            if (stock.getTicker().contentEquals(this.stocks.get(i).getTicker())){
                this.stocks.get(i).setNumOwned(this.stocks.get(i).getNumOwned() + stock.getNumOwned());
                return;
            }
        }
        this.stocks.add(stock);
        this.numStocks++;
    }
    public void removeStock(Stock stock){
        this.stocks.remove(stock);
        this.numStocks--;
    }
}
