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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



import java.util.Optional;

public class ShopingListView extends AppCompatActivity implements View.OnClickListener{

    private static final String ON_COMPLETE = "Successfully: ";
    private static final String EXCEPTION = "Exception: ";
    private static final String TEST = "TEST STRING: ";


    private Button logout;

    private EditText editTextAddList, editTextAddProductName, editTextBarcode, productAmount, editTextCurrent_user;
    private Button addDataButton, getCurrentUserButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_list_view);

        mAuth = FirebaseAuth.getInstance();


        editTextAddList = (EditText) findViewById(R.id.list_name);
        editTextBarcode = (EditText) findViewById(R.id.barcode);
        productAmount = (EditText) findViewById(R.id.productAmount);
        editTextAddProductName = (EditText) findViewById(R.id.product);

        editTextCurrent_user = (EditText) findViewById(R.id.current_user);

        addDataButton = (Button) findViewById(R.id.add_dataBtn);
        addDataButton.setOnClickListener(this);

        getCurrentUserButton = (Button) findViewById(R.id.check_CurrentUserBtn);
        getCurrentUserButton.setOnClickListener(this);



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

    private void getCurrentUser() {

        try{
            editTextCurrent_user.setText( mAuth.getCurrentUser().getUid() );
            Log.d(TEST,"Getting Current User: " + mAuth.getCurrentUser().getUid() );
        }
        catch (Exception e) {
            //editTextCurrent_user.setText( e.getMessage().trim() );
            Log.e(EXCEPTION,"Getting Current User: " + e.getMessage());
        }
    }


    private void insertDataToDatabase(){
        Toast.makeText(ShopingListView.this, "Test String!! ", Toast.LENGTH_LONG).show();

        String productName = editTextAddProductName.getText().toString().trim();
        String barcode = editTextBarcode.getText().toString().trim();
        String amount = productAmount.getText().toString().trim();
        //int amountX = Integer.parseInt(amount);

        int amountX;
        try {
            amountX = Integer.parseInt(amount);
        }
        catch (NumberFormatException e) {
            amountX = 0;
        }

        Product product = new Product(barcode, productName, amountX);

        // snapShop data to get number of products with in the list ?
        // need to get the getUid()

        // Remember first is a seek from scanned code with in a product database - if unknown then prompt to make a new entry to main db. with picture!!

        database.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("List").child("Refrigerator").child("1").setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {

                    //Toast.makeText(ShopingListView.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();
                    Log.d(ON_COMPLETE, "Added data to database");

                    editTextBarcode.setText("");
                    editTextAddProductName.setText("");
                    productAmount.setText("");
                    editTextBarcode.requestFocus();

                }
                /*
                else{
                    Toast.makeText(ShopingListView.this, "Failed to added data!!", Toast.LENGTH_LONG).show();
                }
                */
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(EXCEPTION,"Failed to add the data: " + e.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_dataBtn:
                insertDataToDatabase();
                break;
            case R.id.check_CurrentUserBtn:
                getCurrentUser();
                break;
            case R.id.signOut:
                startActivity(new Intent(ShopingListView.this,MainActivity.class));
                finish();
                //FirebaseAuth.getInstance().signOut();
                break;
        }
    }


}