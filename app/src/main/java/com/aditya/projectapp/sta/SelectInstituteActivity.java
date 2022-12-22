package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectInstituteActivity extends AppCompatActivity {

    //String userType;
    RecyclerView instituteListRecylerView;
    List<InstituteData> instituteDataList;
//    String instituteDetail;
//    String instituteTitle;
//    ImageView imageView;
    EditText intituteSearchEditText;
    InstituteAdapter instituteAdapter;
    Button helpButton;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;



    @Override
    public void onBackPressed() {
        CustomAlertDialogBox  customAlertDialogBox = new CustomAlertDialogBox(this);
        customAlertDialogBox.CustomAlertDialogBoxShow("Please select an institute to continue...");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_institute);

        //Intent intent = getIntent();
        // userType = intent.getStringExtra("usertype");

         final CustomAlertDialogBox customAlertDialogBox = new CustomAlertDialogBox(this);
        instituteListRecylerView = (RecyclerView) findViewById(R.id.instituteListRecylerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SelectInstituteActivity.this, 1);
        instituteListRecylerView.setLayoutManager(gridLayoutManager);
        intituteSearchEditText = (EditText) findViewById(R.id.intituteSearchEditText);
        customLoadingDialog = new CustomLoadingDialog(this);
        instituteDataList = new ArrayList<>();


        intituteSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        instituteAdapter = new InstituteAdapter(this, (ArrayList<InstituteData>) instituteDataList);
        instituteListRecylerView.setAdapter(instituteAdapter);



        helpButton = (Button) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instituteAdapter.notifyDataSetChanged();
                customAlertDialogBox.CustomAlertDialogBoxShow();
            }
        });

//        CardView test - success (Appcompat - support library 26)
//        CardView test - success (AndroidX - material design) attempt - 6
//        instituteData = new InstituteData("Null", "null", R.drawable.noimage);
//        instituteDataList.add(instituteData);



        databaseReference = FirebaseDatabase.getInstance().getReference("Institute");

        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                instituteDataList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    InstituteData instituteData = itemSnapshot.getValue(InstituteData.class);
                    instituteDataList.add(instituteData);
                }
                instituteAdapter.notifyDataSetChanged();
                customLoadingDialog.customLoaingDialogDismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customLoadingDialog.customLoaingDialogDismiss();

            }
        });

    }

    private void filter(String searchText) {

        ArrayList<InstituteData> filterList = new ArrayList<>();

        for(InstituteData item: instituteDataList){
            if(item.getItemName().toLowerCase().contains(searchText.toLowerCase())){
                filterList.add(item);
            }
        }
        instituteAdapter.filteredList(filterList);

    }

}
