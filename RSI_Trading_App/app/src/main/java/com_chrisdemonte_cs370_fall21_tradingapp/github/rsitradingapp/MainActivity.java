package com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import com_chrisdemonte_cs370_fall21_tradingapp.github.rsitradingapp.controllers.UserUtils;

public class MainActivity extends AppCompatActivity {

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
         */
        createNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
}
