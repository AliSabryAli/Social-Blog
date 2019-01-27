package com.ali.socialblog.Activities;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ali.socialblog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etFirstname;
    private EditText etLastname;
    private EditText etPassword;
    private EditText etEmail;
    private Button btCreateNew;
    private ImageView profileImg;

    private StorageReference mFirebaseStorage;
    private FirebaseDatabase mDataBase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    private final static int GALLARY_CODE = 1;
    private Uri resultUri = null;

    private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        setupUI();

        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("Blog Profile");

        btCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GALLARY_CODE);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARY_CODE && resultCode == RESULT_OK) {
            Uri mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .setAspectRatio(1, 1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                profileImg.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }


    private void createNewAccount() {
        final String fName = etFirstname.getText().toString().trim();
        final String lName = etLastname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)
                && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            dialog.setMessage("Creating Account");
            dialog.show();
            mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    if (authResult != null) {
                        isSuccess = true;
                        StorageReference imgPath = mFirebaseStorage.child("Blog Profile")
                                .child(resultUri.getLastPathSegment());
                        imgPath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(Task<Uri> task) {
                                        String userID = mAuth.getCurrentUser().getUid();
                                        DatabaseReference newUserDb = mDatabaseReference.child(userID);
                                        newUserDb.child("firstName").setValue(fName);
                                        newUserDb.child("lastName").setValue(lName);
                                        newUserDb.child("image").setValue(task.getResult().toString());
                                        dialog.dismiss();
                                        startActivity(new Intent(CreateUserActivity.this, PostListActivity.class));
                                        finish();
                                    }
                                });
                            }
                        });


                    }
                }
            });
            if (!isSuccess) {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnFailureListener(new OnFailureListener() {
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(CreateUserActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                isSuccess = false;
            }

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CreateUserActivity.this, LogInActivity.class));
        finish();
    }

    private void setupUI() {
        etFirstname = findViewById(R.id.etFirstNameID);
        etLastname = findViewById(R.id.etLastNameID);
        etEmail = findViewById(R.id.etEmailCreateID);
        etPassword = findViewById(R.id.etPasswordCraeteID);
        btCreateNew = findViewById(R.id.btCreateNewID);
        profileImg = findViewById(R.id.imgProfileID);

        mDataBase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDataBase.getReference().child("Users"); //new DB
        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
    }


}
