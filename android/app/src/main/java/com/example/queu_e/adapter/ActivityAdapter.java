package com.example.queu_e.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queu_e.MainActivity;
import com.example.queu_e.R;
import com.example.queu_e.model.Activity;
import com.example.queu_e.model.TokenDetails;
import com.example.queu_e.model.TokenResponse;
import com.example.queu_e.utility.LoadingDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.CustomVH> {

    private Context context;
    private List<Activity> activities;
    private String id;
    private String name;
    private LoadingDialog dialog;

    public ActivityAdapter(Context context, List<Activity> activities, String id, String name) {
        this.context = context;
        this.activities = activities;
        this.id = id;
        this.name = name;
        dialog = new LoadingDialog(context);
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.sun.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));
        holder.mon.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));
        holder.tue.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));
        holder.wed.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));
        holder.thu.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));
        holder.fri.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));
        holder.sat.setBackground(ContextCompat.getDrawable(context, R.drawable.day_off));

        for (int i = 0; i < activities.get(position).getDay().size(); ++i) {
            switch (activities.get(position).getDay().get(i)) {
                case 0:
                    holder.sun.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
                case 1:
                    holder.mon.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
                case 2:
                    holder.tue.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
                case 3:
                    holder.wed.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
                case 4:
                    holder.thu.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
                case 5:
                    holder.fri.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
                case 6:
                    holder.sat.setBackground(ContextCompat.getDrawable(context, R.drawable.day_text));
                    break;
            }
        }

        holder.name.setText(activities.get(position).getName());
        holder.time.setText(formatTime(activities.get(position).getStarthour(),
                activities.get(position).getStartmin(),
                activities.get(position).getEndhour(),
                activities.get(position).getEndmin()));
    }

    private String formatTime(Integer starthour, Integer startmin, Integer endhour, Integer endmin) {
        StringBuilder builder = new StringBuilder();
        if (starthour < 10) {
            builder.append('0').append(starthour).append(':');
        } else {
            builder.append(starthour).append(':');
        }
        if (startmin < 10) {
            builder.append('0').append(startmin).append(' ').append('-').append(' ');
        } else {
            builder.append(startmin).append(' ').append('-').append(' ');
        }
        if (endhour < 10) {
            builder.append('0').append(endhour).append(':');
        } else {
            builder.append(endhour).append(':');
        }
        if (endmin < 10) {
            builder.append('0').append(endmin);
        } else {
            builder.append(endmin);
        }
        return new String(builder);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        TextView name, time, sun, mon, tue, wed, thu, fri, sat;
        TextView request;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            sun = itemView.findViewById(R.id.sun);
            mon = itemView.findViewById(R.id.mon);
            tue = itemView.findViewById(R.id.tue);
            wed = itemView.findViewById(R.id.wed);
            thu = itemView.findViewById(R.id.thu);
            fri = itemView.findViewById(R.id.fri);
            sat = itemView.findViewById(R.id.sat);
            request = itemView.findViewById(R.id.request);
            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestToken(getAdapterPosition());
                }
            });
        }
    }

    private void requestToken(int adapterPosition) {
        Calendar c = Calendar.getInstance();
        int day = c.get(c.DAY_OF_WEEK);
        --day;
        int hr = c.get(c.HOUR_OF_DAY);
        int min = c.get(c.MINUTE);
        Activity ac = activities.get(adapterPosition);
        if (ac.getDay().contains(day)) {
            if (ac.getStarthour() <= ac.getEndhour()) {
                if (hr < ac.getEndhour() && hr >= ac.getStarthour()) {
                    fireRequest(ac);
                } else if (hr == ac.getEndhour()) {
                    if (min < ac.getEndmin()) {
                        fireRequest(ac);
                    } else {
                        Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                }
            } else {
                if (hr < ac.getStarthour() && hr > ac.getEndhour()) {
                    Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                } else {
                    if (hr >= ac.getStarthour() || hr < ac.getEndhour()) {
                        fireRequest(ac);
                    } else if (hr == ac.getEndhour()) {
                        if (min < ac.getEndmin()) {
                            fireRequest(ac);
                        } else {
                            Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            Toast.makeText(context, "Closed", Toast.LENGTH_LONG).show();
        }
    }

    private void fireRequest(Activity ac) {
        dialog.showDialog();
        FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("tokens")
                .child(id)
                .orderByChild("task")
                .equalTo(ac.getName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            addFreshData(ac);
                        } else {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                Long prevTime = (Long) s.child("availed").getValue();
                                Map<String, Object> map = new HashMap<>();
                                Long curr = System.currentTimeMillis();
                                if ((curr - prevTime) < 600000) {
                                    dialog.stopDialog();
                                    Toast.makeText(context,
                                            "Too early request!\n Request after " + (600 - ((curr - prevTime) / 1000)) + " seconds",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    return;
                                }
                                Call<TokenResponse> call = MainActivity.api.requestToken(id, ac.getName());
                                call.enqueue(new Callback<TokenResponse>() {
                                    @Override
                                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getCode() == 200) {
                                                map.put("availed", curr);
                                                map.put("myNum", response.body().getValue());
                                                FirebaseDatabase.getInstance().getReference()
                                                        .child("USERS")
                                                        .child(FirebaseAuth.getInstance().getUid())
                                                        .child("tokens")
                                                        .child(id)
                                                        .child(Objects.requireNonNull(s.getKey()))
                                                        .updateChildren(map)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                dialog.stopDialog();
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(context, "Your token number is " + response.body().getValue() + "\nYou can check status on home screen", Toast.LENGTH_LONG).show();
                                                                } else {
                                                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        } else {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addFreshData(Activity ac) {
        Call<TokenResponse> call = MainActivity.api.requestToken(id, ac.getName());
        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("org", name);
                        map.put("task", ac.getName());
                        map.put("myNum", response.body().getValue());
                        map.put("current", 0);
                        map.put("availed", System.currentTimeMillis());
                        FirebaseDatabase.getInstance().getReference()
                                .child("USERS")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("tokens")
                                .child(id)
                                .push()
                                .setValue(map)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialog.stopDialog();
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Your token number is " + response.body().getValue() + "\nYou can check status on home screen", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }


}
