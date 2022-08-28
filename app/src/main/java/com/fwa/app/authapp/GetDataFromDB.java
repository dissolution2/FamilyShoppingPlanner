package com.fwa.app.authapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetDataFromDB extends AppCompatActivity implements View.OnClickListener{

    private final String ERORR = "DB eror";

    private Button backBtn, show_dataBtn;
    private EditText editTextgetDBData;




    public FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    public DatabaseReference reference;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data_from_db);
        mAuth = FirebaseAuth.getInstance();
        editTextgetDBData = (EditText) findViewById(R.id.getDBData);

        show_dataBtn = (Button) findViewById(R.id.show_DataDBBtn);
        show_dataBtn.setOnClickListener(this);


        backBtn = (Button) (Button) findViewById(R.id.backButton);
        backBtn.setOnClickListener(this);

    }

    private void readData() {
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    List product_List = new ArrayList();

                    for (DataSnapshot child : dataSnapshot.getChildren() ) {

                        String barcode = child.getValue(Product.class).getBarcode();
                        String product = child.getValue(Product.class).getProductName();
                        int amount = child.getValue(Product.class).getProductAmount();

                        product_List.add(new Product(barcode,product,amount));
                    }
                    // test for fun!!
                    editTextgetDBData.setText( "BareCode: " + ((Product)product_List.get(0)).getBarcode() +
                            "\nProductName: " + ((Product)product_List.get(0)).getProductName() +
                            "\nProductAmount: " + ((Product)product_List.get(0)).getProductAmount() + "\n"+
                            "BareCode: " + ((Product)product_List.get(1)).getBarcode() +
                            "\nProductName: " + ((Product)product_List.get(1)).getProductName() +
                            "\nProductAmount: " + ((Product)product_List.get(1)).getProductAmount() );
                }else{
                    editTextgetDBData.setText("Don't = dataSnapshot exists !!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(ERORR,"data error " + databaseError.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.show_DataDBBtn:
                readData();
                break;
            case R.id.backButton:
                startActivity(new Intent(this, ManualyScanProductForTestingToDB.class));
                break;
            }


    }
}