package com.example.marketingmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements SearchView.OnQueryTextListener, AddCompanyBottomSheetDialog.BottomSheetListener {

    private static final String TAG = "MainActivity";
    SearchView searchBox;
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    private List<Company> dataNames = new ArrayList<>();
    private List<Company> storageCopy = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBox = findViewById(R.id.searchCompany);
        searchBox.setOnQueryTextListener(this);
        searchBox.setIconifiedByDefault(false);
        Log.d(TAG, "onCreate started");
        initData();
    }

    private void initData() {
        //initialise data, needs work here storage copy
        //fitData()
        dataNames.addAll(storageCopy);
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initialising Recycler View");
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(this, dataNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userInput = s.toLowerCase();
        dataNames.clear();
        for (Company f : storageCopy) {
            if (f.getName().toLowerCase().contains(userInput))
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
            for (Company c : dataNames) {
                if (c.getName().toLowerCase().equals(companyName.toLowerCase())) {
                    Toast.makeText(this, "Company Name Already Exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Company newData = new Company(companyName, subTeam);
            dataNames.add(newData);
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(this, "Enter Valid Name.", Toast.LENGTH_SHORT).show();
    }
}
