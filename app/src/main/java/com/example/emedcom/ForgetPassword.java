package com.example.emedcom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    private EditText passwordEmail;
    private Button resetPassword;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);


        passwordEmail = (EditText) findViewById(R.id.email_password);
        resetPassword = (Button) findViewById(R.id.btn_forget_password);

        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = passwordEmail.getText().toString().trim();

                if(useremail.isEmpty()) {

                    Toast.makeText(ForgetPassword.this,"Please enter your verified email address",Toast.LENGTH_LONG).show();

                } else {
                    // sending password reset email
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {

                                Toast.makeText(ForgetPassword.this,"Password reset E-mail sent !!!",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgetPassword.this,LoginActivity.class));
                            }
                            //if the email is nit registered already it will throw an error
                            else {

                                Toast.makeText(ForgetPassword.this,"OOPS !!! Error in sending password reset E-mail !!!",Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }

            }
        });
    }
}
