package com.fwa.app.product.manualy.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fwa.app.classes.Product;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Main_t_manualy_add_products_to_db extends AppCompatActivity implements View.OnClickListener{

    private static final String ON_COMPLETE = "Successfully: ";
    private static final String EXCEPTION = "Exception: ";
    private static final String TEST = "TEST STRING: ";


    private Button logout;

    private EditText editText_Barcode,editText_Product,editText_Company,editText_ProductAmount,editText_Storage,editText_current_user;
    private Button addDataButton, getCurrentUserButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebase_FireStore_Product_DB = FirebaseFirestore.getInstance();
    private FirebaseFirestore firebase_FireStore_User_add_to_DB = FirebaseFirestore.getInstance();
    //private DocumentReference docRef;

    private Product from_DB, from_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_add_product_to_list_db);

        mAuth = FirebaseAuth.getInstance();
        editText_Barcode = (EditText) findViewById(R.id.editTextBarcode );
        editText_Product = (EditText) findViewById(R.id.editTextProduct);
        editText_Company = (EditText) findViewById(R.id.editTextCompany);
        editText_ProductAmount = (EditText) findViewById(R.id.editTextProductAmount);
        editText_Storage = (EditText) findViewById(R.id.editTextStorage);
        editText_current_user = (EditText) findViewById(R.id.editTextCurrent_user);

        addDataButton = (Button) findViewById(R.id.add_dataBtn);
        addDataButton.setOnClickListener(this);

        getCurrentUserButton = (Button) findViewById(R.id.check_CurrentUserBtn);
        getCurrentUserButton.setOnClickListener(this);
    }

    private void getCurrentUser() {

        try{
            editText_current_user.setText( mAuth.getCurrentUser().getEmail() );//.getUid() );
            //Log.d(TEST,"Getting Current User: " + mAuth.getCurrentUser().getUid() );
        }
        catch (Exception e) {
            //editTextCurrent_user.setText( e.getMessage().trim() );
            Log.e(EXCEPTION,"Getting Current User: " + e.getMessage());
        }
    }

    /** Add to Freezer - Refrigerator - DryStorage Manual add product to list - test - */
    private void insertDataToList(){
        String list ="";
        String barcode = editText_Barcode.getText().toString().trim();
        String name = editText_Product.getText().toString().trim();
        String company = editText_Company.getText().toString().trim();
        String amount = editText_ProductAmount.getText().toString().trim();
        String storage =  editText_Storage.getText().toString().trim();

        String id = database.getReference().push().getKey();

        // this is only because i got Long from the snapshot ??!!! might take it bak to String!!!!
        int amountX;
        try {
            amountX = Integer.parseInt(amount);
        }
        catch (NumberFormatException e) {
            amountX = 0;
        }
        Product product = new Product(barcode, name, company, amountX, storage);
        if(!editText_Barcode.getText().toString().trim().isEmpty() &&
                !editText_Product.getText().toString().trim().isEmpty() &&
                !editText_ProductAmount.getText().toString().trim().isEmpty() &&
                !editText_Storage.getText().toString().trim().isEmpty()) {

            //String value = editText_Storage.getText().toString().toLowerCase().trim();

            if(editText_Storage.getText().toString().toLowerCase().trim().equals("f") == true ||
                    editText_Storage.getText().toString().toLowerCase().trim().equals("c") == true ||
                    editText_Storage.getText().toString().toLowerCase().trim().equals("d") == true ) {

                switch (editText_Storage.getText().toString().toLowerCase().trim()) {
                    case "f":
                        list = "Freezer";
                        Toast.makeText(Main_t_manualy_add_products_to_db.this, "Storage f: " + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                        break;
                    case "c":
                        list = "Refrigerator";
                        Toast.makeText(Main_t_manualy_add_products_to_db.this, "Storage c: " + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                        break;
                    case "d":
                        list = "DryStorage";
                        Toast.makeText(Main_t_manualy_add_products_to_db.this, "Storage d: " + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                        break;
                }
                database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("List").child(list)
                        .child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    //Toast.makeText(ShopingListView.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();
                                    Log.d("TODATABASE", "Added data to database");
                                    editText_current_user.setText("Added Data To DB");
                                    editText_Barcode.setText("");
                                    editText_Product.setText("");
                                    editText_Company.setText("");
                                    editText_ProductAmount.setText("");
                                    editText_Storage.setText("");
                                    editText_Barcode.requestFocus();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(EXCEPTION, "Failed to add the data: " + e.getMessage());
                            }
                        });
            }else{
                Toast.makeText(Main_t_manualy_add_products_to_db.this, "Storage must be f or c or d" + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
            }


        }else{
            Toast.makeText(Main_t_manualy_add_products_to_db.this, "BareCode, Product, Amount and Storage are empty!!", Toast.LENGTH_LONG).show();
        }
    }


    // test 01 - general test - How to !!!  - Successful got doc exist -
    private void getDocumentOne_test(){

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("main_product_db").document("z5Blg8RNWFUd3X1dKv6q");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Log.d("Document", "Suc");
                        Toast.makeText(Main_t_manualy_add_products_to_db.this, "Successful to get data!!", Toast.LENGTH_LONG).show();
                    }else{
                        Log.d("Doument", "fail");
                        Toast.makeText(Main_t_manualy_add_products_to_db.this, "Failed to get data!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    // test 02 - getting { main DB || user DB }
    /**
     * 2022-08-31 04:44:36.563 5819-5819/com.fwa.app.authapp D/TAG: 123456432 => {product=Juce, company=Milba, storage=Cold}
     * 2022-08-31 04:44:36.563 5819-5819/com.fwa.app.authapp D/TAG: 1234567 => {product=Milk, company=Tine, storage=Cold}
     *
     *  1. scan code -> Inn User_DB {product made||scanned be for} => add_to_user_DB
     *  2. Else Unknown to User_DB then => query main_DB if/is in main_DB getData() -> add_to_user !!??
     *  3. If/else Product is unknown to main_db {barcode =null} add product to user_add_to_db.
     *  4. set flag to DB_Master -> very fy the Product if/is good -> add to main_DB.
     *
     * */
    // Use String path to the db !!??
    // Firebase Cloud
    private void getDocumentTwo_test() {

        firebase_FireStore_Product_DB.collection("main_product_db") //path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task_one) {
                        if (task_one.isSuccessful()) {
                            Toast.makeText(Main_t_manualy_add_products_to_db.this, "Successful main_product_db", Toast.LENGTH_LONG).show();

                            /** First query Successful get now user_db */
                            firebase_FireStore_User_add_to_DB.collection("user_add_to_main_db").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task_two) {
                                    if (task_two.isSuccessful()) {
                                        /** test if result from task_one and task_two Products match up with bareCode !!*/

                                        Toast.makeText(Main_t_manualy_add_products_to_db.this, "Successful user_add_to_db", Toast.LENGTH_LONG).show();

                                        query_FireCloud_Main_DB_and_User_DB_Check(task_one, task_two);

                                    } else {
                                        Log.w("TAG", "Error getting documents.", task_two.getException());
                                        //Toast.makeText(Main_t_manualy_add_products_to_db.this, "Error to get data from user_db!!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Log.w("TAG", "Error getting documents.", task_one.getException());
                            //Toast.makeText(Main_t_manualy_add_products_to_db.this, "test_two Error to get data!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void getDocumentOne_test_string_search(){

        firebase_FireStore_Product_DB.collection("main_product_db") //path)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task_one) {
                        if (task_one.isSuccessful()) {
                            //Toast.makeText(Main_t_manualy_add_products_to_db.this, "Successful main_product_db", Toast.LENGTH_LONG).show();

                            Log.d("TAG DB", "searching for Milk");
                            for (QueryDocumentSnapshot document_one : task_one.getResult()) {

                                if(document_one.get("name").toString().contains("M") ){ // .equals("Melk")
                                    Log.d("TAG DB", document_one.getId() + " => " + document_one.getData());
                                }
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task_one.getException());
                            //Toast.makeText(Main_t_manualy_add_products_to_db.this, "test_two Error to get data!!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void query_FireCloud_Main_DB_and_User_DB_Check(Task<QuerySnapshot> task_one, Task<QuerySnapshot> task_two){

        for (QueryDocumentSnapshot document_one : task_one.getResult()) {
            Log.d("TAG DB", document_one.getId() + " => " + document_one.getData());

            for (QueryDocumentSnapshot document_two : task_two.getResult()) {
                Log.d("TAG USER", document_two.getId() + " => " + document_two.getData());


                if(document_one.getId().equals(document_two.getId()) ){
                    Log.d("TAG", "Check on to Doc are ==");
                    Log.d("TAG", "Doc 123.. are == to: " + document_one.getId() + " => " + document_one.getData());
                    Log.d("TAG", "Doc 123.. are == to: " + document_two.getId() + " => " + document_two.getData());

                    editText_Barcode.setText(document_one.getId());

                    // sett the data to a HashMap<> if we want the data !!!
                    //editTextAddProductName.setText(document_one.getData().getClass().getName());
                    //productAmount.setText("");
                }else{
                    Log.d("TAG", "Check on to Doc are !=" + document_one.get("barecode"));
                }

                if(document_one.get("barecode").equals(document_two.get("barecode")) ){
                    Log.d("TAG", "Check on to Doc are ==");
                    Log.d("TAG", "Doc 123.. are == to: " + document_one.getId() + " => " + document_one.getData());
                    Log.d("TAG", "Doc 123.. are == to: " + document_two.getId() + " => " + document_two.getData());
                }

            }


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_dataBtn:
                insertDataToList();
                break;
            case R.id.check_CurrentUserBtn:
                getCurrentUser();
                /** Cound FireStore query test main_product_db & user_add_to_main_db && == Check !! ok. */
                //getDocumentOne_test(); // ok
                //getDocumentTwo_test(); // ok
                getDocumentOne_test_string_search();
                break;
        }
    }


}