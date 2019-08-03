package com.example.marketingmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailedLogActivity extends FragmentActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, RecyclerViewAdapterLog.DataShare, RecyclerViewAdapterContactDetails.LogShare {

    private static final String TAG = "MainActivity";
    RecyclerViewAdapterLog adapter;
    RecyclerViewAdapterContactDetails adapterContactDetails;
    RecyclerView recyclerView, recyclerViewContact;
    FirebaseAuth mAuth;
    CollectionReference CompanyReference;
    String companyName;
    CompanyDataFirestore data;
    Switch proposal;
    ProgressBar progressBar;
    TextView date, time;
    private List<LogData> storageCopy = new ArrayList<>();
    TextView companyNameTextView, psSentOn;
    private FirebaseFirestore db;
    Calendar z;
    private List<ContactDetails> storageCopyContacts = new ArrayList<>();

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Calendar c = Calendar.getInstance();
        z.set(Calendar.YEAR, year);
        z.set(Calendar.MONTH, month);
        z.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date.setText(DateFormat.getDateInstance().format(z.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //Calendar c = Calendar.getInstance();
        z.set(Calendar.HOUR_OF_DAY, hourOfDay);
        z.set(Calendar.MINUTE, minute);
        time.setText(DateFormat.getTimeInstance().format(z.getTime()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_log);
        companyNameTextView = findViewById(R.id.companyName);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        proposal = findViewById(R.id.proposalSentStatus);
        psSentOn = findViewById(R.id.psSentOn);
        CompanyReference = db.collection("Companies");
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate started");
        Intent in = getIntent();
        companyName = in.getStringExtra("companyName");
        companyNameTextView.setText(companyName);
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
                            storageCopyContacts = data.getContacts();
                            proposal.setChecked(data.proposalSent);
                            initRecyclerView();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }

    public void AddLog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedLogActivity.this);
        builder.setTitle("Choose option")
                .setMessage("What do you want to add? ")
                .setPositiveButton("Log Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addLog();
                    }
                })
                .setNegativeButton("Contact", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addContact();
                    }
                }).setNeutralButton("Reminder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addReminder();
            }
        });
        builder.show();
    }

    private void addReminder() {
        //add reminder feature
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedLogActivity.this);
        final View view1 = LayoutInflater.from(DetailedLogActivity.this).inflate(R.layout.addreminderpopup_layout, null);
        final EditText reminderEdit = view1.findViewById(R.id.reminderEdit);
        reminderEdit.setText("Call " + companyName);
        date = view1.findViewById(R.id.date);
        time = view1.findViewById(R.id.time);
        final Calendar c = Calendar.getInstance();
        z = Calendar.getInstance();
        date.setText(DateFormat.getDateInstance().format(c.getTime()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });
        time.setText(DateFormat.getTimeInstance().format(c.getTime()));
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time Picker");
            }
        });
        builder.setView(view1).setPositiveButton("Save Reminder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.Reminders.TITLE, "Marketing Manager Reminder");
                intent.putExtra(CalendarContract.Reminders.DESCRIPTION, reminderEdit.getText().toString());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, z.getTimeInMillis() + 3600 * 1000);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, z.getTimeInMillis());
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void addLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedLogActivity.this);
        View view1 = LayoutInflater.from(DetailedLogActivity.this).inflate(R.layout.addlogpopup_layout, null);
        final EditText whatHappened = view1.findViewById(R.id.whatHappenedEdit);
        final Switch proposalSent = view1.findViewById(R.id.proposalSentSwitch);
        date = view1.findViewById(R.id.date);
        z = Calendar.getInstance();
        date.setText(DateFormat.getDateInstance().format(z.getTime()));
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
                LogData newData = new LogData(date.getText().toString(), whatHappened.getText().toString(), proposalSent.isChecked());
                if (proposalSent.isChecked()) {
                    data.setProposalSent(true);
                    proposal.setChecked(data.proposalSent);
                    psSentOn.setText(newData.getDate());

                }
                data.getLogs().add(0, newData);
                CompanyReference.document(companyName).set(data);
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void addContact() {
        //work here to add contact details
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailedLogActivity.this);
        View view1 = LayoutInflater.from(DetailedLogActivity.this).inflate(R.layout.addcontactpopup_layout, null);
        final EditText nameOfPOC = view1.findViewById(R.id.nameOfPOC), designation = view1.findViewById(R.id.designation),
                contactNo = view1.findViewById(R.id.contactNo), email = view1.findViewById(R.id.email);
        builder.setView(view1)
                .setTitle("Enter Contact Details")
                .setPositiveButton("Add Contact", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String POCs = nameOfPOC.getText().toString(), designations = designation.getText().toString(),
                                contacts = contactNo.getText().toString(), emails = email.getText().toString();
                        if (POCs.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "POC name cant be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (contacts.length() != 0 && contacts.length() < 10) {
                            Toast.makeText(getApplicationContext(), "Contact No cant be less that 10 digits", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!emails.isEmpty() && !isEmailValid(emails)) {
                            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ContactDetails newData = new ContactDetails(POCs, designations, contacts, emails);
                        data.getContacts().add(0, newData);
                        CompanyReference.document(companyName).set(data);
                        adapterContactDetails.notifyDataSetChanged();
                        recyclerViewContact.getLayoutManager().scrollToPosition(0);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();

    }

    private void initRecyclerView() {
        Log.d(TAG, "initialising Recycler View");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewContact = findViewById(R.id.recyclerViewContact);
        adapter = new RecyclerViewAdapterLog(DetailedLogActivity.this, storageCopy);
        adapterContactDetails = new RecyclerViewAdapterContactDetails(DetailedLogActivity.this, storageCopyContacts);
        recyclerView.setAdapter(adapter);
        recyclerViewContact.setAdapter(adapterContactDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedLogActivity.this));
        recyclerViewContact.setLayoutManager(new LinearLayoutManager(DetailedLogActivity.this, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewContact.getLayoutManager().scrollToPosition(data.getContacts().size() - 1);
    }

    @Override
    public void setLog() {
        CompanyReference.document(companyName).set(data);
    }

}
