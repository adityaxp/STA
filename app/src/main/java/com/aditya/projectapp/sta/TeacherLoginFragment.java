package com.aditya.projectapp.sta;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherLoginFragment extends androidx.fragment.app.Fragment {

    private FirebaseAuth firebaseAuth;
    String loginUserType1 = "Teacher";

    public TeacherLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_login, container, false);
        TextView signUpTextView = (TextView) view.findViewById(R.id.registerTextView);
        TextView needHelpTextView = (TextView) view.findViewById(R.id.needHelpTextView);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        final TextInputLayout passwordEditText = (TextInputLayout) view.findViewById(R.id.passwordEditText);
        final EditText emailUsernameEditText = (EditText) view.findViewById(R.id.emailUsernameEditText);
        final TextView clickHere = (TextView) view.findViewById(R.id.clickHere);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        needHelpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegisterInstituteActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailUsernameEditText.getText().toString().equals("") || passwordEditText.getEditText().getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Field's can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(getActivity());
                    customLoadingDialog.customLoadingDialogShow();
                    firebaseAuth.signInWithEmailAndPassword(emailUsernameEditText.getText().toString().trim(), passwordEditText.getEditText().getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userType");
                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String type = dataSnapshot.getValue().toString();
                                                if (type.equals(loginUserType1)) {
                                                    customLoadingDialog.customLoaingDialogDismiss();
                                                    Intent intent = new Intent(getActivity(), STAHomeActivity.class);
                                                    intent.putExtra("userType", loginUserType1);
                                                    startActivity(intent);
                                                } else {
                                                    customLoadingDialog.customLoaingDialogDismiss();
                                                    CustomAlertDialogBox customAlertDialogBox = new CustomAlertDialogBox(getActivity());
                                                    customAlertDialogBox.CustomAlertDialogBoxShow("Looks like someone wants to be \na Teacher. Please login through Student login tab \uD83D\uDE03");
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                customLoadingDialog.customLoaingDialogDismiss();
                                                Toast.makeText(getActivity(), "Authentication failed please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        customLoadingDialog.customLoaingDialogDismiss();
                                        Toast.makeText(getActivity(), "Authentication failed please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            customLoadingDialog.customLoaingDialogDismiss();
                            CustomAlertDialogBox customAlertDialogBox = new CustomAlertDialogBox(getActivity());
                            customAlertDialogBox.CustomAlertDialogBoxShow(e.getMessage());
                        }
                    });
                }
            }
        });


        return view;
    }
}
