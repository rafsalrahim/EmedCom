package com.example.emedcom;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail,userPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
  //  private ImageView loginPhoto;
    private Button buttonSignup;

    private Button forgetpassword;

    private FirebaseAuth mAuth;

    private Intent HomeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userMail = (EditText) findViewById(R.id.login_mail);
        userPassword = (EditText) findViewById(R.id.login_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        forgetpassword = (Button) findViewById(R.id.button_forget_password);

        mAuth = FirebaseAuth.getInstance();

        HomeActivity = new Intent(this, Home.class);
    /*    loginPhoto = (ImageView) findViewById(R.id.login_photo);

        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);
                finish();

            }
        });    */

        // underline contents of sign up button
        buttonSignup.setPaintFlags(buttonSignup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        //forget password codes
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent forgetIntent = new Intent(getApplicationContext(),ForgetPassword.class);
                startActivity(forgetIntent);
                finish();

            }
        });


        loginProgress.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginProgress.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty()) {

                    showMessage("Please verify all fields");
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else
                {
                    signIn(mail, password);
                }

            }
        });
    }

    private void signIn(final String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {

                    ///email
                  if(mAuth.getCurrentUser().isEmailVerified()) {

                        loginProgress.setVisibility(View.INVISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                        updateUI();
                    }
                    else {

                        Toast.makeText(LoginActivity.this, "please verify email address"
                                ,Toast.LENGTH_LONG).show();
                    }

                    ///////////////////////////////////
                    loginProgress.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else
                {
                    showMessage(task.getException().getMessage());
                    btnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {

        Toast.makeText(getApplicationContext(),"WELCOME",Toast.LENGTH_LONG).show();

        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            //user is already connected so re-direct to home page
            updateUI();
        }
    }


}
