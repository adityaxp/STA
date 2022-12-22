package com.aditya.projectapp.sta;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChatFragment extends androidx.fragment.app.Fragment {

    //TeacherData teacherData;
    TeacherAdapter teacherAdapter;
    RecyclerView.LayoutManager techerLayoutManager, studentLayoutManager;
    ArrayList<TeacherData> teacherList;
    DatabaseReference databaseReference, databaseReference1;
    ValueEventListener valueEventListener;
    String userName;

    //StudentData studentData;
    StudentAdapter studentAdapter;
    ArrayList<StudentData> studentList;




    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        setHasOptionsMenu(true);

        teacherList = new ArrayList<>();
        studentList = new ArrayList<>();

        RecyclerView techerListRecycleView = (RecyclerView) view.findViewById(R.id.techerListRecycleView);
        RecyclerView studentListRecycleView = (RecyclerView) view.findViewById(R.id.studentListRecycleView);

        techerLayoutManager = new LinearLayoutManager(getActivity());
        studentLayoutManager = new LinearLayoutManager(getActivity());


        // Student-teacher cardView test - successful  - 05/10/2020
//        teacherData = new TeacherData("One", R.drawable.noimage);
//        teacherList.add(teacherData);
//        studentData = new StudentData("Six", R.drawable.noimage);
//        studentList.add(studentData);


        databaseReference1 = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("userName").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        teacherAdapter = new TeacherAdapter(getActivity(), teacherList);
        techerListRecycleView.setLayoutManager(techerLayoutManager);
        techerListRecycleView.setAdapter(teacherAdapter);

        studentAdapter = new StudentAdapter(getActivity(), studentList);
        studentListRecycleView.setLayoutManager(studentLayoutManager);
        studentListRecycleView.setAdapter(studentAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teacherList.clear();
                studentList.clear();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if (itemSnapshot.child("userType").getValue().toString().equals("Teacher") && !itemSnapshot.child("userName").getValue().toString().equals(userName)) {
                        TeacherData teacherData = itemSnapshot.getValue(TeacherData.class);
                        teacherList.add(teacherData);
                    }else if(itemSnapshot.child("userType").getValue().toString().equals("Student") && !itemSnapshot.child("userName").getValue().toString().equals(userName)){
                        StudentData studentData = itemSnapshot.getValue(StudentData.class);
                        studentList.add(studentData);
                    }
                }
                teacherAdapter.notifyDataSetChanged();
                studentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chat_search, menu);

        MenuItem searchItem = menu.findItem(R.id.chat_search);
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
                ArrayList<StudentData> filterList1 = new ArrayList<>();
                ArrayList<TeacherData> filterList2 = new ArrayList<>();

                for(StudentData item: studentList){
                    if(item.getUserName().toLowerCase().contains(newText.toLowerCase())){
                        filterList1.add(item);
                    }
                }

                for(TeacherData item: teacherList){
                    if(item.getUserName().toLowerCase().contains(newText.toLowerCase())){
                        filterList2.add(item);
                    }
                }

                studentAdapter.filteredList(filterList1);
                teacherAdapter.filteredList(filterList2);

                return true;
            }
        });

    }

}