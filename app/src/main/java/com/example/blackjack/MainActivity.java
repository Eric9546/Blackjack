package com.example.blackjack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{

    private final String DATETIME_KEY = "dateTime";
    private final String TOTAL_ROUND = "totalRound";
    private final String TOTAL_ROUND_WON = "totalRoundWon";
    private SharedPreferences mPreferences;
    private String spFileName = "com.example.sharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

    }

    public void playGame_onClick (View view)
    {

        String dateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // Resets the game preferences components //
        SharedPreferences.Editor spEditor = mPreferences.edit();
        spEditor.putString(DATETIME_KEY, dateTime);
        spEditor.putInt(TOTAL_ROUND, 0);
        spEditor.putInt(TOTAL_ROUND_WON, 0);
        spEditor.apply();

        startActivity(new Intent(getApplicationContext(), PlayGame.class));

    }

    public void gameSetting_onClick (View view)
    {

        startActivity(new Intent(getApplicationContext(), GameSetting.class));

    }

    public void viewHistory_onClick (View view)
    {

        startActivity(new Intent(getApplicationContext(), ViewHistory.class));

    }

    public void logout_onClick (View view)
    {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }

}