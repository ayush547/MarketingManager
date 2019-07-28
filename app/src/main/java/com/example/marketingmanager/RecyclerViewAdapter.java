package com.example.marketingmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Company> dataNames;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<Company> dataNames) {
        this.dataNames = dataNames;
        this.mContext = mContext;
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d("RecyclerViewAdapter", "onBind Called.");
        if (dataNames.get(i).isSubTeam()) {
            viewHolder.initials.setBackground(ContextCompat.getDrawable(mContext, R.drawable.circular_subteam));
        }
        viewHolder.initials.setText("" + dataNames.get(i).getName().charAt(0));
        viewHolder.names.setText(dataNames.get(i).getName());
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outToForce = new Intent(mContext, DetailedLogActivity.class);
                //send company identification to intent
                mContext.startActivity(outToForce);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataNames.size();
    }

    public void updateList(List<Company> dataNames) {
        this.dataNames = dataNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView initials, names;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initials = itemView.findViewById(R.id.initials);
            names = itemView.findViewById(R.id.names);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
