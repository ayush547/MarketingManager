package com.example.marketingmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterContactDetails extends RecyclerView.Adapter<RecyclerViewAdapterContactDetails.ViewHolder> {

    private List<ContactDetails> dataNames;
    private Context mContext;
    LogShare logShare;
    String contactString;

    public RecyclerViewAdapterContactDetails(Context mContext, List<ContactDetails> dataNames) {
        this.dataNames = dataNames;
        this.mContext = mContext;
        logShare = (LogShare) mContext;
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
        contactString = "Name - " + dataNames.get(i).getNameOfPOC();
        viewHolder.nameOfPOC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_delete, 0, 0, 0);
        viewHolder.nameOfPOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to remove selected record?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dataNames.remove(i);
                                notifyDataSetChanged();
                                logShare.setLog();
                            }
                        });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        if (dataNames.get(i).getDesignation().isEmpty())
            viewHolder.designation.setVisibility(View.GONE);
        else {
            viewHolder.designation.setText(dataNames.get(i).getDesignation());
            contactString += "\nDesignation - " + dataNames.get(i).getDesignation();
        }
        if (dataNames.get(i).getContactNo().isEmpty())
            viewHolder.designation.setVisibility(View.GONE);
        else {
            viewHolder.contactNo.setText(dataNames.get(i).getContactNo());
            contactString += "\nNumber - " + dataNames.get(i).getContactNo();
            viewHolder.contactNo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_call, 0, 0, 0);
            viewHolder.contactNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + viewHolder.contactNo.getText().toString()));
                    mContext.startActivity(callIntent);
                }
            });
        }
        if (!dataNames.get(i).getEmail().isEmpty())
            contactString += "\nEmail - " + dataNames.get(i).getEmail();
        viewHolder.email.setText(dataNames.get(i).getEmail());
        viewHolder.email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_share, 0, 0, 0);
        viewHolder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, contactString);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacts");
                mContext.startActivity(Intent.createChooser(shareIntent, "Share Contact Details"));
            }
        });
    }

    public void updateList(List<ContactDetails> dataNames) {
        this.dataNames = dataNames;
        notifyDataSetChanged();
    }

    public interface LogShare {
        void setLog();
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
