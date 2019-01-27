package com.ali.socialblog.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ali.socialblog.Data.UserRecycleAdapter;
import com.ali.socialblog.Model.User;
import com.ali.socialblog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserRecycleAdapter userRecycleAdapter;
    private List<User> userList;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        recyclerView = findViewById(R.id.listViewUsersID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReference.keepSynced(true);

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("User", "onChildAdded: " + dataSnapshot.getKey());
                User user = dataSnapshot.getValue(User.class);
                userList.add(user);
                userRecycleAdapter = new UserRecycleAdapter(UsersListActivity.this, userList);
                recyclerView.setAdapter(userRecycleAdapter);
                userRecycleAdapter.notifyDataSetChanged();
                Log.d("User data", "onChildAdded: " + user.getImage());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UsersListActivity.this, PostListActivity.class));
        finish();
    }
}
