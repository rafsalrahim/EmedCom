package com.example.emedcom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccount extends AppCompatActivity {

    Button deleteAccount;
    ProgressBar progressBar;
    TextView emailtv;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);


        deleteAccount = (Button) findViewById(R.id.btnDelete);
        progressBar = (ProgressBar) findViewById(R.id.delete_progress);
        emailtv = (TextView) findViewById(R.id.email_text);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        emailtv.setText("                 WELCOME     \n\n" + firebaseUser.getEmail());

        progressBar.setVisibility(View.INVISIBLE);

        // on click listener for deleting account
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(DeleteAccount.this);
                dialog.setTitle("Are You Sure ? ");
                dialog.setMessage("Deleting this account will result in completely removing your " +
                        "account from the system and you won't be able to access the app.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressBar.setVisibility(View.VISIBLE);

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressBar.setVisibility(View.GONE);

                                if(task.isSuccessful()) {

                                    Toast.makeText(DeleteAccount.this,"DELETED ACCOUNT SUCCESSFULLY !!!",
                                            Toast.LENGTH_LONG).show();

                                    // if account is deleted it will be redirected to login page

                                    Intent intent = new Intent(DeleteAccount.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else {

                                    Toast.makeText(DeleteAccount.this,task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                    }
                });

                dialog.setNegativeButton("DISMISS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                // we need to show the dialogue

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            }
        });
    }
}