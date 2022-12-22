package com.aditya.projectapp.sta;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class TeacherFragment extends androidx.fragment.app.Fragment {

 //   GroupData groupData;
    GroupAdapter groupAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference, databaseReferenceFilter;
    ArrayList<GroupData> groupList;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;


    public TeacherFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        setHasOptionsMenu(true);

        groupList = new ArrayList<>();
        RecyclerView groupListRecylerView = (RecyclerView) view.findViewById(R.id.groupListRecylerView);
        layoutManager = new LinearLayoutManager(getActivity());

        ImageButton createGroupButton = (ImageButton) view.findViewById(R.id.createGroupButton);
        ImageButton studymaterialButton = (ImageButton) view.findViewById(R.id.teacherStudymaterialButton);
        ImageButton assignmentButton = (ImageButton) view.findViewById(R.id.teacherAssignmentButton);
        ImageButton scanButton = (ImageButton) view.findViewById(R.id.teacherScanButton);

        // Group cardView test - successful  - 03/10/2020
        // groupData = new GroupData(R.drawable.noimage, "hahhaha", "hahhahah");
        //  groupList.add(groupData);

        customLoadingDialog = new CustomLoadingDialog(getActivity());
        groupAdapter = new GroupAdapter(getActivity(), groupList);
        groupListRecylerView.setLayoutManager(layoutManager);
        groupListRecylerView.setAdapter(groupAdapter);

        databaseReferenceFilter = FirebaseDatabase.getInstance().getReference("Groups");
        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReferenceFilter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                groupList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if (itemSnapshot.child("adminEmail").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        GroupData groupData = itemSnapshot.getValue(GroupData.class);
                        groupList.add(groupData);
                    }
                }
                groupAdapter.notifyDataSetChanged();
                customLoadingDialog.customLoaingDialogDismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                customLoadingDialog.customLoaingDialogDismiss();

            }
        });



        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                        intent.putExtra("userName", snapshot.child("userName").getValue().toString());
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Failed! Please check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        studymaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGroupForStudyMaterial.class);
                intent.putExtra("userType", "teacher");
                startActivity(intent);
            }
        });

        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGroupForAssignment.class);
                intent.putExtra("userType", "teacher");
                startActivity(intent);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(), STAQuickScanActivity.class);
              startActivity(intent);
            }
        });


        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.group_search, menu);

        MenuItem searchItem = menu.findItem(R.id.group_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<GroupData> filterList = new ArrayList<>();

                for(GroupData item: groupList){
                    if(item.getGroupName().toLowerCase().contains(newText.toLowerCase()) || item.getRecentMessage().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(item);
                    }
                }
                groupAdapter.filteredList(filterList);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.show_all) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
            customLoadingDialog.customLoadingDialogShow();
            valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    groupList.clear();
                    for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                        GroupData groupData = itemSnapshot.getValue(GroupData.class);
                        groupList.add(groupData);
                    }
                    groupAdapter.notifyDataSetChanged();
                    customLoadingDialog.customLoaingDialogDismiss();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    customLoadingDialog.customLoaingDialogDismiss();

                }
            });
            return true;
        }

        if(item.getItemId() == R.id.created_by_you){
            databaseReferenceFilter = FirebaseDatabase.getInstance().getReference("Groups");
            customLoadingDialog.customLoadingDialogShow();
            valueEventListener = databaseReferenceFilter.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    groupList.clear();
                    for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                        if (itemSnapshot.child("adminEmail").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            GroupData groupData = itemSnapshot.getValue(GroupData.class);
                            groupList.add(groupData);
                        }
                    }
                    groupAdapter.notifyDataSetChanged();
                    customLoadingDialog.customLoaingDialogDismiss();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    customLoadingDialog.customLoaingDialogDismiss();

                }
            });
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
}



