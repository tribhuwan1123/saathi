package com.example.sathi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView name;
    String fullName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(TextView)findViewById(R.id.name);

        Intent intent= getIntent();

        fullName= intent.getStringExtra("firstName");
        name.setText(fullName);
    }
}
