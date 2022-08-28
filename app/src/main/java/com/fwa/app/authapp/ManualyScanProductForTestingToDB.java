package com.fwa.app.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ManualyScanProductForTestingToDB extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TEST:";
    private Button logout, test_DB_Insert, test_show_DataDBBtn, test_guiListViewBtn, test_recycleViewBtn;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualy_scan_product_for_testing_to_db);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        test_DB_Insert = (Button) findViewById(R.id.testDBBtn);
        test_DB_Insert.setOnClickListener(this);

        test_show_DataDBBtn = (Button) findViewById(R.id.show_DataDBBtn);
        test_show_DataDBBtn.setOnClickListener(this);

        test_guiListViewBtn = (Button) findViewById(R.id.guiListViewBtn);
        test_guiListViewBtn.setOnClickListener(this);

        test_recycleViewBtn = findViewById(R.id.recycleView);
        test_recycleViewBtn.setOnClickListener(this);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);


        /** Se how and what loggedIn !!! ???  */

        if (gsc != null) {
            //gsc.getSignInIntent().toString();
            Log.d(TAG, "GOOGLE: " + gsc.getSignInIntent().toString());
        }
        if( mAuth.getCurrentUser() != null){
            //
            Log.d(TAG, "mAuth: " + mAuth.getUid().toString()) ;
        }
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
            case R.id.recycleView:
                startActivity(new Intent(this, RecycleViewTest.class));
                break;
            case R.id.guiListViewBtn:
                startActivity(new Intent(this, MainViewGuiShopping.class));
                break;
            case R.id.signOut:

                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        finish();
                        mAuth.signOut(); // new added to sign out of it downt know it this is ok or just to change user to what we want!!
                        startActivity(new Intent(ManualyScanProductForTestingToDB.this, MainActivity.class));
                    }
                });

                //startActivity(new Intent(this, MainActivity.class));
                //finish();
                break;

        }


    }
}