package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models;

public class Stock {
    private String ticker;
    private String company;
    private double currentPrice;
    private double[] historicPrices;
    private int numPrices;
    private double rsi;
    private double rsi2;
    private double averageGain;
    private double averageLoss;

    public Stock(String ticker, String company) {
        this.ticker = ticker;
        this.company = company;
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
}
