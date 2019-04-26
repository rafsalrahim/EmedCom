package com.example.emedcom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri;

    private EditText userEmail,userPassword,userConfirmPassword,userName,userPhone;
    private ProgressBar loadingProgress;
    private Button regBtn,buttonLogin;
    private RadioButton store, centre, user;

    private FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    String user_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //views

        userEmail = (EditText) findViewById(R.id.regMail);
        userName = (EditText) findViewById(R.id.regName);
        userPhone = (EditText) findViewById(R.id.regPhno);
        userPassword = (EditText) findViewById(R.id.regPassword);
        userConfirmPassword = (EditText) findViewById(R.id.regConfirmPassword);
        loadingProgress = (ProgressBar) findViewById(R.id.regProgressBar);
        regBtn = (Button) findViewById(R.id.regBtn);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        store = (RadioButton) findViewById(R.id.id_store);
        centre = (RadioButton) findViewById(R.id.id_centre);
        user = (RadioButton) findViewById(R.id.id_user);

        user.setChecked(true);


        loadingProgress.setVisibility(View.INVISIBLE);

        databaseReference = FirebaseDatabase.getInstance().getReference("registration");
        mAuth = FirebaseAuth.getInstance();


        final Spinner spinnerdis = (Spinner) findViewById(R.id.myDistrict);
        //drop down list of different districts
        final String[] districts = new String[] {"Select District",
                "Kollam",
                "Alappuzha",
                "Pathanamthitta",
                "Kottayam",
                "Idukki",
                "Ernakulam",
                "Thrissur",
                "Palakkad",
                "Malappuram",
                "Kozhikode",
                "Wayanad",
                "Kannur",
                "Kasargod"};

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> adapterdis = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, districts);
        adapterdis.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdis.setAdapter(adapterdis);

        spinnerdis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextColor(Color.rgb(76, 156, 210));
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                districts[0] = "Thiruvananthapuram";
                String selectedItem = districts[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent IntentLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(IntentLogin);

            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String name = userName.getText().toString();
                final String phone = userPhone.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmPassword = userConfirmPassword.getText().toString();
                final String spinnerdistrict = (String) spinnerdis.getSelectedItem().toString();

                if(store.isChecked()) {

                    user_type = "Medical Store";
                }
                if(centre.isChecked()) {

                    user_type = "Collection Centre";
                }
                if(user.isChecked()) {

                    user_type = "Normal User";
                }

                if( email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                    //something goes wrong
                    //all fields need to be filled
                    showMessage("Please verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }
                else if(phone.length()!=10) {

                    userPhone.setError("Phone number length must be 10");
                    userPhone.requestFocus();
                    // showMessage("Phone number length must be 10");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }
                else if(!confirmPassword.equals(password)){

                    userConfirmPassword.setError("password not Matching");
                    userConfirmPassword.requestFocus();
                    // showMessage("password not matching");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    //everything is ok and all fields are filled, now we can start creating user account
                    //it will create an user if the email is valid
                    CreateUserAccount(email,name,password,phone,user_type,spinnerdistrict);
                }
            }
        });


        ImgUserPhoto = (ImageView) findViewById(R.id.regUserPhoto);

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Build.VERSION.SDK_INT >=22) {

                    checkAndRequestForPermission();
                }
                else
                {

                    openGallery();

                }

            }
        });
    }

    private void CreateUserAccount(final String email, final String name, final String password
            , final String phone, final String user_type, final String spinnerdistrict) {

        // this method creates an user with specific email and password

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {

                            //entering value into db
                            test info = new test(
                                    name,
                                    email,
                                    phone,
                                    user_type,
                                    spinnerdistrict
                            );

                            FirebaseDatabase.getInstance().getReference("registration")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    mAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()) {

                                                        //user account created successfully
                                                        showMessage("Account Created Successfully!!! \nPlease check your mail and verify");
                                                        //after we create a user account we need to update profile picture and name
                                                        updateUserInfo( name , pickedImgUri,mAuth.getCurrentUser());


                                                    }

                                                    else {
                                                        Toast.makeText(RegisterActivity.this,task.getException().getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });

                                }
                            });
                        }
                        else
                        {

                            //account creation failed
                            showMessage("Account Creation Failed..!!" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }

                    }
                });


    }


    //update user photo and name
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                //image uploaded successfully
                //now we can get image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //uri contain user image url

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()) {

                                            //user info updated successfully
                                            showMessage("Registration Complete");
                                            showMessage("You are on the home page now");
                                            updateUI();
                                        }

                                    }
                                });

                    }
                });
            }
        });
    }

    private void updateUI() {

        Intent loginActivity = new Intent(getApplicationContext(), Home.class);
        startActivity(loginActivity);
        finish();
    }


    //simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();

    }

    private void openGallery() {
        //TODO : open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);



    }

    private void checkAndRequestForPermission() {

        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {

                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);

            }
        }
        else
            openGallery();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            //the user has successfully picked an image
            //we need to save reference to URI variable

            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);

        }
    }
}


