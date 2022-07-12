package com.example.blackjack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class HistoryListAdapter extends Adapter <HistoryListAdapter.WordViewHolder>
{

    private LinkedList <String> mWordList;
    private LayoutInflater mInflator;

    class WordViewHolder extends RecyclerView.ViewHolder
    {

        public final TextView wordListItem;
        final HistoryListAdapter mAdapter;

        WordViewHolder(View itemView, HistoryListAdapter adapter)
        {

            super(itemView);
            wordListItem = itemView.findViewById(R.id.history);
            this.mAdapter = adapter;

        }

    }

    @NonNull
    @Override
    public HistoryListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position)
    {

        View mItemView = mInflator.inflate(R.layout.historylist_item, parent, false);


        return new WordViewHolder(mItemView, this);

    }

    @Override
    public void onBindViewHolder(HistoryListAdapter.WordViewHolder viewHolder, int position)
    {

        String mCurrent  = mWordList.get(position);
        viewHolder.wordListItem.setText (mCurrent);

    }

    public int getItemCount()
    {

        return mWordList.size();

    }

    HistoryListAdapter(Context context, LinkedList <String> wordList)
    {

        mInflator = LayoutInflater.from(context);
        this.mWordList = wordList;

    }

}


