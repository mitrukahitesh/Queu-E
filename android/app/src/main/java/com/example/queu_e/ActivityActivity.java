package com.example.queu_e;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.queu_e.adapter.ActivityAdapter;
import com.example.queu_e.adapter.OrgAdapter;
import com.example.queu_e.model.Activity;
import com.example.queu_e.model.Org;

import java.util.ArrayList;
import java.util.List;

public class ActivityActivity extends AppCompatActivity {

    private Context context;
    private Org org;
    private RecyclerView recyclerView;
    private List<Activity> activities = new ArrayList<>();
    private ActivityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        setReferences();
    }

    private void setReferences() {
        context = this;
        recyclerView = findViewById(R.id.recycler_ac);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        org = OrgAdapter.org;
        activities = org.getActivities();
        adapter = new ActivityAdapter(context, activities, org.getId(), org.getName());
        recyclerView.setAdapter(adapter);
    }
}