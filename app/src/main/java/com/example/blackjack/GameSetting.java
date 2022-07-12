package com.example.blackjack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class GameSetting extends AppCompatActivity
{

    private final String MAX_KEY = "max";
    private final String RECORD_KEY = "record";
    private SharedPreferences mPreferences;
    private String spFileName = "com.example.sharepreference";

    int maxGames = 3;
    String record = "";

    TextView mSettingMsg;
    RadioButton mRb3, mRb4, mRb5;
    Button mUpdate;
    Switch mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        mSettingMsg = findViewById(R.id.settingMessage);
        mRb3 = findViewById(R.id.rb3);
        mRb4 = findViewById(R.id.rb4);
        mRb5 = findViewById(R.id.rb5);
        mUpdate = findViewById(R.id.btnUpdate);
        mRecord = findViewById(R.id.swtRecord);

        // Restore the max cards setting from shared preferences //
        if (mPreferences.contains(MAX_KEY))
        {

            maxGames = mPreferences.getInt(MAX_KEY, 0);
            mSettingMsg.setText("Current Maximum Cards: " + maxGames);

        }

        else
        {

            mSettingMsg.setText("Current Maximum Cards: " + maxGames);

        }

        // Restore the record game history setting from shared preferences //
        if (mPreferences.contains(RECORD_KEY))
        {

            record = mPreferences.getString(RECORD_KEY, "disabled");

            if (record.equalsIgnoreCase("enabled"))
            {

                mRecord.setChecked(true);

            }

            else
            {

                mRecord.setChecked(false);

            }

        }

        else
        {

            mRecord.setChecked(false);

        }

    }

    // User clicks radio button for maximum games //
    public void rb3_onClick (View view)
    {

        maxGames = 3;

    }

    public void rb4_onClick (View view)
    {

        maxGames = 4;

    }

    public void rb5_onClick (View view)
    {

        maxGames = 5;

    }

    public void updateSettings_onClick (View view)
    {

        SharedPreferences.Editor spEditor = mPreferences.edit();
        spEditor.putInt(MAX_KEY, maxGames);

        // Chech the user's selected maximum games radio button //
        if (mRecord.isChecked())
        {

            spEditor.putString(RECORD_KEY, "enabled");

        }

        else
        {

            spEditor.putString(RECORD_KEY, "disabled");

        }

        spEditor.apply();

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();

    }

}