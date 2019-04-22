package com.example.emedcom;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class placeOrder extends AppCompatActivity {



    private FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference mDb,mDb2,mDb3,mDbUSer;
    MedData  med,med2;
    userData usr;
    TextView t1;
    EditText t2;
    Button b1;
    int test2,qnt;
    String userKey,ts,sell_sell,medname,userDist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        t1=(TextView) findViewById(R.id.textView);
        t2=(EditText) findViewById(R.id.sell_count);
        b1=(Button) findViewById(R.id.btn_buy);
        //Viewed Data

        final String details = getIntent().getStringExtra("details");
        final String dist = getIntent().getStringExtra("dist");
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference("Collection_accpt_details").child(dist);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userKey =FirebaseAuth.getInstance().getCurrentUser().getUid();


        med=new MedData();
        //med2=new MedData();
        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    med=ds.getValue(MedData.class);
                    if(details.equals(med.getQuantity())){
                        t1.setText(med.getMedname());
                        break; //updated now not tested
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*
        mDbUSer=FirebaseDatabase.getInstance().getReference("registration").child(userKey);
        mDbUSer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for( DataSnapshot ds: dataSnapshot.getChildren()){
                    usr=dataSnapshot.getValue(userData.class);
               // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //userDist =usr.getDistrict();
        //Buy medicine
        mDb2 = mDatabase.getReference();
        //mDb3=mDatabase.getReference("med_sell_details");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            place_order_details pls_order=new place_order_details(
                t1.getText().toString(),
                    userKey
            );
                Toast.makeText(getApplicationContext(),med.getMedname(), Toast.LENGTH_SHORT).show();
                String test1=t2.getText().toString();
                 test2=Integer.parseInt(test1);
                Long tsLong = System.currentTimeMillis()/1000;
                 ts = tsLong.toString();
                ///Toast.makeText(getApplicationContext(),test1, Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("place_order_details")
                        .child(userKey+ts).setValue(pls_order).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {









                             // test= Integer.parseInt(med.getQuantity().toString());
                             qnt=Integer.parseInt(med.getQuantity().toString())-test2;
                            ///showMessage( String.valueOf(qnt)+"hi" );
                            //Toast.makeText(getApplicationContext(),String.valueOf(qnt)+"hi", Toast.LENGTH_SHORT).show();
                            /*if(qnt<=0){
                             sell_sell="Yes";
                            }
                            else{
                                sell_sell="No";
                            }////*/
                            //user account created successfully
                          Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                            //showMessage( ts );
                            String tt=String.valueOf(qnt);
                            //mDb3=mDatabase.getReference("med_sell_details").child(userKey+med.getMedname());
                            //Toast.makeText(getApplicationContext(), med.getMedname(), Toast.LENGTH_SHORT).show();
                            medname=med.getMedname();
                            String compname=med.getCompname();
                            String frm=med.getFrom();
                            String collection = med.getCollection_center();
                            String exp_date = med.getExp_date();
                            //String qnty=quantity.getText().toString();
                            sell_sell="Yes";
                            // usr_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            sell_info info = new sell_info(
                                    medname,
                                    compname,
                                    frm,
                                    tt,
                                    sell_sell,
                                    userKey,
                                    collection,
                                    exp_date
                            );



                            FirebaseDatabase.getInstance().getReference("Collection_accpt_details")
                                    .child(dist).child(medname+compname)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        showMessage( "Yes i have done it" );
                                    }

                                    else {
                                        showMessage("Not Saved");
                                        Toast.makeText(placeOrder.this,task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                        }


                        else {
                            showMessage("Not Saved");
                            Toast.makeText(placeOrder.this,task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });



            }
        });


    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();

    }
}
