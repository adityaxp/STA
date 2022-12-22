package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectGroupForAssignment extends AppCompatActivity {

    SelectGroupAdapterForAssignments selectGroupAdapterForAssignments;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    ArrayList<GroupData> selectGroupList;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;
    SearchView searchView;
    Toolbar toolbar;
    Intent intent;
    String userType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group_for_assignment);


        toolbar =  findViewById(R.id.groupSelectToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        intent = getIntent();
        userType = intent.getStringExtra("userType");

        customLoadingDialog = new CustomLoadingDialog(this);

        selectGroupList = new ArrayList<>();
        RecyclerView groupListRecyclerView = (RecyclerView) findViewById(R.id.GroupListRecyclerView);


        layoutManager = new LinearLayoutManager(this);
        selectGroupAdapterForAssignments = new SelectGroupAdapterForAssignments(this, selectGroupList);
        groupListRecyclerView.setLayoutManager(layoutManager);
        groupListRecyclerView.setAdapter(selectGroupAdapterForAssignments);


        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                selectGroupList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if(userType.equals("teacher")){
                        if (itemSnapshot.child("adminEmail").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            GroupData groupData = itemSnapshot.getValue(GroupData.class);
                            selectGroupList.add(groupData);
                        }
                    }else{
                        if(itemSnapshot.child("groupMembers").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            GroupData groupData = itemSnapshot.getValue(GroupData.class);
                            selectGroupList.add(groupData);
                        }
                    }
                }
                selectGroupAdapterForAssignments.notifyDataSetChanged();
                customLoadingDialog.customLoaingDialogDismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customLoadingDialog.customLoaingDialogDismiss();
                Toast.makeText(SelectGroupForAssignment.this , "Failed to retrieve group list" + error, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.student_group_search, menu);

        MenuItem searchItem = menu.findItem(R.id.group_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search group...");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<GroupData> filterList = new ArrayList<>();

                for(GroupData item: selectGroupList){
                    if(item.getGroupName().toLowerCase().contains(newText.toLowerCase()) || item.getRecentMessage().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(item);
                    }
                }
                selectGroupAdapterForAssignments.filteredList(filterList);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SelectGroupForAssignment.this ,STAHomeActivity.class);
        intent.putExtra("userType", userType);
        startActivity(intent);
    }
}