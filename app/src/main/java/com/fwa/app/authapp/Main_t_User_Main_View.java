package com.fwa.app.authapp;

import android.app.appsearch.PutDocumentsRequest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Main_t_User_Main_View extends AppCompatActivity implements View.OnClickListener{

    private Button scanBarcodeBtn;
private EditText editText_BarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_t_user_main_view);

        editText_BarCode = (EditText) findViewById(R.id.editTextBarCode);



        scanBarcodeBtn = (Button) findViewById(R.id.scanCodeBtn);
        scanBarcodeBtn.setOnClickListener(this);


    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(Main_t_User_Main_View.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(Main_t_User_Main_View.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "BarCode is");
                    Toast.makeText(Main_t_User_Main_View.this, "Scanned is: " + result.getContents(), Toast.LENGTH_LONG).show();

                    editText_BarCode.setText(result.getContents());
                    addProduct_to_User_database_list(result.getContents());
                    /** Save the Content and make a new product to the database */


                }
            });


    public void addProduct_to_User_database_list(String product_code){





    }

    // query Realtime DB
    // - if true - add_product_amount_OnUser_Realtime_db
    // - if false - queryMaster_DB_KnownProduct()
    public boolean queryUserKnownProductInList(String product_code){






        return false;
    }


    public boolean add_product_amount_OnUser_Realtime_db(String product_code, String product_amount){

        return true; // added product amount++
    }


    // if user {add a product not known to user} - check if the product exist in Main DB
    // if true - addProduct_to_User_database_list( product_code )
    // if false - add_Unknown_product_to_User_DB(.....)
    // (after a time DM must do regularly products in user_db and transfer them to main DB.
    public boolean queryMaster_DB_KnownProduct(String product_code){






        return false;
    }

    // Might just use a class Product as input.
    public void add_Unknown_product_to_User_DB(String bare_code, String product_name,
                                               String companyName, String product_amount,
                                               String storage_List, String image_ref ){



    }





    public void scanToolbar(View view) {
        ScanOptions options = new ScanOptions().setCaptureActivity(ToolbarCaptureActivity.class);
        barcodeLauncher.launch(options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.scanCodeBtn:
                scanToolbar(v);
                //startActivity(new Intent(this, Main_t_manualy_add_products_to_db.class));
                break;
        }
    }
}
