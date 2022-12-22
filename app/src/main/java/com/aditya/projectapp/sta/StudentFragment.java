package com.aditya.projectapp.sta;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * * create an instance of this fragment.
 */
public class StudentFragment extends androidx.fragment.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    GroupAdapter groupAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference, memberDatabaseReference;
    ArrayList<GroupData> studentGroupList;
    ValueEventListener valueEventListener;
    CustomLoadingDialog customLoadingDialog;
    SearchView searchView;

    public StudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student, container, false);

        setHasOptionsMenu(true);

        studentGroupList = new ArrayList<>();
        RecyclerView studentGroupListRecylerView = (RecyclerView) view.findViewById(R.id.studentGroupListRecylerView);

        ImageButton joinGroupButton = (ImageButton) view.findViewById(R.id.joinGroupButton);
        ImageButton studymaterialButton = (ImageButton) view.findViewById(R.id.studymaterialButton);
        ImageButton assignmentButton = (ImageButton) view.findViewById(R.id.assignmentButton);
        ImageButton scanButton = (ImageButton) view.findViewById(R.id.scanButton);

        customLoadingDialog = new CustomLoadingDialog(getActivity());

        layoutManager = new LinearLayoutManager(getActivity());
        groupAdapter = new GroupAdapter(getActivity(), studentGroupList);
        studentGroupListRecylerView.setLayoutManager(layoutManager);
        studentGroupListRecylerView.setAdapter(groupAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        customLoadingDialog.customLoadingDialogShow();
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                studentGroupList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if(itemSnapshot.child("groupMembers").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        GroupData groupData = itemSnapshot.getValue(GroupData.class);
                        studentGroupList.add(groupData);
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

        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup();
            }
        });

        studymaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGroupForStudyMaterial.class);
                intent.putExtra("userType", "Student");
                startActivity(intent);
            }
        });

        assignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGroupForAssignment.class);
                intent.putExtra("userType", "Student");
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

    void joinGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog;

        View view = getLayoutInflater().inflate(R.layout.join_group_alert_dialog_box, null);
        Button joinButton = (Button) view.findViewById(R.id.joinButton);
        Button closeButton = (Button) view.findViewById(R.id.closeButton);
        final EditText joinGroupKeyEditText = (EditText) view.findViewById(R.id.joinGroupKeyEditText);


        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(joinGroupKeyEditText.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Please enter group key", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
                    valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                                if(itemSnapshot.child("groupKey").getValue().toString().equals(joinGroupKeyEditText.getText().toString())){
                                    memberDatabaseReference = FirebaseDatabase.getInstance().getReference("Groups");
                                    memberDatabaseReference.child(joinGroupKeyEditText.getText().toString()).child("groupMembers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("member");
                                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(getActivity(), "Group dosen't exist", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.student_group_search, menu);

        MenuItem searchItem = menu.findItem(R.id.group_search);
        searchView = (SearchView) searchItem.getActionView();

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

                for(GroupData item: studentGroupList){
                    if(item.getGroupName().toLowerCase().contains(newText.toLowerCase()) || item.getRecentMessage().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(item);
                    }
                }
                groupAdapter.filteredList(filterList);
                return true;
            }
        });

    }
}