package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.controllers;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models.Stock;

public class StockUtils  {

    //alphavantage API key
    private static String apiKey = "KYT2MHPX4980VKLJ";
    private static String priceDataURL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED";
    private static String overviewDataURL = "https://www.alphavantage.co/query?function=OVERVIEW";
    private static String response;

    public static Stock temp = null;

    public static String getBasicStockData(String ticker){
        String requestURL = overviewDataURL + "&symbol=" + ticker + "&outputsize=compact&apikey=" + apiKey;
        try {
            URL request = new URL(requestURL);
            URLConnection connection = request.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            StringBuilder textBuilder = new StringBuilder();
            Reader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName(StandardCharsets.UTF_8.name())));
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
            response = trimResponse(textBuilder.toString());
            //response = textBuilder.toString();
        }
        catch (MalformedURLException e){
            response = e.toString();
        }
        catch (IOException e){
            response = e.toString();
        }
        return response;
    }
    public static String trimResponse (String text){
        StringBuilder responseBuilder = new StringBuilder();
        Scanner scanner = new Scanner(text);
        while (scanner.hasNext()){
            String line = scanner.nextLine().trim();
            if (line.contains("{}")){
                responseBuilder.append("Invalid Ticker");
                temp = null;
                break;
            }
            if (line.contains("Symbol")){
                temp = new Stock();
                temp.setTicker(line.split(":")[1].replace(",","").replace("\"",""));
                temp.setNumOwned(0);
                responseBuilder.append(line.replace(",","").replace("\"",""));
                responseBuilder.append("\n");
            }
            else if (line.contains("Name")){
                temp.setCompany(line.split(":")[1].replace(",","").replace("\"",""));
                responseBuilder.append(line.replace(",","").replace("\"",""));
                responseBuilder.append("\n");
            }
            else if (line.contains("Description")){
                responseBuilder.append(line.replace("\"",""));
                responseBuilder.append("\n");
                break;
            }
        }
        scanner.close();
        String response = responseBuilder.toString();
        if (response.length() > 550){
            return responseBuilder.toString().substring(0,550) + "...";
        }
        return responseBuilder.toString();
    }

}