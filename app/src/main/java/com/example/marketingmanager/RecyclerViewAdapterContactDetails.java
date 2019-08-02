package com.example.marketingmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterContactDetails extends RecyclerView.Adapter<RecyclerViewAdapterContactDetails.ViewHolder> {

    private List<ContactDetails> dataNames;
    private Context mContext;

    public RecyclerViewAdapterContactDetails(Context mContext, List<ContactDetails> dataNames) {
        this.dataNames = dataNames;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contactitem_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return dataNames.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d("RecyclerViewAdapter", "onBind Called.");
        viewHolder.nameOfPOC.setText("" + dataNames.get(i).getNameOfPOC());
        if (dataNames.get(i).getDesignation().isEmpty())
            viewHolder.designation.setVisibility(View.GONE);
        else viewHolder.designation.setText(dataNames.get(i).getDesignation());
        if (dataNames.get(i).getEmail().isEmpty()) viewHolder.designation.setVisibility(View.GONE);
        else {
            viewHolder.contactNo.setText(dataNames.get(i).getContactNo());
            viewHolder.contactNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + viewHolder.contactNo.getText().toString()));
                    mContext.startActivity(callIntent);
                }
            });
        }
        if (dataNames.get(i).getContactNo().isEmpty())
            viewHolder.designation.setVisibility(View.GONE);
        else viewHolder.email.setText(dataNames.get(i).getEmail());
    }

    public void updateList(List<ContactDetails> dataNames) {
        this.dataNames = dataNames;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameOfPOC, designation, contactNo, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfPOC = itemView.findViewById(R.id.nameOfPOC);
            designation = itemView.findViewById(R.id.designation);
            contactNo = itemView.findViewById(R.id.contactNo);
            email = itemView.findViewById(R.id.email);
        }
    }

}
