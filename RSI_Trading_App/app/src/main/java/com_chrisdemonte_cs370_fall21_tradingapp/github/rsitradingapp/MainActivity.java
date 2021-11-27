package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.controllers.UserUtils;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.gui.NewAccountActivity;
import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.models.User;

public class MainActivity extends AppCompatActivity {

    public static User USER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Create references to the GUI objects in the Main Activity
         */
        final EditText usernameEntry = findViewById(R.id.usernameEntry);
        final EditText passwordEntry = findViewById(R.id.passwordEntry);
        final TextView errorTextView = findViewById(R.id.errorMessageTextView);
        Button loginButton = findViewById(R.id.loginButton);
        Button createNewButton = findViewById(R.id.newAccountButton);
        /**
         * Code for the LOGIN BUTTON
         * onClick() Tests if the username and password are valid strings according to specs in the UserUtil class
         * To Do: database check, and logging into homepage
         */
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
            }
        });
        /**
         * Code for the CREATE NEW ACCOUNT BUTTON
         * onClick() Loads the New Account Activity
         */
        createNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadNewAccountActivity();
            }
        });
    }

    /**
     * Method for loading the NEW ACCOUNT ACTIVITY
     * To Do: left commented code for message passing
     */
    private void loadNewAccountActivity(){
        Intent intent = new Intent(this, NewAccountActivity.class);
        //   intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
