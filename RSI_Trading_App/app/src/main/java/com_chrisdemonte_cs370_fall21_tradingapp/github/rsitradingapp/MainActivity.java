package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.controllers.UserUtils;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.HomeFragment;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.LoginFragment;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.NewAccountActivity;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models.Stock;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models.User;

public class MainActivity extends AppCompatActivity {

    public static User USER;
    public static int displayedStock = 0;
    public LoginFragment login = new LoginFragment();
    public HomeFragment home = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        giveLoginButtonsActions();
        loadLoginFragment();

        USER = new User();
        giveHomeButtonsActions();

    }

     /** Method for loading the NEW ACCOUNT ACTIVITY
     * To Do: left commented code for message passing
     */
    private void loadNewAccountActivity(){
        Intent intent = new Intent(this, NewAccountActivity.class);
        //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void loadHomeFragment(){
        findViewById(R.id.loginLayout).setVisibility(View.GONE);
        findViewById(R.id.homeLayout).setVisibility(View.VISIBLE);
        updateStockDisplay();

    }
    public void updateStockDisplay(){
        if (USER != null){
            if (displayedStock < USER.getNumStocks()){
                Stock stock = USER.getStocks().get(displayedStock);
                final TextView ticker = findViewById(R.id.tickerText);
                final TextView company = findViewById(R.id.companyText);
                final TextView RSI = findViewById(R.id.rsiText);
                final TextView currentPrice = findViewById(R.id.priceText);
                final TextView suggestion = findViewById(R.id.buySellText);

                ticker.setText(stock.getTicker());
                company.setText(stock.getCompany());
                RSI.setText("RSI: " + String.valueOf(stock.getRsi()));
                currentPrice.setText("Current Price: " + String.valueOf(stock.getCurrentPrice()));
                String suggest = "HOLD";
                if (stock.getRsi() <= 40){
                    suggest = "BUY";
                }
                else if (stock.getRsi() >= 70){
                    suggest = "SELL";
                }
                suggestion.setText(suggest);
                updateGraph(stock);
            }
        }
    }
    public void updateGraph(Stock stock){
        GraphView graph = findViewById(R.id.graphView1);
        graph.removeAllSeries();

        LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>();
        for (int i = 0; i < stock.getNumPrices(); i++){
            dataSeries.appendData(new DataPoint(i, stock.getHistoricPrices()[i]), true, stock.getNumPrices() + 1);
        }
        graph.addSeries(dataSeries);

    }

    public void loadLoginFragment(){
        findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.homeLayout).setVisibility(View.GONE);

    }
    public void giveLoginButtonsActions(){

        final EditText usernameEntry = findViewById(R.id.usernameEntry);
        final EditText passwordEntry = findViewById(R.id.passwordEntry);
        final TextView errorTextView = findViewById(R.id.errorMessageTextView);
        Button loginButton = findViewById(R.id.loginButton);
        Button createNewButton = findViewById(R.id.newAccountButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = usernameEntry.getText().toString();
                int result = UserUtils.validateUsername(username);
                if (result != 1){
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(UserUtils.getUsernameErrorMsg(result));
                    return;
                }
                String password = passwordEntry.getText().toString();
                result = UserUtils.validatePassword(password);
                if (result != 1){
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(UserUtils.getPasswordErrorMsg(result));
                    return;
                }
                errorTextView.setVisibility(View.INVISIBLE);
                errorTextView.setText("Unknown Error!");
                loadHomeFragment();
            }
        });
        createNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadNewAccountActivity();
            }
        });
    }
    public void giveHomeButtonsActions(){

        Button logoutButton = findViewById(R.id.logoutButton);
        Button nextButton = findViewById(R.id.nextStockButton);
        Button previousButton = findViewById(R.id.previousStockButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadLoginFragment();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayedStock++;
                displayedStock%= USER.getNumStocks();
                updateStockDisplay();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayedStock--;
                if (displayedStock < 0){
                    displayedStock = USER.getNumStocks() - 1;
                }
                updateStockDisplay();
            }
        });
    }
}
