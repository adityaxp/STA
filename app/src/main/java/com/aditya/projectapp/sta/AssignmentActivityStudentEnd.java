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

public class AssignmentActivityStudentEnd extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;
    AssignmentAdapterStudentEnd assignmentAdapterStudentEnd;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    ArrayList<AssignmentData> assignmentArrayList;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_student_end);

        toolbar = findViewById(R.id.assignmentStudentEndToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        assignmentArrayList = new ArrayList<>();
        RecyclerView assignmentRecyclerView = (RecyclerView) findViewById(R.id.assignmentRecyclerView);

        customLoadingDialog = new CustomLoadingDialog(this);

        intent  = getIntent();
        final String groupName = intent.getStringExtra("groupName");


        layoutManager = new LinearLayoutManager(this);
        assignmentAdapterStudentEnd = new AssignmentAdapterStudentEnd(this, assignmentArrayList);
        assignmentRecyclerView.setLayoutManager(layoutManager);
        assignmentRecyclerView.setAdapter(assignmentAdapterStudentEnd);

        databaseReference = FirebaseDatabase.getInstance().getReference("Assignments");
        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assignmentArrayList.clear();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    if (itemSnapshot.child("groupName").getValue().toString().equals(groupName)) {
                        AssignmentData assignmentData = itemSnapshot.getValue(AssignmentData.class);
                        assignmentArrayList.add(assignmentData);
                    }
                }
                assignmentAdapterStudentEnd.notifyDataSetChanged();
                customLoadingDialog.customLoaingDialogDismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customLoadingDialog.customLoaingDialogDismiss();
                Toast.makeText(AssignmentActivityStudentEnd.this, "Failed to retrieve group list" + error, Toast.LENGTH_SHORT).show();

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
                ArrayList<AssignmentData> filterList = new ArrayList<>();

                for (AssignmentData item : assignmentArrayList) {
                    if (item.getAssignmentTitle().toLowerCase().contains(newText.toLowerCase()) || item.getAssignmentDescription().toLowerCase().contains(newText.toLowerCase())) {
                        filterList.add(item);
                    }
                }
                assignmentAdapterStudentEnd.filteredList(filterList);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AssignmentActivityStudentEnd.this, STAHomeActivity.class);
        intent.putExtra("userType", "Student");
        startActivity(intent);
    }
}