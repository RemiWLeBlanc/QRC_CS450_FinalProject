package com.example.remileblanc.qrc_cs450_finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MentorFragment extends Fragment {

    private MentorFragment.OnFragmentInteractionListener mListener;

    private Button viewSessionsButton;
    private String userName;
    private TextView mentorGreeting;
    private TextView sessionsData;
    private boolean clicked = true;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public MentorFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mentor, container, false);

        mentorGreeting = rootView.findViewById(R.id.mentorGreeting);
        viewSessionsButton = rootView.findViewById(R.id.viewSessionsButton);
        sessionsData = rootView.findViewById(R.id.sessionsData);

        getName(mAuth.getCurrentUser());
        sessionsData.setVisibility(View.INVISIBLE);

        viewSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference sessions = database.getReference("Mentor Sessions");
                sessions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String data = "";
                        getName(mAuth.getCurrentUser());
                        if(clicked) {
                            sessionsData.setText("");
                            sessionsData.setVisibility(View.VISIBLE);
                            viewSessionsButton.setText("Minimize Scheduled Sessions");
                            for (DataSnapshot mentor : dataSnapshot.getChildren()) {
                                System.out.println(mentor.getValue());
                                System.out.println(userName);
                                if (mentor.getKey().equals(userName)) {
                                    for (DataSnapshot day : mentor.getChildren()) {
                                        String date = day.child("date").getValue().toString();
                                        String time = day.child("time").getValue().toString();
                                        String prof = day.child("professor").getValue().toString();
                                        String course = day.child("course").getValue().toString();
                                        String student = day.child("name").getValue().toString();
                                        data = "- You have a scheduled session on " + date + " at " + time + " with " + student + " who is looking for help in " + course + ", and is taking it with " + prof + ".";
                                        sessionsData.append(data);
                                        sessionsData.append("\n\n");
                                    }
                                }
                            }
                            if(sessionsData.getText().length() == 0){
                                sessionsData.append("- You have no scheduled sessions.");
                            }
                            clicked = false;
                        } else if(!clicked){
                            viewSessionsButton.setText("View Scheduled Sessions");
                            sessionsData.setVisibility(View.INVISIBLE);
                            clicked = true;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });



        return rootView;

    }

    public void getName(final FirebaseUser specificUser){
        final DatabaseReference users = database.getReference("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey().equals(specificUser.getUid())){
                        userName = ds.child("firstName").getValue() + " " + ds.child("lastName").getValue();
                        System.out.println(userName);
                        String greeting = "Hello, Mentor "+userName+"!";
                        mentorGreeting.setText(greeting);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MentorFragment.OnFragmentInteractionListener) {
            mListener = (MentorFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
