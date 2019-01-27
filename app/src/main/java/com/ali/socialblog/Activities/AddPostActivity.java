package com.ali.socialblog.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ali.socialblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private ImageView imgPost;
    private EditText etPostTitle;
    private EditText etPostDescrip;
    private Button btSubmit;
    private ProgressDialog mProgress;

    private StorageReference mStorage;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private Uri imageUri; //get the path of image
    public static final int GALLAY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        setupUI();

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //posting to dataBase
                startposting();
            }
        });

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryInent = new Intent(Intent.ACTION_GET_CONTENT);
                gallaryInent.setType("image/*");
                startActivityForResult(gallaryInent, GALLAY_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLAY_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imgPost.setImageURI(imageUri);
        }
    }

    private void setupUI() {
        imgPost = findViewById(R.id.imgPostButtonID);
        etPostTitle = findViewById(R.id.etPostTitleID);
        etPostDescrip = findViewById(R.id.etPostDescripID);
        btSubmit = findViewById(R.id.btSubmitPostID);
        btSubmit = findViewById(R.id.btSubmitPostID);

        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");
        mStorage = FirebaseStorage.getInstance().getReference(); // get storage link
    }

    private void startposting() {
        mProgress.setMessage("Posting....");
        mProgress.show();
        final String title = etPostTitle.getText().toString().trim();
        final String descrip = etPostDescrip.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(descrip) && imageUri != null) {
            //Start Uploading
            final StorageReference filepath = mStorage.child("Blog_imgs")  // create folder
                    .child(imageUri.getLastPathSegment()); // get path

            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            String photoLink = task.getResult().toString();
                            Log.d("ADD post", "onSuccess: " + photoLink);
                            DatabaseReference newPost = mDatabaseReference.push();

                            Map<String, String> dataToSave = new HashMap<>();
                            dataToSave.put("pTitle", title);
                            dataToSave.put("desc", descrip);
                            dataToSave.put("pImage", photoLink);
                            dataToSave.put("timeStamp", String.valueOf(java.lang.System.currentTimeMillis()));
                            dataToSave.put("userId", mUser.getUid());

                            newPost.setValue(dataToSave);
                            mProgress.dismiss();
                            startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
                            finish();
                        }
                    });


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
        finish();
    }
}
