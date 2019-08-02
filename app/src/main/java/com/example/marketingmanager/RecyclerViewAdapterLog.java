package com.example.marketingmanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterLog extends RecyclerView.Adapter<RecyclerViewAdapterLog.ViewHolder> {

    DataShare dataShare;

    public RecyclerViewAdapterLog(Context mContext, List<LogData> dataNames) {
        this.dataNames = dataNames;
        this.mContext = mContext;
        dataShare = (DataShare) mContext;
    }

    private List<LogData> dataNames;
    private Context mContext;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        //what makes the view special
        Log.d("RecyclerViewAdapterLog", "onBind Called.");
        viewHolder.date.setText(dataNames.get(i).getDate());
        viewHolder.whatHappened.setText(dataNames.get(i).getWhatHappened());
        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, dataNames.get(i).getWhatHappened() + " - Happened On " + dataNames.get(i).getDate());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Log");
                mContext.startActivity(Intent.createChooser(shareIntent, "Share Log"));
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
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
                                dataShare.setLog();
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
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.logitem_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public List<LogData> returnList() {
        return dataNames;
    }

    @Override
    public int getItemCount() {
        return dataNames.size();
    }

    public interface DataShare {
        void setLog();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, time, whatHappened;
        RelativeLayout parentLayout;
        ImageButton delete, share;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            whatHappened = itemView.findViewById(R.id.whatHappenedView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            delete = itemView.findViewById(R.id.deleteButton);
            share = itemView.findViewById(R.id.shareButton);
        }

    }
}
