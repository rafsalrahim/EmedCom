package com.example.emedcom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Buy extends AppCompatActivity {

    ListView listView;

    SearchView searchView;


    FirebaseDatabase mDatabase;
    DatabaseReference mDb,mDbUSer,mDbUSer2;
    MedData  med;
    ArrayList<String> list;

    //ArrayAdapter <medicinedata> adapt;
    ArrayAdapter<String> adapter;
    private FirebaseAuth firebaseAuth;

    public String[] ab;
    public int i=0;
    public String userDist,tet,userKey;
    userData usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        tet=user.getEmail();
        userKey = user.getUid();
        ab = new String [30];
        listView = (ListView) findViewById(R.id.list);
        searchView = (SearchView) findViewById(R.id.searchView);
        list=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,R.layout.list_buy,R.id.ltext,list);
      //  adapter=new ArrayAdapter<String>(this,R.layout.activity_buy,R.id.ltext,list);
        //adapter=new ArrayAdapter<medicinedata>(this,R.layout.test,R.id.ltext);
        //userKey =FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDbUSer=FirebaseDatabase.getInstance().getReference();
        mDbUSer2=mDbUSer.child("registration").child(userKey).child("district");
        mDbUSer2.keepSynced(true);
        mDbUSer2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //usr = ds.getValue(userData.class);
                    userDist = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), userDist , Toast.LENGTH_LONG).show();


                mDatabase = FirebaseDatabase.getInstance();
                mDb = mDatabase.getReference("Collection_accpt_details").child(userDist);
                //DatabaseReference zone1Ref = (DatabaseReference) mDb.child();
                med=new MedData();

                mDb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){  //dataSnapshot.child(userDist)

                                //Toast.makeText(Buy.this, ""+ds, Toast.LENGTH_SHORT).show();

                                med=ds.getValue(MedData.class);
                                if(med.getSell().equals("No") ){  //&& med.getFrom().equals(userDist)
                                    //Toast.makeText(Buy.this, "  "+med.from, Toast.LENGTH_SHORT).show();
                                    list.add(med.getMedname().toString()+"  "+med.getQuantity()+"  "+med.getSell().toString());
                                    ab[i]=med.getQuantity();
                                    i=i+1;
                                }
                        }
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(), tet+userKey+userDist , Toast.LENGTH_LONG).show();

/*
        mDbUSer=FirebaseDatabase.getInstance().getReference("registration");
        mDbUSer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot ds: dataSnapshot.getChildren()){
                    usr = ds.getValue(userData.class);
                    Toast.makeText(getApplicationContext(), "Hello" +usr.getEmail(), Toast.LENGTH_SHORT).show();
                    //userDist = ds.getValue().toString();
                   /* if(usr.getEmail().equals(tet)){
                        userDist = usr.getDistrict();
                        Toast.makeText(getApplicationContext(), "Hello" +userDist, Toast.LENGTH_SHORT).show();
                        break;}*/
 /*               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        //Toast.makeText(getApplicationContext(), userDist , Toast.LENGTH_LONG).show();
//Delay time should be set since the data is loaded correctly within time

              ////////

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /* View parentRow = (View) view.getParent();
         ListView listView = (ListView) parentRow.getParent();
         final int pos = listView.getPositionForView(parentRow);*/

        // Toast.makeText(getApplicationContext(), ab[position] , Toast.LENGTH_LONG).show();

              String entry = (String) parent.getItemAtPosition(position);
             Toast.makeText(getApplicationContext(), entry , Toast.LENGTH_LONG).show();

              Intent intent = new Intent(Buy.this, placeOrder.class);
         //Get the value of the item you clicked
         //String itemClicked = countries[position];
             intent.putExtra("details", ab[position] );
             intent.putExtra("dist",userDist);
             startActivity(intent);


           }
        });

        /*med_name = (EditText) findViewById(R.id.med_name);
        com_name = (EditText) findViewById(R.id.com_name);
        from = (EditText) findViewById(R.id.from);
        quantity = (EditText) findViewById(R.id.quantity);

        buy = (Button) findViewById(R.id.btnBuy);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();


        mDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {showData(dataSnapshot);}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //Toast.makeText(this, "Hello" + test, Toast.LENGTH_SHORT).show();
    }

   /* public void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            medicinedata data=new medicinedata();
            userData datatest=new userData();
            data.setCompname(ds.child(userKey).getValue(medicinedata.class).getCompname());
            //data.compname(ds.child(userKey).getValue(userData.class).ge);
            //data.setFrom(ds.child(userKey).getValue(medicinedata.class).getFrom());
            data.setMedname(ds.child(userKey).getValue(medicinedata.class).getMedname());
            datatest.setPhone(ds.child(userKey).getValue(userData.class).getPhone());
            test=datatest.getPhone();
            Toast.makeText(this, "Hello inside fun  " + test, Toast.LENGTH_SHORT).show();
        }
    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();

    }*/

}
