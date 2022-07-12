package com.example.blackjack;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

import javax.annotation.Nullable;

public class ViewHistory extends AppCompatActivity
{

    private final LinkedList<String> mHistoryList = new LinkedList<>();
    private RecyclerView mHistoryView;
    private HistoryListAdapter mAdapter;
    private FloatingActionButton mRefresh;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        mHistoryView = findViewById(R.id.historyView);
        mRefresh = findViewById(R.id.refresh);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Get user UID from database //
        userID = fAuth.getCurrentUser().getUid();

        // Retrieve the user record history from database and display it //
        fStore.collection(userID).addSnapshotListener(new EventListener<QuerySnapshot>()
        {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {

                for (DocumentSnapshot snapshot : queryDocumentSnapshots)
                {

                    mHistoryList.add(snapshot.getString("dateTime") + "\nTotal Rounds: " + snapshot.getString("totalRound") + "\nTotal Rounds Won: "
                                     + snapshot.getString("totalRoundWon") + "\nWinrate: " + snapshot.getString("winrate") + "%\n*****************************");

                }

            }

        });

        // Display the record history //
        mAdapter = new HistoryListAdapter (this, mHistoryList);
        mHistoryView.setAdapter(mAdapter);
        mHistoryView.setLayoutManager(new LinearLayoutManager(this));

        // Refresh the page when button is clicked //
        mRefresh.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {

                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }

        });

    }

}