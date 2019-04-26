package com.example.emedcom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class ChangemailActivity extends AppCompatActivity {


    EditText editText1;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changemail);

        editText1 = (EditText) findViewById(R.id.old_mail);
        editText2 = (EditText) findViewById(R.id.new_mail);

    }
}
