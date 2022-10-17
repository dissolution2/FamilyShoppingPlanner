package com.fwa.app.product.manualy.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fwa.app.classes.Product;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.familyshoppingplanner.ToolbarCaptureActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class main_add_product_shopping_list_with_barcode_reader_db extends AppCompatActivity implements View.OnClickListener{

    private Button scanBarcodeBtn;
    private EditText editText_BarCode, editText_ProductName, editText_AddToList;
    private boolean queryUserIsProductKnownReturnValue = false;
    String productCodeis ="";

    private FirebaseAuth mAuth;
    public FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_shopping_list_db_view);

        //queryUserIsProductKnownReturnValue = false;

        mAuth = FirebaseAuth.getInstance();

        editText_BarCode = (EditText) findViewById(R.id.editTextBarCode);
        editText_ProductName = (EditText) findViewById(R.id.editTextProductName);
        editText_AddToList = (EditText) findViewById(R.id.editTextAddToList);

        scanBarcodeBtn = (Button) findViewById(R.id.scanCodeBtn);
        scanBarcodeBtn.setOnClickListener(this);



        //readData(list -> Log.d("TAG", ": " + ((Product)list.get(0)).getProductName() ));

        /** Test on getting data - outside of its scope for larger query's we need to do most with inn the scope and take out what we can!!!
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(List<Product> list) {
                editText_BarCode.setText( ((Product)list.get(0)).getProductName() );
            }
        });
         //readData(list -> Log.d("TAG", ": " + ((Product)list.get(0)).getProductName() )); simpler formatted as output string.
        */
    }

    /** User input by barcode : Where is the user !! Check so we can reuse this!!
     * 1. Scan Barcode: add product to shopping list
     * 2. Scan barcode: remove item from shopping list - At the store
     *
     * */
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        //Toast.makeText(Main_t_User_Main_View.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        //Toast.makeText(Main_t_User_Main_View.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "BarCode is");
                    //Toast.makeText(Main_t_User_Main_View.this, "Scanned is: " + result.getContents(), Toast.LENGTH_LONG).show();

                    /** Callback On query as Asynchronous Firebase API */
                    /** Checking Refrigerator */
                    readDataRefrigerator(new FirebaseCallback() {
                        @Override
                        public void onCallback(List<Product> list) {

                            if(!list.isEmpty()) {

                                editText_BarCode.setText(((Product) list.get(0)).getBarcode());
                                editText_ProductName.setText(((Product) list.get(0)).getStorage());

                                writeData_to_shoppingList_from_barcode(
                                        ((Product) list.get(0)).getBarcode(),
                                        ((Product) list.get(0)).getName(),
                                        ((Product) list.get(0)).getCompany(),
                                        ((Product) list.get(0)).getAmount(),
                                        ((Product) list.get(0)).getStorage(), editText_AddToList.getText().toString().toUpperCase().trim() );
                            }else{
                                /** Checking Freezer */
                                readDataFreezer(new FirebaseCallback() {
                                    @Override
                                    public void onCallback(List<Product> list) {

                                        if (!list.isEmpty()) {

                                            editText_BarCode.setText(((Product) list.get(0)).getBarcode());
                                            editText_ProductName.setText(((Product) list.get(0)).getStorage());

                                            writeData_to_shoppingList_from_barcode(
                                                    ((Product) list.get(0)).getBarcode(),
                                                    ((Product) list.get(0)).getName(),
                                                    ((Product) list.get(0)).getCompany(),
                                                    ((Product) list.get(0)).getAmount(),
                                                    ((Product) list.get(0)).getStorage(), editText_AddToList.getText().toString().toUpperCase().trim() );
                                        } else {
                                            /** Checking DryStorage */
                                            readDataDryStorage(new FirebaseCallback() {
                                                @Override
                                                public void onCallback(List<Product> list) {

                                                    if(!list.isEmpty()) {

                                                        editText_BarCode.setText(((Product) list.get(0)).getBarcode());
                                                        editText_ProductName.setText(((Product) list.get(0)).getStorage());

                                                        writeData_to_shoppingList_from_barcode(
                                                                ((Product) list.get(0)).getBarcode(),
                                                                ((Product) list.get(0)).getName(),
                                                                ((Product) list.get(0)).getCompany(),
                                                                ((Product) list.get(0)).getAmount(),
                                                                ((Product) list.get(0)).getStorage(), editText_AddToList.getText().toString().toUpperCase().trim() );
                                                    }else{
                                                        //ToDo: Add search's on main db !!
                                                        /** Product is not in any of our Storage place, need to check Main product database */
                                                        editText_BarCode.setText( result.getContents() );
                                                        editText_ProductName.setText( "Not in database!!" );
                                                    }
                                                }
                                            }, result.getContents() );
                                        }
                                    }
                                }, result.getContents());

                            }
                        }
                    }, result.getContents() );
                }
            });





    /** Asynchronous firebase callback to get data outside of the scope */
    private interface FirebaseCallback {
        void onCallback(List<Product> list);
    }

    private void writeData(FirebaseCallback firebaseCallback, String code) {
        /** Variable : use the Product class to compare data on a larger query etc */
        List product_List = new ArrayList();


        /** ShoppingList = N=Norway, S=Sweden, U=User_Own_Made_List { Weekend etc } ??  */
        /** child("ShoppingList").child("N"); */

        /**
         * 1. Get the Product definition on Storage Cold, Freezer, Dry. to switch the list
         *
         * 2. test now only on adding product to the data base with out the search if all ready there !!!
         *  */

        /** Need to be able to switch the list
         *
         * Only search  " Refrigerator " for now as a test!!! keep it simple for now!!!!
         *
         * */
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {


                    for (DataSnapshot child : dataSnapshot.getResult().getChildren() ) {

                        if(child.getValue(Product.class).getBarcode().equals(code)) { //"7310130417832")){

                            product_List.add(new Product(
                                    child.getValue(Product.class).getBarcode(),
                                    child.getValue(Product.class).getName(),
                                    child.getValue(Product.class).getCompany(),
                                    child.getValue(Product.class).getAmount(),
                                    child.getValue(Product.class).getStorage()
                            ) );

                            Log.d("dataSnapshot", "BarCode is in db");
                            break;
                        }else{
                            Log.d("dataSnapshot", "BarCode is not in db");
                        }
                    }
                    /** set Firebase Callback value */
                    firebaseCallback.onCallback(product_List);
                }
            }
        });



    }

