package com.example.esra.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {
Button GotoMapBtn;
Button GotoClassrooms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        GotoMapBtn= (Button) findViewById(R.id.button);
        GotoMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        GotoClassrooms=(Button) findViewById(R.id.button2);
        GotoClassrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this, ClassroomsMainActivity.class);
                startActivity(intent);
            }
        });


    }

}
