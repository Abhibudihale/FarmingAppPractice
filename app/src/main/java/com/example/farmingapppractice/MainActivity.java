package com.example.farmingapppractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent1;
        if(firebaseUser != null)
        {
            intent1 = new Intent(MainActivity.this, HomeActivity.class);
        }
        else
        {
            intent1 = new Intent(MainActivity.this, LoginActivity.class);
        }

        startActivity(intent1);

    }
}