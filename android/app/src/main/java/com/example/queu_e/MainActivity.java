package com.example.queu_e;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.queu_e.adapter.TokensAdapter;
import com.example.queu_e.api.QueueApi;
import com.example.queu_e.model.TokenDetails;
import com.example.queu_e.model.TokenResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView recyclerView;
    private TokensAdapter adapter;
    private Button button;
    private Retrofit retrofit;
    public static QueueApi api;
    private TextView no_token;
    public static final String BASE_URL = "https://queu-e.herokuapp.com";
    private final List<TokenDetails> tokens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReferences();
        setRetrofit();
        getTokens();
        setRefresher();
    }

    private void setRefresher() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    refreshCurrentStatus();
                    SystemClock.sleep(20000);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void refreshCurrentStatus() {
        for (int i = 0; i < tokens.size(); ++i) {
            final int pos = i;
            Call<TokenResponse> call = api.getCurrent(tokens.get(pos).orgId, tokens.get(pos).task);
            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.isSuccessful()) {
                        TokenResponse r = response.body();
                        if (r.getCode() == 200) {
                            tokens.get(pos).current = r.getValue().longValue();
                            adapter.notifyItemChanged(pos);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {

                }
            });
        }
    }

    private void getTokens() {
        FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child(FirebaseAuth.getInstance().getUid())
                .child("tokens")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        no_token.setVisibility(View.GONE);
                        setOrgIdChildListener(snapshot.getKey());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setOrgIdChildListener(String orgId) {
        FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child(FirebaseAuth.getInstance().getUid())
                .child("tokens")
                .child(orgId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TokenDetails tokenDetails = new TokenDetails();
                        tokenDetails.orgId = orgId;
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                        tokenDetails.key = snapshot.getKey();
                        tokenDetails.org = (String) map.get("org");
                        tokenDetails.task = (String) map.get("task");
                        tokenDetails.myNum = (Long) map.get("myNum");
                        tokenDetails.current = (Long) map.get("current");
                        tokenDetails.availed = (Long) map.get("availed");
                        tokens.add(0, tokenDetails);
                        adapter.notifyItemInserted(0);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        TokenDetails tokenDetails = new TokenDetails();
                        tokenDetails.orgId = orgId;
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                        tokenDetails.key = snapshot.getKey();
                        tokenDetails.org = (String) map.get("org");
                        tokenDetails.task = (String) map.get("task");
                        tokenDetails.myNum = (Long) map.get("myNum");
                        tokenDetails.current = (Long) map.get("current");
                        tokenDetails.availed = (Long) map.get("availed");
                        for (int i = 0; i < tokens.size(); ++i) {
                            if (tokenDetails.equals(tokens.get(i))) {
                                tokens.get(i).myNum = tokenDetails.myNum;
                                adapter.notifyItemChanged(i);
                                return;
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(QueueApi.class);
    }

    private void setReferences() {
        context = this;
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        button = findViewById(R.id.button);
        no_token = findViewById(R.id.no_token);
        adapter = new TokensAdapter(context, tokens);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrganizationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (adapter.longPressed.size() == 0)
            super.onBackPressed();
        adapter.longPressed.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu_custom, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.contact_us:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, "mitrukahitesh@gmail.com");
                startActivity(Intent.createChooser(intent, "Send Email"));
        }
        return super.onOptionsItemSelected(item);
    }
}