//ToDo: Working here Last!!
    /** Test on a Asynchronous firebase callback to get data outside of the scope */
    private void readDataRefrigerator(FirebaseCallback firebaseCallback, String code){

        /** Variable : use the Product class to compare data on a larger query etc */
        List product_List = new ArrayList();


        /** ShoppingList = N=Norway, S=Sweden, U=User_Own_Made_List { Weekend etc } ??  */


        /**
         * 1. Get the Product definition on Storage = Refrigerator, Freezer, DryStorage. to switch the list
         *
         *
         *  */

        /** Need to be able to switch the list  */
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator"); //.child("N");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {


                    for (DataSnapshot child : dataSnapshot.getResult().getChildren() ) {

                        if(child.getValue(Product.class).getBarcode().equals(code)) { //"7310130417832")){

                            product_List.add(new Product(
                                    child.getValue(Product.class).getBarcode(),
                                    child.getValue(Product.class).getName(),
                                    child.getValue(Product.class).getCompany(),
                                    child.getValue(Product.class).getAmount(),
                                    child.getValue(Product.class).getStorage()
                            ) );

                            Log.d("dataSnapshot", "BarCode is in db");
                            break;
                        }else{
                            Log.d("dataSnapshot", "BarCode is not in db");
                        }
                    }
                    /** set Firebase Callback value */
                    firebaseCallback.onCallback(product_List);
                }
            }
        });
    }

    private void readDataFreezer(FirebaseCallback firebaseCallback, String code){

        /** Variable : use the Product class to compare data on a larger query etc */
        List product_List = new ArrayList();

        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Freezer"); //.child("N");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {


                    for (DataSnapshot child : dataSnapshot.getResult().getChildren() ) {

                        if(child.getValue(Product.class).getBarcode().equals(code)) { //"7310130417832")){

                            product_List.add(new Product(
                                    child.getValue(Product.class).getBarcode(),
                                    child.getValue(Product.class).getName(),
                                    child.getValue(Product.class).getCompany(),
                                    child.getValue(Product.class).getAmount(),
                                    child.getValue(Product.class).getStorage()
                            ) );

                            Log.d("dataSnapshot", "BarCode is in db");
                            break;
                        }else{
                            Log.d("dataSnapshot", "BarCode is not in db");
                        }
                    }
                    /** set Firebase Callback value */
                    firebaseCallback.onCallback(product_List);
                }
            }
        });
    }

    private void readDataDryStorage(FirebaseCallback firebaseCallback, String code){

        /** Variable : use the Product class to compare data on a larger query etc */
        List product_List = new ArrayList();

        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("DryStorage"); //.child("N");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {


                    for (DataSnapshot child : dataSnapshot.getResult().getChildren() ) {

                        if(child.getValue(Product.class).getBarcode().equals(code)) { //"7310130417832")){

                            product_List.add(new Product(
                                    child.getValue(Product.class).getBarcode(),
                                    child.getValue(Product.class).getName(),
                                    child.getValue(Product.class).getCompany(),
                                    child.getValue(Product.class).getAmount(),
                                    child.getValue(Product.class).getStorage()
                            ) );

                            Log.d("dataSnapshot", "BarCode is in db");
                            break;
                        }else{
                            Log.d("dataSnapshot", "BarCode is not in db");
                        }
                    }
                    /** set Firebase Callback value */
                    firebaseCallback.onCallback(product_List);
                }
            }
        });
    }


    /** Here user must be on: user interface - chose witch list he/she will use to store the product */
    /** if all ready in shopping list add amount not the hold product */
    private void writeData_to_shoppingList_from_barcode(String code, String name, String company, int amount, String storage, String shoppingList ){
        /** List to use if User can't make a own list or just the 3 default lists: Freezer, Refrigerator, DryStorage **/

        String id = database.getReference().push().getKey();

        Product product = new Product(code, name, company, amount, storage);

        /** N = Norway, Here we must add more list's as a variable  */
        database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("List").child("ShoppingList").child(shoppingList).child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                          Toast.makeText(main_add_product_shopping_list_with_barcode_reader_db.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("EXCEPTION","Failed to add the data: " + e.getMessage());
                    }
                });
    }



    public void addProduct_to_User_database_list(String product_code){

        // Look into from start have a function on setting collectionPath = database path
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

    // this would be on a DM - app maybe
    public void add_user_db_to_master_db(){
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
