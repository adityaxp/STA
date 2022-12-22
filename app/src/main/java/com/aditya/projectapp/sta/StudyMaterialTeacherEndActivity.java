package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterialTeacherEndActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton imageFloatButton, pdfFloatButton, docxFloatButton;
    Intent intent;
    StudyMaterialAdapter studyMaterialAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    ArrayList<StudyMaterialData> studyMaterialList;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material_teacher_end);

        toolbar =  findViewById(R.id.studyToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudyMaterialTeacherEndActivity.this ,STAHomeActivity.class);
                intent.putExtra("userType", "Teacher");
                startActivity(intent);
            }
        });

        intent  = getIntent();
        final String groupName = intent.getStringExtra("groupName");
      //  Toast.makeText(this, groupName, Toast.LENGTH_SHORT).show();

        imageFloatButton = (FloatingActionButton) findViewById(R.id.imageButton);
        pdfFloatButton = (FloatingActionButton)  findViewById(R.id.pdfButton);
        docxFloatButton = (FloatingActionButton) findViewById(R.id.docxButton);



        studyMaterialList = new ArrayList<>();
        RecyclerView studyMaterialRecyclerView = (RecyclerView) findViewById(R.id.studyMaterialListRecyclerView);

        customLoadingDialog = new CustomLoadingDialog(this);

        layoutManager = new LinearLayoutManager(this);
        studyMaterialAdapter = new StudyMaterialAdapter(this, studyMaterialList);
        studyMaterialRecyclerView.setLayoutManager(layoutManager);
        studyMaterialRecyclerView.setAdapter(studyMaterialAdapter);



        imageFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyMaterialTeacherEndActivity.this, UploadStudyMaterialActivity.class);
                intent.putExtra("fileType", "Image");
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });

        pdfFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyMaterialTeacherEndActivity.this, UploadStudyMaterialActivity.class);
                intent.putExtra("fileType", "Pdf");
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });

        docxFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StudyMaterialTeacherEndActivity.this)
                        .setMessage("STA does not have support for files with extension docx at this moment please tune in for next updates")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

            }
        });
       // Toast.makeText(this, "Teacher End", Toast.LENGTH_SHORT).show();


        databaseReference = FirebaseDatabase.getInstance().getReference("StudyMaterial");
        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studyMaterialList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if (itemSnapshot.child("groupName").getValue().toString().equals(groupName)){
                        StudyMaterialData studyMaterialData = itemSnapshot.getValue(StudyMaterialData.class);
                        studyMaterialList.add(studyMaterialData);
                    }
                }
                studyMaterialAdapter.notifyDataSetChanged();
                customLoadingDialog.customLoaingDialogDismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customLoadingDialog.customLoaingDialogDismiss();
                Toast.makeText(StudyMaterialTeacherEndActivity.this , "Failed to retrieve group list" + error, Toast.LENGTH_SHORT).show();

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
                ArrayList<StudyMaterialData> filterList = new ArrayList<>();

                for(StudyMaterialData item: studyMaterialList){
                    if(item.getStudyMaterialTitle().toLowerCase().contains(newText.toLowerCase()) || item.getStudyMaterialDescription().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(item);
                    }
                }
                studyMaterialAdapter.filteredList(filterList);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudyMaterialTeacherEndActivity.this ,STAHomeActivity.class);
        intent.putExtra("userType", "Teacher");
        startActivity(intent);
    }
}