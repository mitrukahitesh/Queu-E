package com.example.queu_e.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.queu_e.ActivityActivity;
import com.example.queu_e.OrganizationActivity;
import com.example.queu_e.R;
import com.example.queu_e.model.Org;
import com.example.queu_e.model.OrganizationResponse;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

public class OrgAdapter extends RecyclerView.Adapter<OrgAdapter.CustomVH> {

    private Context context;

    private List<Org> orgs;

    public static Org org;

    public OrgAdapter(Context context, List<Org> orgs) {
        this.context = context;
        this.orgs = orgs;
    }


    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.org_view, parent, false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, int position) {
        holder.name.setText(orgs.get(position).getName());
        holder.type.setText(orgs.get(position).getType());
        holder.email.setText(String.format("Email: %s", orgs.get(position).getEmail()));
    }

    @Override
    public int getItemCount() {
        return orgs.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        TextView name, type, email;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            email = itemView.findViewById(R.id.email);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqTokenActivity(getAdapterPosition());
                }
            });
            type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqTokenActivity(getAdapterPosition());
                }
            });
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqTokenActivity(getAdapterPosition());
                }
            });
        }
    }

    private void reqTokenActivity(int adapterPosition) {
        Intent intent = new Intent(context, ActivityActivity.class);
        org = orgs.get(adapterPosition);
        context.startActivity(intent);
    }
}
