package com.example.lrgeneratorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView emptyView;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.historyRecycler);
        emptyView = findViewById(R.id.tvEmpty);

        dbHelper = new DatabaseHelper(this);

        ArrayList<LRHistory> list = dbHelper.getAllHistory();

        if(list.size() == 0){

            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);

        }else{

            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            HistoryAdapter adapter = new HistoryAdapter(this, list, () -> {

                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);

            });

            recyclerView.setAdapter(adapter);
        }
    }
}