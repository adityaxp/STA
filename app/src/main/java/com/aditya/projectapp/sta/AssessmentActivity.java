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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssessmentActivity extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;
    AssessmentAdapter assessmentAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    ArrayList<StudentAssignmentData> studentAssignmentDataArrayList;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        toolbar =  findViewById(R.id.assessmentToolBar);
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
        //Toast.makeText(this, intent.getStringExtra("assignmentTitle") + " " + intent.getStringExtra("groupName"), Toast.LENGTH_SHORT).show();


        studentAssignmentDataArrayList = new ArrayList<>();
        RecyclerView assessmentRecyclerView = (RecyclerView) findViewById(R.id.assessmentRecyclerView);

        customLoadingDialog = new CustomLoadingDialog(this);

        layoutManager = new LinearLayoutManager(this);
        assessmentAdapter = new AssessmentAdapter(this, studentAssignmentDataArrayList);
        assessmentRecyclerView.setLayoutManager(layoutManager);
        assessmentRecyclerView.setAdapter(assessmentAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("StudentAssignments").child(intent.getStringExtra("groupName")).child(intent.getStringExtra("assignmentTitle"));
        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentAssignmentDataArrayList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if (itemSnapshot.child("approvalStatus").getValue().toString().equals("In assessment")){
                        StudentAssignmentData studentAssignmentData = itemSnapshot.getValue(StudentAssignmentData.class);
                        studentAssignmentDataArrayList.add(studentAssignmentData);
                    }
                }
                assessmentAdapter.notifyDataSetChanged();
                customLoadingDialog.customLoaingDialogDismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customLoadingDialog.customLoaingDialogDismiss();
                Toast.makeText(AssessmentActivity.this , "Failed to retrieve group list" + error, Toast.LENGTH_SHORT).show();

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
                ArrayList<StudentAssignmentData> filterList = new ArrayList<>();

                for(StudentAssignmentData item: studentAssignmentDataArrayList){
                    if(item.getAssignmentTitle().toLowerCase().contains(newText.toLowerCase()) || item.getAssignmentDescription().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(item);
                    }
                }
                assessmentAdapter.filteredList(filterList);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }





}