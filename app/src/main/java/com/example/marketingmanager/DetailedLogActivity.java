package com.example.marketingmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DetailedLogActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, RecyclerViewAdapterLog.DataShare {

    RecyclerViewAdapterLog.DataShare dataShare;

    private static final String TAG = "MainActivity";
    RecyclerViewAdapterLog adapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    CollectionReference CompanyReference;
    String companyName;
    CompanyDataFirestore data;
    TextView date;
    private List<LogData> storageCopy = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date.setText(DateFormat.getDateInstance().format(c.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_log);
        db = FirebaseFirestore.getInstance();
        CompanyReference = db.collection("Companies");
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate started");
        Intent in = getIntent();
        companyName = in.getStringExtra("companyName");
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        CompanyReference.document(companyName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            data = task.getResult().toObject(CompanyDataFirestore.class);
                            storageCopy = data.getLogs();
                            initRecyclerView();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void AddLog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedLogActivity.this);
        View view1 = LayoutInflater.from(DetailedLogActivity.this).inflate(R.layout.addlogpopup_layout, null);
        final EditText whatHappened = view1.findViewById(R.id.whatHappenedEdit);
        final Switch proposalSent = view1.findViewById(R.id.proposalSentSwitch);
        date = view1.findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        date.setText(DateFormat.getDateInstance().format(c.getTime()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        builder.setView(view1).setPositiveButton("Save Log", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (whatHappened.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "What happened cant be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Date input needed", Toast.LENGTH_SHORT).show();
                    return;
                }
                LogData newData = new LogData(date.getText().toString(), whatHappened.getText().toString(), proposalSent.isChecked());
                if (proposalSent.isChecked()) data.setProposalSent(true);
                data.getLogs().add(0, newData);
                CompanyReference.document(companyName).set(data);
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();

    }

    private void initRecyclerView() {
        Log.d(TAG, "initialising Recycler View");
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapterLog(this, storageCopy);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void setLog(int myData) {
        data.getLogs().remove(myData);
        CompanyReference.document(companyName).set(data);
    }
}
