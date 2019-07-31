package com.example.marketingmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements SearchView.OnQueryTextListener, AddCompanyBottomSheetDialog.BottomSheetListener {

    private static final String TAG = "MainActivity";
    SearchView searchBox;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;
    CollectionReference User;
    DocumentReference Doc;
    UserDataFirestore data;
    private List<UserDataFirestoreCompany> storageCopy = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        User = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();
        searchBox = findViewById(R.id.searchCompany);
        searchBox.setOnQueryTextListener(this);
        searchBox.setIconifiedByDefault(false);
        Log.d(TAG, "onCreate started");
        onStart();
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
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
        if (s.isEmpty()) {
            adapter.updateList(storageCopy);
            return true;
        }
        String userInput = s.toLowerCase();
        List<UserDataFirestoreCompany> dataNames = new ArrayList<>();
        for (UserDataFirestoreCompany f : storageCopy) {
            if (f.getCompanyName().toLowerCase().contains(userInput))
                dataNames.add(f);
        }
        adapter.updateList(dataNames);
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
                if (c.getCompanyName().toLowerCase().equals(companyName.toLowerCase())) {
                    Toast.makeText(this, "Company Name Already Exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            UserDataFirestoreCompany newData = new UserDataFirestoreCompany(companyName, subTeam);
            data.getCompanies().add(newData);
            User.document(mAuth.getUid()).set(data);
            adapter.updateList(storageCopy);
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(this, "Enter Valid Name.", Toast.LENGTH_SHORT).show();
    }
}
