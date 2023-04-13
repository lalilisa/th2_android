package com.example.testsqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testsqllite.config.datasource.SqlLiteConfig;



public class MainActivity extends AppCompatActivity {
    private SqlLiteConfig sqlLiteConfig=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sqlLiteConfig=new SqlLiteConfig(this);
        sqlLiteConfig.open();






    }
}