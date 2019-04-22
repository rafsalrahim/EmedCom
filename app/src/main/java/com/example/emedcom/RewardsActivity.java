package com.example.emedcom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RewardsActivity extends AppCompatActivity {

    TextView points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        points = (TextView) findViewById(R.id.txt_point);

    }
}
