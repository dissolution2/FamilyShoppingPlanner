package com.fwa.app.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class ManualyScanProductForTestingToDB extends AppCompatActivity implements View.OnClickListener{

    private Button logout, test_DB_Insert, test_show_DataDBBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualy_scan_product_for_testing_to_db);



        test_DB_Insert = (Button) findViewById(R.id.testDBBtn);
        test_DB_Insert.setOnClickListener(this);

        test_show_DataDBBtn = (Button) findViewById(R.id.show_DataDBBtn);
        test_show_DataDBBtn.setOnClickListener(this);


        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);


    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event){
        if(key_code == android.view.KeyEvent.KEYCODE_BACK){
            super.onKeyDown(key_code, key_event);
            Toast.makeText(getApplicationContext(), "back press Disabled!!",
                    Toast.LENGTH_LONG).show();

        }
        return false;
    }







    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.testDBBtn:
                startActivity(new Intent(this, ShopingListView.class));
                break;
            case R.id.show_DataDBBtn:
                startActivity(new Intent(this, GetDataFromDB.class));
                break;

            case R.id.signOut:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }


    }
}