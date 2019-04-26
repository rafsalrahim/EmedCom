package com.example.emedcom;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Sell extends AppCompatActivity {

    EditText med_name,com_name,from,quantity,exp_date;
    Button sell;

    Calendar mCurrentDate;
    int day, month, year;


    private FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDb,mDbUSer;
    FirebaseAuth.AuthStateListener Authlistener;

    String userDist,usr_id,tet;
    String userKey;
    String medname,frm,qnty,sell_sell,expdate;
    String compname,dist;
    userData usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);


        med_name = (EditText) findViewById(R.id.med_name);
        com_name = (EditText) findViewById(R.id.com_name);
        from = (EditText) findViewById(R.id.from);
        quantity = (EditText) findViewById(R.id.quantity);
        exp_date=(EditText) findViewById(R.id.med_expiry) ;
        sell = (Button) findViewById(R.id.btnSell);

        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;
      //  exp_date.setText(day+"/"+month+"/"+year);


        exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Sell.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        monthOfYear = monthOfYear+1;
                        exp_date.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
         userKey = user.getUid();
         tet=user.getEmail();
         usr=new userData();

        mDbUSer=FirebaseDatabase.getInstance().getReference("registration");
        mDbUSer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for( DataSnapshot ds: dataSnapshot.getChildren()){
                   usr = ds.getValue(userData.class);
                   //userDist = ds.getValue().toString();
                   if(usr.getEmail().equals(tet)){
                    userDist = usr.getDistrict();
                    dist=usr.getUser_type();
                   Toast.makeText(getApplicationContext(), "Hello" + " " +userDist+ " "+dist, Toast.LENGTH_SHORT).show();
                   break;}
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Toast.makeText(getApplicationContext(), "hi"+ usr.getEmail() , Toast.LENGTH_SHORT).show();
        /*mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });-*/
        //Toast.makeText(this, "Hello" + dist+userDist , Toast.LENGTH_SHORT).show();

       /* sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               medname=med_name.getText().toString();
               compname=com_name.getText().toString();

                Toast.makeText(getApplicationContext(), "msg msg"+medname, Toast.LENGTH_SHORT).show();

            }
        });*/

        //Toast.makeText(this, "Hello" + medname + compname , Toast.LENGTH_SHORT).show();

        if(!"Collection Centre".equals(dist)) {

                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        medname = med_name.getText().toString();
                        compname = com_name.getText().toString();
                        frm = from.getText().toString();
                        qnty = quantity.getText().toString();
                        expdate = exp_date.getText().toString();

                        if (medname.isEmpty() || compname.isEmpty() || frm.isEmpty() || qnty.isEmpty() || expdate.isEmpty()) {
                            //something goes wrong
                            //all fields need to be filled
                            showMessage("Please verify all fields");

                        } else {
                            sell_sell = "No";
                            usr_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final sell_info info = new sell_info(
                                    medname,
                                    compname,
                                    frm,
                                    qnty,
                                    sell_sell,
                                    usr_id,
                                    userDist,
                                    expdate
                            );
                            Long tsLong = System.currentTimeMillis() / 1000;
                            final String ts = tsLong.toString();

                            FirebaseDatabase.getInstance().getReference("user_med_sell_details")
                                    .child(usr_id).child(medname + qnty)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        FirebaseDatabase.getInstance().getReference("Collection_accpt_details").child(userDist)
                                                .child(medname + compname).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "Medicine Name : " + medname + "\nGeneric Name : " + compname, Toast.LENGTH_SHORT).show();
                                                showMessage(medname);

                                                Intent home = new Intent(getApplicationContext(), Home.class);
                                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(home);

                                            }
                                        });


                                    } else {
                                        showMessage("Not Saved");
                                        Toast.makeText(Sell.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }
                    }
                });

        }
        else
            {
            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    medname = med_name.getText().toString();
                    compname = com_name.getText().toString();
                    frm = from.getText().toString();
                    qnty = quantity.getText().toString();
                    expdate = exp_date.getText().toString();
                    sell_sell = "No";
                    usr_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final sell_info info = new sell_info(
                            medname,
                            compname,
                            frm,
                            qnty,
                            sell_sell,
                            usr_id,
                            userDist,
                            expdate
                    );
                    Long tsLong = System.currentTimeMillis() / 1000;
                    final String ts = tsLong.toString();

                    FirebaseDatabase.getInstance().getReference("collection_inter_change_details")
                            .child(medname + qnty)
                            .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), medname + compname, Toast.LENGTH_SHORT).show();
                                showMessage(medname);

                            } else {
                                showMessage("Not Saved");
                                Toast.makeText(Sell.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            });

        }


    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();

    }
}
