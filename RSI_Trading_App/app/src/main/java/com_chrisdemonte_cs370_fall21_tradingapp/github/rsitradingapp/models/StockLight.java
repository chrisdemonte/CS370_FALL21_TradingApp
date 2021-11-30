package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models;

public class StockLight {

    private String owner;
    private String ticker;
    private int amount;

    public StockLight(String owner, String ticker, int amount) {
        this.owner = owner;
        this.ticker = ticker;
        this.amount = amount;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void changeAmount(int amount){
        this.amount += amount;
    }
}
