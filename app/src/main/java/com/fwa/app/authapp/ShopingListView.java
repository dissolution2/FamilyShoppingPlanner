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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



import java.util.Optional;

public class ShopingListView extends AppCompatActivity implements View.OnClickListener{

    private Button logout;

    private EditText editTextAddList, editTextAddProductName, editTextBarcode, productAmount;
    private Button addDataButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_list_view);


        editTextAddList = (EditText) findViewById(R.id.list_name);
        editTextBarcode = (EditText) findViewById(R.id.barcode);
        productAmount = (EditText) findViewById(R.id.productAmount);
        editTextAddProductName = (EditText) findViewById(R.id.product);

        addDataButton = (Button) findViewById(R.id.add_dataBtn);
        addDataButton.setOnClickListener(this);



/*
        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ShopingListView.this, MainActivity.class));
        }
    });

 */
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

                    Toast.makeText(ShopingListView.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();

                    editTextBarcode.setText("");
                    editTextAddProductName.setText("");
                    productAmount.setText("");
                    editTextBarcode.requestFocus();

                }else{
                    Toast.makeText(ShopingListView.this, "Failed to added data!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
*/

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_dataBtn:
                insertDataToDatabase();
                break;

        }
    }
}