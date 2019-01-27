package com.ali.socialblog.Activities;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import com.ali.socialblog.Data.BlogRecycleAdapter;
import com.ali.socialblog.Model.Blog;
import com.ali.socialblog.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostListActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private RecyclerView recyclerView;
    private BlogRecycleAdapter blogRecycleAdapter;
    List<Blog> blogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = findViewById(R.id.recycleViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        blogList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Blog");
        mDatabaseReference.keepSynced(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_signout) {
            if (mAuth != null && mUser != null) {
                mAuth.signOut();
                startActivity(new Intent(PostListActivity.this, LogInActivity.class));
                finish();
            }
        }
        if (item.getItemId() == R.id.action_add) {
            if (mAuth != null && mUser != null) {
                startActivity(new Intent(PostListActivity.this, AddPostActivity.class));
                finish();
            }
        }
        if (item.getItemId() == R.id.action_profiles) {
            if (mAuth != null && mUser != null) {
                startActivity(new Intent(PostListActivity.this, ShowProfileActivity.class));
                finish();
            }
        }
        if (item.getItemId() == R.id.action_connections) {
            if (mAuth != null && mUser != null) {
                startActivity(new Intent(PostListActivity.this, UsersListActivity.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Blog blog = dataSnapshot.getValue(Blog.class); //map database objects to blog var (matching);
                blogList.add(blog);
                Collections.reverse(blogList);
                blogRecycleAdapter = new BlogRecycleAdapter(PostListActivity.this, blogList);
                recyclerView.setAdapter(blogRecycleAdapter);
                blogRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
