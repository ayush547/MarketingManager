package com.example.marketingmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<UserDataFirestoreCompany> dataNames;
    private Context mContext;
    DataShare dataShare;

    public RecyclerViewAdapter(Context mContext, List<UserDataFirestoreCompany> dataNames) {
        this.dataNames = dataNames;
        this.mContext = mContext;
        dataShare = (DataShare) mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d("RecyclerViewAdapter", "onBind Called.");
        if (dataNames.get(i).getSubTeam()) {
            viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_subteam));
        }
        viewHolder.initials.setText("" + dataNames.get(i).getCompanyName().charAt(0));
        if (!dataNames.get(i).getActive()) {
            viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_unactive));
        } else {
            if (dataNames.get(i).getSubTeam()) {
                viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_subteam));
            } else
                viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular));
        }
        viewHolder.activeSwitch.setChecked(dataNames.get(i).getActive());
        viewHolder.activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataNames.get(i).setActive(isChecked);
                if (!isChecked) {
                    viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_unactive));
                } else {
                    if (dataNames.get(i).getSubTeam()) {
                        viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_subteam));
                    } else
                        viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular));
                }
                dataShare.setLog();
            }
        });
        viewHolder.names.setText(dataNames.get(i).getCompanyName());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outToForce = new Intent(mContext, DetailedLogActivity.class);
                outToForce.putExtra("companyName", dataNames.get(i).getCompanyName());
                mContext.startActivity(outToForce);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataNames.size();
    }

    public void updateList(List<UserDataFirestoreCompany> dataNames) {
        this.dataNames = dataNames;
        notifyDataSetChanged();
    }

    public interface DataShare {
        void setLog();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView initials, names;
        RelativeLayout parentLayout;
        Switch activeSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initials = itemView.findViewById(R.id.initials);
            names = itemView.findViewById(R.id.names);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            activeSwitch = itemView.findViewById(R.id.activeSwitch);
        }
    }
}
