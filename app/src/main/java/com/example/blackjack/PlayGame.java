package com.example.blackjack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PlayGame extends AppCompatActivity
{

    private final String MAX_KEY = "max";
    private final String TOTAL_ROUND = "totalRound";
    private final String TOTAL_ROUND_WON = "totalRoundWon";
    private SharedPreferences mPreferences;
    private String spFileName = "com.example.sharepreference";

    Animation animation = new AlphaAnimation(0f, 1.0f);

    String [] drawnStatus = new String [51];

    int maxGames = 3;
    int playerGameCounter = 0;
    int playerCardCount = 0;
    int playerCardCountAlt = 0;

    int pcGameCounter = 0;
    int pcCardCount = 0;
    int pcCardCountAlt = 0;

    int totalRound = 0;
    int totalRoundWon = 0;

    boolean pcAces = false;

    TextView mMaxCardsMsg, mPlayerCardCount, mOrMsgPlayer, mPlayerCardCountAlt;
    TextView mPcCardCount, mOrMsgPc, mPcCardCountAlt;
    ImageView mCardPlayer, mCardPc;
    Button mStand,mHit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        mPreferences = getSharedPreferences(spFileName, MODE_PRIVATE);

        mPlayerCardCount = findViewById(R.id.playerCardCount);
        mOrMsgPlayer = findViewById(R.id.orMsgPlayer);
        mPlayerCardCountAlt = findViewById(R.id.playerCardCountAlt);

        mPcCardCount = findViewById(R.id.pcCardCount);
        mOrMsgPc = findViewById(R.id.orMsgPc);
        mPcCardCountAlt = findViewById(R.id.pcCardCountAlt);

        mMaxCardsMsg = findViewById(R.id.maxCardsMessage);
        mCardPlayer = findViewById(R.id.imgPlayerCardOne);
        mCardPc = findViewById(R.id.imgPcCardOne);

        mStand = findViewById(R.id.btnStand);
        mHit = findViewById(R.id.btnHit);

        // Retrieving them total rounds played //
        if (mPreferences.contains(TOTAL_ROUND))
        {

            totalRound = mPreferences.getInt(TOTAL_ROUND, 0);

        }

        // Retrieving them total rounds won //
        if (mPreferences.contains(TOTAL_ROUND_WON))
        {

            totalRoundWon = mPreferences.getInt(TOTAL_ROUND_WON, 0);

        }

        // Initialize the array //
        for (int i = 0; i < 51; i++)
        {

            drawnStatus [i] = "No";

        }

        // Retrieve the max draw cards settings //
        if (mPreferences.contains(MAX_KEY))
        {

            maxGames = mPreferences.getInt(MAX_KEY, 0);
            mMaxCardsMsg.setText("Maximum Cards: " + maxGames);

        }

        else
        {

            mMaxCardsMsg.setText("Maximum Cards: " + maxGames);

        }

        // Draw first 2 cards for player //
        playerGameUpdate();
        playerDrawCard();
        playerGameUpdate();
        playerDrawCard();

        // If player cards total to 21, they got blackjack and wins //
        if (playerCardCount == 21 || playerCardCountAlt == 21)
            {

                SharedPreferences.Editor spEditor = mPreferences.edit();

                totalRound = totalRound + 1;
                totalRoundWon = totalRoundWon + 1;

                spEditor.putInt(TOTAL_ROUND, totalRound);
                spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        startActivity(new Intent(getApplicationContext(), GameWin.class));

                    }
                }, 800);

            }

        // Draw first 2 cards for pc (Dealer) //
        pcGameUpdate();
        pcDrawCard();
        pcGameUpdate();
        pcDrawCard();

        // If pc (Dealer) cards total to 21, they got blackjack and wins //
        if (pcCardCount == 21 || pcCardCountAlt == 21)
        {

            SharedPreferences.Editor spEditor = mPreferences.edit();

            totalRound = totalRound + 1;

            spEditor.putInt(TOTAL_ROUND, totalRound);

            mCardPc = findViewById(R.id.imgPcCardTwo);
            mCardPc.setVisibility(View.VISIBLE);
            mPcCardCount.setVisibility(View.VISIBLE);

            if (pcAces == true)
            {

                mOrMsgPc.setVisibility(View.VISIBLE);
                mPcCardCountAlt.setVisibility(View.VISIBLE);

            }

            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {

                    startActivity(new Intent(getApplicationContext(), GameLose.class));

                }
            }, 800);

        }

        // Check for when the user wants to hit //
        mHit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                if (playerGameCounter < maxGames)
                {

                    // Update the game process //
                    playerGameUpdate();

                    // Draw a random card //
                    playerDrawCard();

                    // If player cards total is above 21, they lose //
                    if (playerCardCount > 21)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameLose.class));

                            }
                        }, 800);

                    }

                }

                else
                {

                    // If the player cards total is below 17, they win //
                    if (playerCardCount < 17)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;
                        totalRoundWon = totalRoundWon + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);
                        spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameWin.class));

                            }
                        }, 800);

                    }

                    // If the player cards total is above 21, they lose //
                    else if (playerCardCount > 21)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameLose.class));

                            }
                        }, 800);

                    }

                    // Pc (Dealer) draws cards //
                    while (pcCardCount < 17 && pcGameCounter < maxGames)
                    {

                        pcGameUpdate();
                        pcDrawCard();

                    }

                    mCardPc = findViewById(R.id.imgPcCardTwo);
                    mCardPc.setVisibility(View.VISIBLE);
                    mPcCardCount.setVisibility(View.VISIBLE);

                    if (pcAces == true)
                    {

                        mOrMsgPc.setVisibility(View.VISIBLE);
                        mPcCardCountAlt.setVisibility(View.VISIBLE);

                    }

                    // If the pc cards total is below 17, they win //
                    if (pcCardCount < 17)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameLose.class));

                            }
                        }, 800);

                    }

                    // If the pc cards total is above 21, they lose //
                    else if (pcCardCount > 21)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;
                        totalRoundWon = totalRoundWon + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);
                        spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameWin.class));

                            }
                        }, 800);

                    }

                    // Besides the special situation (on the maximum number of cards reach) stated above, the party who has total cards’ values 17-21 and highest will win the game //
                    else if (playerCardCount > pcCardCount || playerCardCountAlt > pcCardCountAlt)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;
                        totalRoundWon = totalRoundWon + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);
                        spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameWin.class));

                            }
                        }, 800);

                    }

                    else if (playerCardCount < pcCardCount || playerCardCountAlt < pcCardCountAlt)
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameLose.class));

                            }
                        }, 800);

                    }

                    // If both parties hit get a card count of 21, player wins //
                    else
                    {

                        SharedPreferences.Editor spEditor = mPreferences.edit();

                        totalRound = totalRound + 1;
                        totalRoundWon = totalRoundWon + 1;

                        spEditor.putInt(TOTAL_ROUND, totalRound);
                        spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                        spEditor.apply();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                startActivity(new Intent(getApplicationContext(), GameWin.class));

                            }
                        }, 800);

                    }

                }

            }

        });

        mStand.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                while (pcCardCount < 17 && pcGameCounter < maxGames)
                {

                    pcGameUpdate();
                    pcDrawCard();

                }

                mCardPc = findViewById(R.id.imgPcCardTwo);
                mCardPc.setVisibility(View.VISIBLE);
                mPcCardCount.setVisibility(View.VISIBLE);

                if (pcAces == true)
                {

                    mOrMsgPc.setVisibility(View.VISIBLE);
                    mPcCardCountAlt.setVisibility(View.VISIBLE);

                }

                // If the pc cards total is below 17, they win //
                if (pcCardCount < 17)
                {

                    SharedPreferences.Editor spEditor = mPreferences.edit();

                    totalRound = totalRound + 1;

                    spEditor.putInt(TOTAL_ROUND, totalRound);

                    spEditor.apply();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            startActivity(new Intent(getApplicationContext(), GameLose.class));

                        }
                    }, 800);

                }

                // If the pc cards total is above 21, they lose //
                else if (pcCardCount > 21)
                {

                    SharedPreferences.Editor spEditor = mPreferences.edit();

                    totalRound = totalRound + 1;
                    totalRoundWon = totalRoundWon + 1;

                    spEditor.putInt(TOTAL_ROUND, totalRound);
                    spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                    spEditor.apply();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            startActivity(new Intent(getApplicationContext(), GameWin.class));

                        }
                    }, 800);

                }

                // Besides the special situation (on the maximum number of cards reach) stated above, the party who has total cards’ values 17-21 and highest will win the game //
                else if (playerCardCount > pcCardCount || playerCardCountAlt > pcCardCountAlt)
                {

                    SharedPreferences.Editor spEditor = mPreferences.edit();

                    totalRound = totalRound + 1;
                    totalRoundWon = totalRoundWon + 1;

                    spEditor.putInt(TOTAL_ROUND, totalRound);
                    spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                    spEditor.apply();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            startActivity(new Intent(getApplicationContext(), GameWin.class));

                        }
                    }, 800);

                }

                else if (playerCardCount < pcCardCount || playerCardCountAlt < pcCardCountAlt)
                {

                    SharedPreferences.Editor spEditor = mPreferences.edit();

                    totalRound = totalRound + 1;

                    spEditor.putInt(TOTAL_ROUND, totalRound);

                    spEditor.apply();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            startActivity(new Intent(getApplicationContext(), GameLose.class));

                        }
                    }, 800);

                }

                // If both parties hit get a card count of 21, player wins //
                else
                {

                    SharedPreferences.Editor spEditor = mPreferences.edit();

                    totalRound = totalRound + 1;
                    totalRoundWon = totalRoundWon + 1;

                    spEditor.putInt(TOTAL_ROUND, totalRound);
                    spEditor.putInt(TOTAL_ROUND_WON, totalRoundWon);

                    spEditor.apply();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            startActivity(new Intent(getApplicationContext(), GameWin.class));

                        }
                    }, 800);

                }

            }

        });

    }

    public void playerDrawCard ()
    {

        // Setting animation //
        animation.setDuration(2000);
        mCardPlayer.setAnimation(animation);

        // Generate random card //
        int randomCard = 0;

        Random rand = new Random();

        randomCard = rand.nextInt(52);

        // Makes sure a different card is drawn //
        while (drawnStatus [randomCard] == "Yes")
        {

            randomCard = rand.nextInt(52);

        }

        switch (randomCard)
        {

            case 0:

                mCardPlayer.setImageResource(R.drawable.ace_of_hearts);
                drawnStatus [0] = "Yes";
                playerCardCount = playerCardCount + 1;
                playerCardCountAlt = playerCardCount + 10;

                mOrMsgPlayer.setVisibility(View.VISIBLE);
                mPlayerCardCountAlt.setVisibility(View.VISIBLE);

                break;

            case 1:

                mCardPlayer.setImageResource(R.drawable.two_of_spades);
                drawnStatus [1] = "Yes";
                playerCardCount = playerCardCount + 2;
                playerCardCountAlt = playerCardCountAlt + 2;
                break;

            case 2:

                mCardPlayer.setImageResource(R.drawable.three_of_diamonds);
                drawnStatus [2] = "Yes";
                playerCardCount = playerCardCount + 3;
                playerCardCountAlt = playerCardCountAlt + 3;
                break;

            case 3:

                mCardPlayer.setImageResource(R.drawable.four_of_clubs);
                drawnStatus [3] = "Yes";
                playerCardCount = playerCardCount + 4;
                playerCardCountAlt = playerCardCountAlt + 4;
                break;

            case 4:

                mCardPlayer.setImageResource(R.drawable.five_of_hearts);
                drawnStatus [4] = "Yes";
                playerCardCount = playerCardCount + 5;
                playerCardCountAlt = playerCardCountAlt + 5;
                break;

            case 5:

                mCardPlayer.setImageResource(R.drawable.six_of_spades);
                drawnStatus [5] = "Yes";
                playerCardCount = playerCardCount + 6;
                playerCardCountAlt = playerCardCountAlt + 6;
                break;

            case 6:

                mCardPlayer.setImageResource(R.drawable.seven_of_diamonds);
                drawnStatus [6] = "Yes";
                playerCardCount = playerCardCount + 7;
                playerCardCountAlt = playerCardCountAlt + 7;
                break;

            case 7:

                mCardPlayer.setImageResource(R.drawable.eight_of_clubs);
                drawnStatus [7] = "Yes";
                playerCardCount = playerCardCount + 8;
                playerCardCountAlt = playerCardCountAlt + 8;
                break;

            case 8:

                mCardPlayer.setImageResource(R.drawable.nine_of_hearts);
                drawnStatus [8] = "Yes";
                playerCardCount = playerCardCount + 9;
                playerCardCountAlt = playerCardCountAlt + 9;
                break;

            case 9:

                mCardPlayer.setImageResource(R.drawable.ten_of_spades);
                drawnStatus [9] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 10:

                mCardPlayer.setImageResource(R.drawable.jack_of_diamonds);
                drawnStatus [10] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 11:

                mCardPlayer.setImageResource(R.drawable.queen_of_clubs);
                drawnStatus [11] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 12:

                mCardPlayer.setImageResource(R.drawable.king_of_hearts);
                drawnStatus [12] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 13:

                mCardPlayer.setImageResource(R.drawable.ace_of_spades);
                drawnStatus [13] = "Yes";
                playerCardCount = playerCardCount + 1;
                playerCardCountAlt = playerCardCount + 10;

                mOrMsgPlayer.setVisibility(View.VISIBLE);
                mPlayerCardCountAlt.setVisibility(View.VISIBLE);
                break;

            case 14:

                mCardPlayer.setImageResource(R.drawable.two_of_diamonds);
                drawnStatus [14] = "Yes";
                playerCardCount = playerCardCount + 2;
                playerCardCountAlt = playerCardCountAlt + 2;
                break;

            case 15:

                mCardPlayer.setImageResource(R.drawable.three_of_clubs);
                drawnStatus [15] = "Yes";
                playerCardCount = playerCardCount + 3;
                playerCardCountAlt = playerCardCountAlt + 3;
                break;

            case 16:

                mCardPlayer.setImageResource(R.drawable.four_of_hearts);
                drawnStatus [16] = "Yes";
                playerCardCount = playerCardCount + 4;
                playerCardCountAlt = playerCardCountAlt + 4;
                break;

            case 17:

                mCardPlayer.setImageResource(R.drawable.five_of_spades);
                drawnStatus [17] = "Yes";
                playerCardCount = playerCardCount + 5;
                playerCardCountAlt = playerCardCountAlt + 5;
                break;

            case 18:

                mCardPlayer.setImageResource(R.drawable.six_of_diamonds);
                drawnStatus [18] = "Yes";
                playerCardCount = playerCardCount + 6;
                playerCardCountAlt = playerCardCountAlt + 6;
                break;

            case 19:

                mCardPlayer.setImageResource(R.drawable.seven_of_clubs);
                drawnStatus [19] = "Yes";
                playerCardCount = playerCardCount + 7;
                playerCardCountAlt = playerCardCountAlt + 7;
                break;

            case 20:

                mCardPlayer.setImageResource(R.drawable.eight_of_hearts);
                drawnStatus [20] = "Yes";
                playerCardCount = playerCardCount + 8;
                playerCardCountAlt = playerCardCountAlt + 8;
                break;

            case 21:

                mCardPlayer.setImageResource(R.drawable.nine_of_spades);
                drawnStatus [21] = "Yes";
                playerCardCount = playerCardCount + 9;
                playerCardCountAlt = playerCardCountAlt + 9;
                break;

            case 22:

                mCardPlayer.setImageResource(R.drawable.ten_of_diamonds);
                drawnStatus [22] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 23:

                mCardPlayer.setImageResource(R.drawable.jack_of_clubs);
                drawnStatus [23] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 24:

                mCardPlayer.setImageResource(R.drawable.queen_of_hearts);
                drawnStatus [24] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 25:

                mCardPlayer.setImageResource(R.drawable.king_of_spades);
                drawnStatus [25] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 26:

                mCardPlayer.setImageResource(R.drawable.ace_of_diamonds);
                drawnStatus [26] = "Yes";
                playerCardCount = playerCardCount + 1;
                playerCardCountAlt = playerCardCount + 10;

                mOrMsgPlayer.setVisibility(View.VISIBLE);
                mPlayerCardCountAlt.setVisibility(View.VISIBLE);
                break;

            case 27:

                mCardPlayer.setImageResource(R.drawable.two_of_clubs);
                drawnStatus [27] = "Yes";
                playerCardCount = playerCardCount + 2;
                playerCardCountAlt = playerCardCountAlt + 2;
                break;

            case 28:

                mCardPlayer.setImageResource(R.drawable.three_of_hearts);
                drawnStatus [28] = "Yes";
                playerCardCount = playerCardCount + 3;
                playerCardCountAlt = playerCardCountAlt + 3;
                break;

            case 29:

                mCardPlayer.setImageResource(R.drawable.four_of_spades);
                drawnStatus [29] = "Yes";
                playerCardCount = playerCardCount + 4;
                playerCardCountAlt = playerCardCountAlt + 4;
                break;

            case 30:

                mCardPlayer.setImageResource(R.drawable.five_of_diamonds);
                drawnStatus [30] = "Yes";
                playerCardCount = playerCardCount + 5;
                playerCardCountAlt = playerCardCountAlt + 5;
                break;

            case 31:

                mCardPlayer.setImageResource(R.drawable.six_of_clubs);
                drawnStatus [31] = "Yes";
                playerCardCount = playerCardCount + 6;
                playerCardCountAlt = playerCardCountAlt + 6;
                break;

            case 32:

                mCardPlayer.setImageResource(R.drawable.seven_of_hearts);
                drawnStatus [32] = "Yes";
                playerCardCount = playerCardCount + 7;
                playerCardCountAlt = playerCardCountAlt + 7;
                break;

            case 33:

                mCardPlayer.setImageResource(R.drawable.eight_of_spades);
                drawnStatus [33] = "Yes";
                playerCardCount = playerCardCount + 8;
                playerCardCountAlt = playerCardCountAlt + 8;
                break;

            case 34:

                mCardPlayer.setImageResource(R.drawable.nine_of_diamonds);
                drawnStatus [34] = "Yes";
                playerCardCount = playerCardCount + 9;
                playerCardCountAlt = playerCardCountAlt + 9;
                break;

            case 35:

                mCardPlayer.setImageResource(R.drawable.ten_of_clubs);
                drawnStatus [35] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 36:

                mCardPlayer.setImageResource(R.drawable.jack_of_hearts);
                drawnStatus [36] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 37:

                mCardPlayer.setImageResource(R.drawable.queen_of_spades);
                drawnStatus [37] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 38:

                mCardPlayer.setImageResource(R.drawable.king_of_diamonds);
                drawnStatus [38] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 39:

                mCardPlayer.setImageResource(R.drawable.ace_of_clubs);
                drawnStatus [39] = "Yes";
                playerCardCount = playerCardCount + 1;
                playerCardCountAlt = playerCardCount + 10;

                mOrMsgPlayer.setVisibility(View.VISIBLE);
                mPlayerCardCountAlt.setVisibility(View.VISIBLE);
                break;

            case 40:

                mCardPlayer.setImageResource(R.drawable.two_of_hearts);
                drawnStatus [40] = "Yes";
                playerCardCount = playerCardCount + 2;
                playerCardCountAlt = playerCardCountAlt + 2;
                break;

            case 41:

                mCardPlayer.setImageResource(R.drawable.three_of_spades);
                drawnStatus [41] = "Yes";
                playerCardCount = playerCardCount + 3;
                playerCardCountAlt = playerCardCountAlt + 3;
                break;

            case 42:

                mCardPlayer.setImageResource(R.drawable.four_of_diamonds);
                drawnStatus [42] = "Yes";
                playerCardCount = playerCardCount + 4;
                playerCardCountAlt = playerCardCountAlt + 4;
                break;

            case 43:

                mCardPlayer.setImageResource(R.drawable.five_of_clubs);
                drawnStatus [43] = "Yes";
                playerCardCount = playerCardCount + 5;
                playerCardCountAlt = playerCardCountAlt + 5;
                break;

            case 44:

                mCardPlayer.setImageResource(R.drawable.six_of_hearts);
                drawnStatus [44] = "Yes";
                playerCardCount = playerCardCount + 6;
                playerCardCountAlt = playerCardCountAlt + 6;
                break;

            case 45:

                mCardPlayer.setImageResource(R.drawable.seven_of_spades);
                drawnStatus [45] = "Yes";
                playerCardCount = playerCardCount + 7;
                playerCardCountAlt = playerCardCountAlt + 7;
                break;

            case 46:

                mCardPlayer.setImageResource(R.drawable.eight_of_diamonds);
                drawnStatus [46] = "Yes";
                playerCardCount = playerCardCount + 8;
                playerCardCountAlt = playerCardCountAlt + 8;
                break;

            case 47:

                mCardPlayer.setImageResource(R.drawable.nine_of_clubs);
                drawnStatus [47] = "Yes";
                playerCardCount = playerCardCount + 9;
                playerCardCountAlt = playerCardCountAlt + 9;
                break;

            case 48:

                mCardPlayer.setImageResource(R.drawable.ten_of_hearts);
                drawnStatus [48] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 49:

                mCardPlayer.setImageResource(R.drawable.jack_of_spades);
                drawnStatus [49] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 50:

                mCardPlayer.setImageResource(R.drawable.queen_of_diamonds);
                drawnStatus [50] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

            case 51:

                mCardPlayer.setImageResource(R.drawable.king_of_clubs);
                drawnStatus [51] = "Yes";
                playerCardCount = playerCardCount + 10;
                playerCardCountAlt = playerCardCountAlt + 10;
                break;

        }

        // Set the card count //
        String strPlayerCardCount = Integer.toString(playerCardCount);
        String strPlayerCardCountAlt = Integer.toString(playerCardCountAlt);

        mPlayerCardCount.setText(strPlayerCardCount);
        mPlayerCardCountAlt.setText(strPlayerCardCountAlt);

    }

    public void playerGameUpdate ()
    {

        // Update the appearance of cards on the game screen //
        switch (playerGameCounter)
        {

            case 0:

                mCardPlayer = findViewById(R.id.imgPlayerCardOne);
                break;

            case 1:

                mCardPlayer = findViewById(R.id.imgPlayerCardTwo);
                break;

            case 2:

                mCardPlayer = findViewById(R.id.imgPlayerCardThree);
                mCardPlayer.setVisibility(View.VISIBLE);
                break;

            case 3:

                mCardPlayer = findViewById(R.id.imgPlayerCardFour);
                mCardPlayer.setVisibility(View.VISIBLE);
                break;

            case 4:

                mCardPlayer = findViewById(R.id.imgPlayerCardFive);
                mCardPlayer.setVisibility(View.VISIBLE);
                break;

            default:

                break;

        }

        playerGameCounter++;

    }

    public void pcDrawCard ()
    {

        // Generate random card //
        int randomCard = 0;

        Random rand = new Random();

        randomCard = rand.nextInt(52);

        // Makes sure a different card is drawn //
        while (drawnStatus [randomCard] == "Yes")
        {

            randomCard = rand.nextInt(52);

        }

        switch (randomCard)
        {

            case 0:

                mCardPc.setImageResource(R.drawable.ace_of_hearts);
                drawnStatus [0] = "Yes";
                pcCardCount = pcCardCount + 1;
                pcCardCountAlt = pcCardCount + 10;
                pcAces = true;
                break;

            case 1:

                mCardPc.setImageResource(R.drawable.two_of_spades);
                drawnStatus [1] = "Yes";
                pcCardCount = pcCardCount + 2;
                pcCardCountAlt = pcCardCountAlt + 2;
                break;

            case 2:

                mCardPc.setImageResource(R.drawable.three_of_diamonds);
                drawnStatus [2] = "Yes";
                pcCardCount = pcCardCount + 3;
                pcCardCountAlt = pcCardCountAlt + 3;
                break;

            case 3:

                mCardPc.setImageResource(R.drawable.four_of_clubs);
                drawnStatus [3] = "Yes";
                pcCardCount = pcCardCount + 4;
                pcCardCountAlt = pcCardCountAlt + 4;
                break;

            case 4:

                mCardPc.setImageResource(R.drawable.five_of_hearts);
                drawnStatus [4] = "Yes";
                pcCardCount = pcCardCount + 5;
                pcCardCountAlt = pcCardCountAlt + 5;
                break;

            case 5:

                mCardPc.setImageResource(R.drawable.six_of_spades);
                drawnStatus [5] = "Yes";
                pcCardCount = pcCardCount + 6;
                pcCardCountAlt = pcCardCountAlt + 6;
                break;

            case 6:

                mCardPc.setImageResource(R.drawable.seven_of_diamonds);
                drawnStatus [6] = "Yes";
                pcCardCount = pcCardCount + 7;
                pcCardCountAlt = pcCardCountAlt + 7;
                break;

            case 7:

                mCardPc.setImageResource(R.drawable.eight_of_clubs);
                drawnStatus [7] = "Yes";
                pcCardCount = pcCardCount + 8;
                pcCardCountAlt = pcCardCountAlt + 8;
                break;

            case 8:

                mCardPc.setImageResource(R.drawable.nine_of_hearts);
                drawnStatus [8] = "Yes";
                pcCardCount = pcCardCount + 9;
                pcCardCountAlt = pcCardCountAlt + 9;
                break;

            case 9:

                mCardPc.setImageResource(R.drawable.ten_of_spades);
                drawnStatus [9] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 10:

                mCardPc.setImageResource(R.drawable.jack_of_diamonds);
                drawnStatus [10] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 11:

                mCardPc.setImageResource(R.drawable.queen_of_clubs);
                drawnStatus [11] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 12:

                mCardPc.setImageResource(R.drawable.king_of_hearts);
                drawnStatus [12] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 13:

                mCardPc.setImageResource(R.drawable.ace_of_spades);
                drawnStatus [13] = "Yes";
                pcCardCount = pcCardCount + 1;
                pcCardCountAlt = pcCardCount + 10;
                pcAces = true;
                break;

            case 14:

                mCardPc.setImageResource(R.drawable.two_of_diamonds);
                drawnStatus [14] = "Yes";
                pcCardCount = pcCardCount + 2;
                pcCardCountAlt = pcCardCountAlt + 2;
                break;

            case 15:

                mCardPc.setImageResource(R.drawable.three_of_clubs);
                drawnStatus [15] = "Yes";
                pcCardCount = pcCardCount + 3;
                pcCardCountAlt = pcCardCountAlt + 3;
                break;

            case 16:

                mCardPc.setImageResource(R.drawable.four_of_hearts);
                drawnStatus [16] = "Yes";
                pcCardCount = pcCardCount + 4;
                pcCardCountAlt = pcCardCountAlt + 4;
                break;

            case 17:

                mCardPc.setImageResource(R.drawable.five_of_spades);
                drawnStatus [17] = "Yes";
                pcCardCount = pcCardCount + 5;
                pcCardCountAlt = pcCardCountAlt + 5;
                break;

            case 18:

                mCardPc.setImageResource(R.drawable.six_of_diamonds);
                drawnStatus [18] = "Yes";
                pcCardCount = pcCardCount + 6;
                pcCardCountAlt = pcCardCountAlt + 6;
                break;

            case 19:

                mCardPc.setImageResource(R.drawable.seven_of_clubs);
                drawnStatus [19] = "Yes";
                pcCardCount = pcCardCount + 7;
                pcCardCountAlt = pcCardCountAlt + 7;
                break;

            case 20:

                mCardPc.setImageResource(R.drawable.eight_of_hearts);
                drawnStatus [20] = "Yes";
                pcCardCount = pcCardCount + 8;
                pcCardCountAlt = pcCardCountAlt + 8;
                break;

            case 21:

                mCardPc.setImageResource(R.drawable.nine_of_spades);
                drawnStatus [21] = "Yes";
                pcCardCount = pcCardCount + 9;
                pcCardCountAlt = pcCardCountAlt + 9;
                break;

            case 22:

                mCardPc.setImageResource(R.drawable.ten_of_diamonds);
                drawnStatus [22] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 23:

                mCardPc.setImageResource(R.drawable.jack_of_clubs);
                drawnStatus [23] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 24:

                mCardPc.setImageResource(R.drawable.queen_of_hearts);
                drawnStatus [24] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 25:

                mCardPc.setImageResource(R.drawable.king_of_spades);
                drawnStatus [25] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 26:

                mCardPc.setImageResource(R.drawable.ace_of_diamonds);
                drawnStatus [26] = "Yes";
                pcCardCount = pcCardCount + 1;
                pcCardCountAlt = pcCardCount + 10;
                pcAces = true;
                break;

            case 27:

                mCardPc.setImageResource(R.drawable.two_of_clubs);
                drawnStatus [27] = "Yes";
                pcCardCount = pcCardCount + 2;
                pcCardCountAlt = pcCardCountAlt + 2;
                break;

            case 28:

                mCardPc.setImageResource(R.drawable.three_of_hearts);
                drawnStatus [28] = "Yes";
                pcCardCount = pcCardCount + 3;
                pcCardCountAlt = pcCardCountAlt + 3;
                break;

            case 29:

                mCardPc.setImageResource(R.drawable.four_of_spades);
                drawnStatus [29] = "Yes";
                pcCardCount = pcCardCount + 4;
                pcCardCountAlt = pcCardCountAlt + 4;
                break;

            case 30:

                mCardPc.setImageResource(R.drawable.five_of_diamonds);
                drawnStatus [30] = "Yes";
                pcCardCount = pcCardCount + 5;
                pcCardCountAlt = pcCardCountAlt + 5;
                break;

            case 31:

                mCardPc.setImageResource(R.drawable.six_of_clubs);
                drawnStatus [31] = "Yes";
                pcCardCount = pcCardCount + 6;
                pcCardCountAlt = pcCardCountAlt + 6;
                break;

            case 32:

                mCardPc.setImageResource(R.drawable.seven_of_hearts);
                drawnStatus [32] = "Yes";
                pcCardCount = pcCardCount + 7;
                pcCardCountAlt = pcCardCountAlt + 7;
                break;

            case 33:

                mCardPc.setImageResource(R.drawable.eight_of_spades);
                drawnStatus [33] = "Yes";
                pcCardCount = pcCardCount + 8;
                pcCardCountAlt = pcCardCountAlt + 8;
                break;

            case 34:

                mCardPc.setImageResource(R.drawable.nine_of_diamonds);
                drawnStatus [34] = "Yes";
                pcCardCount = pcCardCount + 9;
                pcCardCountAlt = pcCardCountAlt + 9;
                break;

            case 35:

                mCardPc.setImageResource(R.drawable.ten_of_clubs);
                drawnStatus [35] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 36:

                mCardPc.setImageResource(R.drawable.jack_of_hearts);
                drawnStatus [36] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 37:

                mCardPc.setImageResource(R.drawable.queen_of_spades);
                drawnStatus [37] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 38:

                mCardPc.setImageResource(R.drawable.king_of_diamonds);
                drawnStatus [38] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 39:

                mCardPc.setImageResource(R.drawable.ace_of_clubs);
                drawnStatus [39] = "Yes";
                pcCardCount = pcCardCount + 1;
                pcCardCountAlt = pcCardCount + 10;
                pcAces = true;
                break;

            case 40:

                mCardPc.setImageResource(R.drawable.two_of_hearts);
                drawnStatus [40] = "Yes";
                pcCardCount = pcCardCount + 2;
                pcCardCountAlt = pcCardCountAlt + 2;
                break;

            case 41:

                mCardPc.setImageResource(R.drawable.three_of_spades);
                drawnStatus [41] = "Yes";
                pcCardCount = pcCardCount + 3;
                pcCardCountAlt = pcCardCountAlt + 3;
                break;

            case 42:

                mCardPc.setImageResource(R.drawable.four_of_diamonds);
                drawnStatus [42] = "Yes";
                pcCardCount = pcCardCount + 4;
                pcCardCountAlt = pcCardCountAlt + 4;
                break;

            case 43:

                mCardPc.setImageResource(R.drawable.five_of_clubs);
                drawnStatus [43] = "Yes";
                pcCardCount = pcCardCount + 5;
                pcCardCountAlt = pcCardCountAlt + 5;
                break;

            case 44:

                mCardPc.setImageResource(R.drawable.six_of_hearts);
                drawnStatus [44] = "Yes";
                pcCardCount = pcCardCount + 6;
                pcCardCountAlt = pcCardCountAlt + 6;
                break;

            case 45:

                mCardPc.setImageResource(R.drawable.seven_of_spades);
                drawnStatus [45] = "Yes";
                pcCardCount = pcCardCount + 7;
                pcCardCountAlt = pcCardCountAlt + 7;
                break;

            case 46:

                mCardPc.setImageResource(R.drawable.eight_of_diamonds);
                drawnStatus [46] = "Yes";
                pcCardCount = pcCardCount + 8;
                pcCardCountAlt = pcCardCountAlt + 8;
                break;

            case 47:

                mCardPc.setImageResource(R.drawable.nine_of_clubs);
                drawnStatus [47] = "Yes";
                pcCardCount = pcCardCount + 9;
                pcCardCountAlt = pcCardCountAlt + 9;
                break;

            case 48:

                mCardPc.setImageResource(R.drawable.ten_of_hearts);
                drawnStatus [48] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 49:

                mCardPc.setImageResource(R.drawable.jack_of_spades);
                drawnStatus [49] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 50:

                mCardPc.setImageResource(R.drawable.queen_of_diamonds);
                drawnStatus [50] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

            case 51:

                mCardPc.setImageResource(R.drawable.king_of_clubs);
                drawnStatus [51] = "Yes";
                pcCardCount = pcCardCount + 10;
                pcCardCountAlt = pcCardCountAlt + 10;
                break;

        }

        // Set the card count //
        String strPcCardCount = Integer.toString(pcCardCount);
        String strPcCardCountAlt = Integer.toString(pcCardCountAlt);

        mPcCardCount.setText(strPcCardCount);
        mPcCardCountAlt.setText(strPcCardCountAlt);

    }

    public void pcGameUpdate ()
    {

        // Setting animation //
        animation.setDuration(2000);
        mCardPc.setAnimation(animation);

        switch (pcGameCounter)
        {

            case 0:

                mCardPc = findViewById(R.id.imgPcCardOne);
                break;

            case 1:

                mCardPc = findViewById(R.id.imgPcCardTwo);
                break;

            case 2:

                mCardPc = findViewById(R.id.imgPcCardThree);
                mCardPc.setVisibility(View.VISIBLE);
                break;

            case 3:

                mCardPc = findViewById(R.id.imgPcCardFour);
                mCardPc.setVisibility(View.VISIBLE);
                break;

            case 4:

                mCardPc = findViewById(R.id.imgPcCardFive);
                mCardPc.setVisibility(View.VISIBLE);
                break;

            default:

                break;

        }

        pcGameCounter++;

    }

}