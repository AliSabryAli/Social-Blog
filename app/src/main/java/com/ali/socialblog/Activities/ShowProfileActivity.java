package com.ali.socialblog.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ali.socialblog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView tvFullName;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private String name;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        imgProfile = findViewById(R.id.imgShowProfileID);
        tvFullName = findViewById(R.id.tvNameProfileID);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid());
        mDatabaseReference.keepSynced(true);


        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.getKey().equals("firstName")) {
                    name = dataSnapshot.getValue().toString();
                } else if (dataSnapshot.getKey().equals("lastName")) {
                    name += " ";
                    name += dataSnapshot.getValue().toString();
                    tvFullName.setText(name);
                } else if (dataSnapshot.getKey().equals("image")) {
                    url = dataSnapshot.getValue().toString();
                    Picasso.get().load(url).fit().into(imgProfile);
                }
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
        startActivity(new Intent(ShowProfileActivity.this, PostListActivity.class));
        finish();
    }
}
