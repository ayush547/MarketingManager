package com.example.marketingmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements RecyclerViewAdapter.DataShare, SearchView.OnQueryTextListener, AddCompanyBottomSheetDialog.BottomSheetListener {

    private static final String TAG = "MainActivity";
    SearchView searchBox;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    CollectionReference User, Company;
    UserDataFirestore data;
    ProgressBar progressBar;
    private List<UserDataFirestoreCompany> storageCopy = new ArrayList<>();
    private FirebaseFirestore db;
    ImageButton moreOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        User = db.collection("Users");
        Company = db.collection("Companies");
        mAuth = FirebaseAuth.getInstance();
        moreOptions = findViewById(R.id.moreOptions);
        searchBox = findViewById(R.id.searchCompany);
        searchBox.setOnQueryTextListener(this);
        searchBox.setIconifiedByDefault(false);
        Log.d(TAG, "onCreate started");
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose option")
                        .setMessage("What do you want to do? ")
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                signOut();
                            }
                        })
                        .setNegativeButton("Delete All Data!!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteData();
                            }
                        });
                builder.show();
            }
        });
        onStart();
    }

    private void signOut() {
        mAuth.signOut();
        Intent outToLogin = new Intent(this, LoginActivity.class);
        startActivity(outToLogin);
        finish();
    }

    private void deleteData() {
        User.whereEqualTo(UserDataFirestore.Key_ID, mAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                data = documentSnapshot.toObject(UserDataFirestore.class);
                                for (UserDataFirestoreCompany c : data.getCompanies())
                                    Company.document(c.getCompanyName()).delete();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        UserDataFirestore newUser = new UserDataFirestore(mAuth.getUid());
        User.document(mAuth.getUid()).set(newUser);
        signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        User.whereEqualTo(UserDataFirestore.Key_ID, mAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                data = documentSnapshot.toObject(UserDataFirestore.class);
                                storageCopy = data.getCompanies();
                                initRecyclerView();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }



    private void initRecyclerView() {
        Log.d(TAG, "initialising Recycler View");
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this, storageCopy);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (!s.isEmpty()) {
            String userInput = s.toLowerCase();
            List<UserDataFirestoreCompany> dataNames = new ArrayList<>();
            for (UserDataFirestoreCompany f : storageCopy) {
                if (f.getCompanyName().toLowerCase().contains(userInput))
                    dataNames.add(f);
            }
            adapter.updateList(dataNames);
            return true;
        }
        adapter.updateList(storageCopy);
        return true;
    }


    public void AddCompany(View view) {
        AddCompanyBottomSheetDialog bottomSheetDialog = new AddCompanyBottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "addCompanyBottomSheet");
    }

    @Override
    public void onButtonClicked(String companyName, Boolean subTeam) {
        if (!companyName.isEmpty()) {
            for (UserDataFirestoreCompany c : storageCopy) {
                if (c.getCompanyName().toLowerCase().equals(companyName.toLowerCase().trim())) {
                    Toast.makeText(this, "Company Name Already Exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            UserDataFirestoreCompany newData = new UserDataFirestoreCompany(companyName, subTeam);
            CompanyDataFirestore newCompanyData = new CompanyDataFirestore(newData.getCompanyName().trim(), newData.getSubTeam());
            Company.document(newData.getCompanyName()).set(newCompanyData);
            data.getCompanies().add(0, newData);
            User.document(mAuth.getUid()).set(data);
            adapter.updateList(storageCopy);
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(this, "Enter Valid Name.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLog() {
        User.document(mAuth.getUid()).set(data);
    }
}
