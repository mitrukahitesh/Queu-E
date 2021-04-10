package com.example.queu_e;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.queu_e.adapter.OrgAdapter;
import com.example.queu_e.model.Org;
import com.example.queu_e.model.OrganizationResponse;
import com.example.queu_e.utility.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrganizationActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private List<Org> orgs = new ArrayList<>();
    private OrgAdapter adapter;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        setReferences();
        dialog.showDialog();
        getOrgs();
    }

    private void getOrgs() {
        Call<OrganizationResponse> call = MainActivity.api.getAll();
        call.enqueue(new Callback<OrganizationResponse>() {
            @Override
            public void onResponse(Call<OrganizationResponse> call, Response<OrganizationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        orgs.clear();
                        orgs.addAll(response.body().getOrgs());
                        adapter.notifyDataSetChanged();
                    }
                }
                dialog.stopDialog();
            }

            @Override
            public void onFailure(Call<OrganizationResponse> call, Throwable t) {
                dialog.stopDialog();
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setReferences() {
        context = this;
        recyclerView = findViewById(R.id.recycler_org);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new OrgAdapter(context, orgs);
        recyclerView.setAdapter(adapter);
        dialog = new LoadingDialog(context);
    }
}