package com.example.queu_e.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queu_e.R;
import com.example.queu_e.model.TokenDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class TokensAdapter extends RecyclerView.Adapter<TokensAdapter.CustomVH> {

    private Context context;
    private List<TokenDetails> tokens;
    public Set<Integer> longPressed = new HashSet<>();

    public TokensAdapter(Context context, List<TokenDetails> tokens) {
        this.context = context;
        this.tokens = tokens;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tokens_view, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.org.setText(tokens.get(position).org);
        holder.task.setText(tokens.get(position).task);
        holder.date.setText("Received On: " + getDate(tokens.get(position).availed));
        holder.myNum.setText(tokens.get(position).myNum + "");
        holder.current.setText(tokens.get(position).current + "");
        if (longPressed.contains(position)) {
            holder.dlt.setVisibility(View.VISIBLE);
            holder.linearLayout1.setVisibility(View.GONE);
            holder.linearLayout2.setVisibility(View.GONE);
        } else {
            holder.dlt.setVisibility(View.GONE);
            holder.linearLayout1.setVisibility(View.VISIBLE);
            holder.linearLayout2.setVisibility(View.VISIBLE);
        }
    }

    private String getDate(Long availed) {
        TimeZone zone = TimeZone.getDefault();
        Date date = new Date(availed);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy h:mm a", Locale.ENGLISH);
        sdf.setTimeZone(zone);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        TextView org, task, date, myNum, current;
        LinearLayout linearLayout1, linearLayout2, linearLayout;
        ImageButton dlt;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            org = itemView.findViewById(R.id.org_name);
            task = itemView.findViewById(R.id.task);
            date = itemView.findViewById(R.id.date);
            myNum = itemView.findViewById(R.id.my_num);
            current = itemView.findViewById(R.id.current);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            linearLayout1 = itemView.findViewById(R.id.my_ll);
            linearLayout2 = itemView.findViewById(R.id.status_ll);
            dlt = itemView.findViewById(R.id.dlt);
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    dlt.setVisibility(View.VISIBLE);
                    longPressed.add(getAdapterPosition());
                    return true;
                }
            });
            dlt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteToken(getAdapterPosition());
                }
            });
        }
    }

    private void deleteToken(int adapterPosition) {
        FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child(FirebaseAuth.getInstance().getUid())
                .child("tokens")
                .child(tokens.get(adapterPosition).orgId)
                .child(tokens.get(adapterPosition).key)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            tokens.remove(adapterPosition);
                            notifyDataSetChanged();
                        }
                    }
                });
    }
}
