package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models;

public class Stock {
    private String ticker;
    private String company;
    private double currentPrice;
    private int numOwned;
    private double[] historicPrices;
    private int numPrices;
    private double rsi;
    private double rsi2;
    private double averageGain;
    private double averageLoss;

    public Stock(int num){
        if (num == 1) {
            this.ticker = "APPL";
            this.company = "Apple Inc.";
            this.currentPrice = 148.69;
            double[] arr = {147.92, 150.81, 150.44, 151.28, 150.96, 151.49, 150.02, 148.96, 149.80, 152.57, 148.85, 149.32, 148.64, 148.69};
            this.historicPrices = arr;
            this.numPrices = 14;
            this.numOwned = 1;
        }
        else {
            this.ticker = "NASDAQ: GOOGL";
            this.company = "Alphabet Inc. Class A";
            this.currentPrice = 2935.11;
            double[] arr = {2984.97, 2932.52, 2934.96, 2992.91, 2987.76, 2981.52, 2981.24, 3014.18, 2999.05, 2941.57, 2935.14, 2934.35, 2856.12, 2935.11};
            this.historicPrices = arr;
            this.numPrices = 14;
            this.numOwned = 1;
        }
        this.calculateGainLoss();
        this.calculateRSI1();
        this.calculateRSI2();
    }

    public Stock(String ticker, String company, int numOwned) {
        this.ticker = ticker;
        this.company = company;
        this.numOwned = numOwned;
    }

    public void apiCall(){

    }
    public void calculateRSI1(){
        calculateGainLoss();
        this.rsi = 100.0 - (100.0/ (1.0 + ((this.averageGain / (double)numPrices)/(this.averageLoss/(double)numPrices))));
    }
    public void calculateRSI2(){
        double change = this.currentPrice - historicPrices[this.numPrices - 1];
        double gain = 0;
        double loss = 0;
        if (change > 0){
            gain += change;
        }
        else {
            loss -= change;
        }
        this.rsi2 = 100.0 - (100.0/
                (1.0 + ((((this.averageGain * (double)(this.numPrices - 2)) + gain) / (double)numPrices)/
                        (((this.averageLoss * (double)(this.numPrices - 2)) + loss) / (double)numPrices))));
    }
    public void calculateGainLoss(){
        double change;
        this.averageGain = 0;
        this.averageLoss = 0;
        for(int i = 0; i < numPrices - 1; i++){
            change = (this.historicPrices[i+1] - historicPrices[i])/this.historicPrices[i];
            if (change < 0){
                this.averageLoss -= change;
            }
            else {
                this.averageGain += change;
            }
        }
        this.averageLoss /= (double)(this.numPrices - 1);
        this.averageGain /= (double)(this.numPrices - 1);
    }


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double[] getHistoricPrices() {
        return historicPrices;
    }

    public void setHistoricPrices(double[] historicPrices) {
        this.historicPrices = historicPrices;
    }

    public int getNumPrices() {
        return numPrices;
    }

    public void setNumPrices(int numPrices) {
        this.numPrices = numPrices;
    }

    public double getRsi() {
        return Math.round(rsi * 100.0)/100.0;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    public double getRsi2() {
        return rsi2;
    }

    public void setRsi2(double rsi2) {
        this.rsi2 = rsi2;
    }

    public double getAverageGain() {
        return averageGain;
    }

    public void setAverageGain(double averageGain) {
        this.averageGain = averageGain;
    }

    public double getAverageLoss() {
        return averageLoss;
    }

    public void setAverageLoss(double averageLoss) {
        this.averageLoss = averageLoss;
    }

    public int getNumOwned() {
        return numOwned;
    }

    public void setNumOwned(int numOwned) {
        this.numOwned = numOwned;
    }
}
