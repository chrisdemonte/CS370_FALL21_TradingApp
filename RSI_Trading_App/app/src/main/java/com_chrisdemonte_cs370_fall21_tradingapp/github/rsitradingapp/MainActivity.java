package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.HashMap;
import java.util.Map;

import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.controllers.StockUtils;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.controllers.UserUtils;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.AccountEditFragment;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.AddStockFragment;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.HomeFragment;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.LoginFragment;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.NewAccountActivity;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models.Stock;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models.User;

public class MainActivity extends AppCompatActivity {

    public static User USER;
    public static int displayedStock = 0;
    private int stockDownloadProgress = 0;
    public FirebaseFirestore database;
    public LoginFragment login = new LoginFragment();
    public HomeFragment home = new HomeFragment();
    public AccountEditFragment accountEdit = new AccountEditFragment();
    public AddStockFragment addStockFragment = new AddStockFragment();

    private boolean changes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        USER = new User("", "", "");
        database = FirebaseFirestore.getInstance();
        //this.saveUserToDatabase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        giveLoginButtonsActions();
        loadLoginFragment();
        giveHomeButtonsActions();
        giveAccountEditButtonsActions();
    }

    private void loadNewAccountActivity(){
        Intent intent = new Intent(this, NewAccountActivity.class);
        //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void loadHomeFragment(){
        findViewById(R.id.loginLayout).setVisibility(View.GONE);
        findViewById(R.id.homeLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.accountEditFragment).setVisibility(View.GONE);
        findViewById(R.id.addStockFragment).setVisibility(View.GONE);
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
        findViewById(R.id.accountEditFragment).setVisibility(View.GONE);
        findViewById(R.id.addStockFragment).setVisibility(View.GONE);

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
                attemptLogin();
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
        Button editAccountButton = findViewById(R.id.editButton);
        Button trackNewStockButton = findViewById(R.id.addNewStockButton);

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
                    displayedStock = (int)USER.getNumStocks() - 1;
                }
                updateStockDisplay();
            }
        });
        editAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAccountEditFragment();
            }
        });
        trackNewStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAddStockFragment();
            }
        });
    }
    public void loadAccountEditFragment(){
        findViewById(R.id.loginLayout).setVisibility(View.GONE);
        findViewById(R.id.homeLayout).setVisibility(View.GONE);
        findViewById(R.id.accountEditFragment).setVisibility(View.VISIBLE);
        findViewById(R.id.addStockFragment).setVisibility(View.GONE);

        TextView usernameView = findViewById(R.id.usernameView);
        usernameView.setText(USER.getUsername());

        EditText passwordEntry = findViewById(R.id.editTextPassword);

        EditText emailEntry = findViewById(R.id.emailEditText);
        emailEntry.setText(USER.getEmail());

        EditText capitalEntry = findViewById(R.id.capitalEditText);
        double capital = ((double)USER.getCapital())/100.0;
        capitalEntry.setText(capital + "");

        refreshStockSpinner();
        Spinner spinner = findViewById(R.id.removeStockSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Stock selectedItem = (Stock) parent.getItemAtPosition(position);
                TextView amount = findViewById(R.id.stockOwnedView);
                amount.setText("Amount Owned: " + selectedItem.getNumOwned());

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
                TextView amount = findViewById(R.id.stockOwnedView);
                amount.setText("Amount Owned:");
            }
        });

        Button removeStockButton = findViewById(R.id.removeStockButton);
        removeStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = findViewById(R.id.removeStockSpinner);
                TextView amount = findViewById(R.id.stockOwnedView);
                Stock stock = (Stock) spinner.getSelectedItem();
                if (stock!=null) {
                    USER.removeStock(stock);
                    amount.setText("Amount Owned:");
                    refreshStockSpinner();
                }
            }
        });

        Button addStockButton = findViewById(R.id.addStockButton);
        addStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = findViewById(R.id.removeStockSpinner);
                TextView amount = findViewById(R.id.stockOwnedView);
                Stock stock = (Stock) spinner.getSelectedItem();
                if (stock!=null) {
                    stock.setNumOwned(stock.getNumOwned() + 1);
                    amount.setText("Amount Owned: " + stock.getNumOwned());
                }
            }
        });
        Button reduceStockButton = findViewById(R.id.reduceStockButton);
        reduceStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = findViewById(R.id.removeStockSpinner);
                TextView amount = findViewById(R.id.stockOwnedView);
                Stock stock = (Stock) spinner.getSelectedItem();
                if (stock!=null) {
                    if (stock.getNumOwned() > 0) {
                        stock.setNumOwned(stock.getNumOwned() - 1);
                        amount.setText("Amount Owned: " + stock.getNumOwned());
                    }
                }
            }
        });

    }
    public void loadAddStockFragment(){
        findViewById(R.id.loginLayout).setVisibility(View.GONE);
        findViewById(R.id.homeLayout).setVisibility(View.GONE);
        findViewById(R.id.accountEditFragment).setVisibility(View.GONE);
        findViewById(R.id.addStockFragment).setVisibility(View.VISIBLE);
        changes = false;

        Button doneButton = findViewById(R.id.newStockDoneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changes){
                    //saveUserToDatabase();
                }
                loadHomeFragment();
            }
        });
        Button stockSearchButton = findViewById(R.id.stockSearchButton);
        EditText tickerInput = findViewById(R.id.tickerInputEditText);
        TextView stockDataOutput = findViewById(R.id.newStockDataTextView);
        stockSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = tickerInput.getText().toString();
                String stockData = StockUtils.getBasicStockData(ticker);
                stockDataOutput.setText(stockData);
            }
        });
        Button trackNewButton = findViewById(R.id.trackNewStockButton);
        trackNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stockDataOutput.getText().toString().contentEquals("Invalid Ticker")){
                    USER.addStock(StockUtils.temp);
                    changes = true;
                }
            }
        });
    }
    public void refreshStockSpinner(){
        Spinner spinner = findViewById(R.id.removeStockSpinner);
        ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this, android.R.layout.simple_spinner_item, USER.getStocks());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
    public void giveAccountEditButtonsActions(){
        Button cancelButton = findViewById(R.id.cancelButton);
        Button saveButton = findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHomeFragment();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void attemptLogin(){
        final EditText usernameEntry = findViewById(R.id.usernameEntry);
        String username = usernameEntry.getText().toString();

        CollectionReference userData = database.collection("userdata");
        DocumentReference userDoc = userData.document(username);

        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final TextView errorTextView = findViewById(R.id.errorMessageTextView);
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()){
                        final EditText passwordEntry = findViewById(R.id.passwordEntry);
                        String password = passwordEntry.getText().toString();
                        String docPass = (String) doc.get("password");
                        if (password.contentEquals(docPass)){
                            errorTextView.setVisibility(View.INVISIBLE);
                            loadUserFromDatabase(doc);
                            loadHomeFragment();
                        }
                        else {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText("Password Incorrect.");
                        }

                    }
                    else {
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText("No Such User Exists.");
                        }
                    }
                else {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Database Error.");
                }
            }
        });
    }
    public void saveUserToDatabase(){
        DocumentReference userTable = database.document("userdata/"+ USER.getUsername());
        Map<String, Object> user1 = new HashMap<String, Object>();
        user1.put("username", USER.getUsername());
        user1.put("password", USER.getPassword());
        user1.put("email", USER.getEmail());
        user1.put("capital", USER.getCapital());
        user1.put("numStocks", USER.getNumStocks());
        userTable.set(user1);

        for (int i = 0; i < USER.getNumStocks(); i++){
            DocumentReference stockTable = database.document("stockdata/"+ USER.getUsername() + "_" + i);
            Map<String, Object> stockData = new HashMap<String, Object>();
            Stock stock = USER.getStocks().get(i);
            stockData.put("ticker", stock.getTicker());
            stockData.put("company", stock.getCompany());
            stockData.put("numOwned", stock.getNumOwned());
            stockTable.set(stockData);
        }

    }

    public void loadUserFromDatabase(DocumentSnapshot userData){
        USER.setUsername((String)userData.get("username"));
        USER.setPassword((String)userData.get("password"));
        USER.setEmail((String)userData.get("email"));
        Long cap = (long)userData.get("capital");
        USER.setCapital(cap.intValue());
        USER.setNumStocks(0);
        Long num = (long)userData.get("numStocks");
        this.stockDownloadProgress = num.intValue();

        CollectionReference stockData = database.collection("stockdata");
        for (int i = 0; i < this.stockDownloadProgress; i++) {
            DocumentReference stockDoc = stockData.document(USER.getUsername() + "_" + i);

            stockDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    final TextView errorTextView = findViewById(R.id.errorMessageTextView);
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            Long num = (long)doc.get("numOwned");
                            Stock stock = new Stock ((String)doc.get("ticker"), (String)doc.get("company"), num.intValue());
                            USER.addStock(stock);
                            if (USER.getNumStocks() >= stockDownloadProgress){
                                loadHomeFragment();
                            }

                        } else {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText("Error getting stock data");
                        }
                    } else {
                        errorTextView.setVisibility(View.VISIBLE);
                        errorTextView.setText("Database Error getting stock data.");
                    }
                }
            });
        }


    }
}
