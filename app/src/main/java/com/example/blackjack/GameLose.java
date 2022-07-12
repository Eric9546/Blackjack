package com.example.blackjack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GameLose extends AppCompatActivity
{

    private final String RECORD_KEY = "record";
    private final String DATETIME_KEY = "dateTime";
    private final String TOTAL_ROUND = "totalRound";
    private final String TOTAL_ROUND_WON = "totalRoundWon";
    private SharedPreferences mPreferences;
    private String spFileName = "com.example.sharepreference";

    String record;

    Button mEndSession;
    Button mPlay;
    ProgressBar mProgress;
    TextView mSavingMsg;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lose);

        mEndSession = findViewById(R.id.btnEndSession);
        mPlay = findViewById(R.id.btnPlay);
        mProgress = findViewById(R.id.progress);
        mSavingMsg = findViewById(R.id.savingMsg);

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

    }

    public void btnEndSession_onClick (View view)
    {

        String dateTime = mPreferences.getString(DATETIME_KEY, "");
        int totalRound = mPreferences.getInt(TOTAL_ROUND, 0);
        int totalRoundWon = mPreferences.getInt(TOTAL_ROUND_WON, 0);
        double winrate = 0.0;
        String winrateString = "";
        String totalRoundString = "";
        String totalRoundWonString  = "";

        totalRoundString = Integer.toString(totalRound);

        totalRoundWonString = Integer.toString(totalRoundWon);

        // Calculate the winrate //
        winrate = (double) totalRoundWon / (double) totalRound;
        winrateString = String.format("%.2f", winrate);

        mProgress.setVisibility(View.VISIBLE);
        mSavingMsg.setVisibility(View.VISIBLE);

        // Save record history setting is turned on //
        if (mPreferences.contains(RECORD_KEY))
        {

            record = mPreferences.getString(RECORD_KEY, "disabled");

            if (record.equalsIgnoreCase("enabled"))
            {

                // Get user UID from database //
                userID = fAuth.getCurrentUser().getUid();

                DocumentReference documentReference = fStore.collection(userID).document(dateTime);

                // Store the record history into the database //
                Map<String, Object> user = new HashMap<>();
                user.put ("dateTime", dateTime);
                user.put ("totalRound", totalRoundString);
                user.put ("totalRoundWon", totalRoundWonString);
                user.put ("winrate", winrateString);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>()
                {

                    @Override
                    public void onSuccess(Void aVoid)
                    {

                        Toast.makeText(GameLose.this, "Game Session Recorded!", Toast.LENGTH_SHORT).show();

                    }

                }).addOnFailureListener(new OnFailureListener()
                {

                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        Toast.makeText(GameLose.this, "Game Session Not Recorded!", Toast.LENGTH_SHORT).show();

                    }

                });

                finishAffinity();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }

            else
            {

                finishAffinity();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }

        }

        // Dont store record history (Record history setting:off)
        else
        {

            finishAffinity();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        }

    }

    public void btnPlay_onClick (View view)
    {

        // Play another round //
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), PlayGame.class));
        finish();

    }

}