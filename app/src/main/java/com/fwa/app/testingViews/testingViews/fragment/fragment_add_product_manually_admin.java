package com.fwa.app.testingViews.testingViews.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fwa.app.classes.Product;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.product.manualy.add.Main_t_manualy_add_products_to_db;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_add_product_manually_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_add_product_manually_admin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View list_view;
    //private String user_uid="";

    private EditText editText_Barcode,editText_Product,editText_Company,editText_ProductAmount,editText_Storage, editText_Quantity, editText_current_user;
    private Button addDataButton, getCurrentUserButton, addCollectionButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    private String family_uid="";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebase_FireStore_Product_DB = FirebaseFirestore.getInstance();
    private FirebaseFirestore firebase_FireStore_User_add_to_DB = FirebaseFirestore.getInstance();
    //private DocumentReference docRef;

    private Product from_DB, from_User;
    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_add_product_manually_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_add_product_manually_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_add_product_manually_admin newInstance(String param1, String param2) {
        fragment_add_product_manually_admin fragment = new fragment_add_product_manually_admin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        list_view = inflater.inflate(R.layout.fragment_add_product_manually_admin, container, false);
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        family_uid = pref.getString("key_name","NO_USER");

        //family_uid = pref.getString("key_name", "No data!!");
        mAuth = FirebaseAuth.getInstance();
        editText_Barcode = (EditText) list_view.findViewById(R.id.editTextBarcode );
        editText_Product = (EditText) list_view.findViewById(R.id.editTextProduct);
        editText_Company = (EditText) list_view.findViewById(R.id.editTextCompany);
        editText_ProductAmount = (EditText) list_view.findViewById(R.id.editTextProductAmount);
        editText_Storage = (EditText) list_view.findViewById(R.id.editTextStorage);
        editText_Quantity = (EditText) list_view.findViewById(R.id.editTextProductQuantity);
        //editText_current_user = (EditText) list_view.findViewById(R.id.editTextCurrent_user);

        addDataButton = (Button) list_view.findViewById(R.id.add_dataBtn);
        addDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String list ="";
                String barcode = editText_Barcode.getText().toString().trim();
                String name = editText_Product.getText().toString().toLowerCase().trim();
                String company = editText_Company.getText().toString().toLowerCase().trim();
                String amount = editText_ProductAmount.getText().toString().trim();
                String quantity = editText_Quantity.getText().toString().trim();
                String storage =  editText_Storage.getText().toString().toLowerCase().trim();

                String id = database.getReference().push().getKey();

                // this is only because i got Long from the snapshot ??!!! might take it bak to String!!!!
                int amountX;
                try {
                    amountX = Integer.parseInt(quantity);
                }
                catch (NumberFormatException e) {
                    amountX = 0;
                }

                if(!editText_Barcode.getText().toString().trim().isEmpty() &&
                        !editText_Product.getText().toString().trim().isEmpty() &&
                        !editText_ProductAmount.getText().toString().trim().isEmpty() &&
                        !editText_Quantity.getText().toString().trim().isEmpty() &&
                        !editText_Storage.getText().toString().isEmpty()) {

                    //Product(String code, String name, String company, String amount, int quantity, List<String> storage)
                    List<String> storage_list = new ArrayList<>();
                    String string_array[] = storage.split(" ");
                    Log.d("SPLIT","string split: " + string_array.toString());
                    for(int i=0; i< string_array.length;i++){
                        storage_list.add( string_array[i] );
                        Log.d("ARRAYLIST","ArrayList: " + storage_list.get(i) );
                    }

                    Product product = new Product(barcode, name, company, amount, amountX, storage_list);
                    //String value = editText_Storage.getText().toString().toLowerCase().trim();

                   // if(editText_Storage.getText().toString().toLowerCase().trim().equals("f") == true ||
                   //         editText_Storage.getText().toString().toLowerCase().trim().equals("c") == true ||
                   //         editText_Storage.getText().toString().toLowerCase().trim().equals("d") == true ) {

                        switch (storage_list.get(0)) {//editText_Storage.getText().toString().toLowerCase().trim()) {
                            case "f":
                                list = "Freezer";
                                Toast.makeText(getActivity(), "Storage f: " + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                                break;
                            case "c":
                                list = "Refrigerator";
                                Toast.makeText(getActivity(), "Storage c: " + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                                break;
                            case "d":
                                list = "Dry";
                                Toast.makeText(getActivity(), "Storage d: " + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                                break;
                        }
                        // old
                        //database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("List").child(list)
                        //        .child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                        database.getReference("Groups").child(family_uid)//mAuth.getCurrentUser().getUid())
                                .child("Data").child("List").child(list)
                                .child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            //Toast.makeText(ShopingListView.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();
                                            Log.d("TODATABASE", "Added data to database");
                                            //editText_current_user.setText("Added Data To DB");
                                            editText_Barcode.setText("");
                                            editText_Product.setText("");
                                            editText_Company.setText("");
                                            editText_ProductAmount.setText("");
                                            editText_Quantity.setText("");
                                            editText_Storage.setText("");
                                            editText_Barcode.requestFocus();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.e("EXCEPTION", "Failed to add the data: " + e.getMessage());
                                    }
                                });
                    //}else{
                    //    Toast.makeText(getActivity(), "Storage must be f or c or d" + editText_Storage.getText().toString().toLowerCase().trim(), Toast.LENGTH_LONG).show();
                    //}


                }else{
                    Toast.makeText(getActivity(), "BareCode, Product, Amount and Storage are empty!!", Toast.LENGTH_LONG).show();
                }
            }
        });

/** Admin db Input Product **/
        addCollectionButton = (Button)list_view.findViewById(R.id.add_data_collectionBtn);
        addCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /** Test One main db document**/ //ok
                //admin_add_document_product();

                /** Test Two user db document **/
                user_search_user_db_product_add_to_user_storage_list();

            }
        });

        return list_view;
    }

    // [Test add product One] -ok-
    public void admin_add_document_product(){
        String barcode = editText_Barcode.getText().toString().trim();
        String name = editText_Product.getText().toString().toLowerCase().trim();
        String company = editText_Company.getText().toString().toLowerCase().trim();
        String amount = editText_ProductAmount.getText().toString().trim();
        String quantity = editText_Quantity.getText().toString().trim();
        String storage =  editText_Storage.getText().toString().toLowerCase().trim();

        // this is only because i got Long from the snapshot ??!!! might take it bak to String!!!!
        int amountX;
        try {
            amountX = Integer.parseInt(quantity);
        }
        catch (NumberFormatException e) {
            amountX = 0;
        }
        if(!editText_Barcode.getText().toString().trim().isEmpty() &&
                !editText_Product.getText().toString().trim().isEmpty() &&
                !editText_ProductAmount.getText().toString().trim().isEmpty() &&
                !editText_Quantity.getText().toString().trim().isEmpty() &&
                !editText_Storage.getText().toString().trim().isEmpty()) {

            List<String> storage_list = new ArrayList<>();
            String string_array[] = storage.split(" ");
            for(int i=0; i< string_array.length;i++){
                storage_list.add( string_array[i] );
            }

            Product product = new Product(barcode, name, company, amount, amountX, storage_list);

            DocumentReference docRef = FirebaseFirestore.getInstance().collection("main_product_db").document(editText_Barcode.getText().toString().trim());
            docRef.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //Toast.makeText(ShopingListView.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();
                        Log.d("TODATABASE", "Added data to database");
                        //editText_current_user.setText("Added Data To DB");
                        editText_Barcode.setText("");
                        editText_Product.setText("");
                        editText_Company.setText("");
                        editText_ProductAmount.setText("");
                        editText_Quantity.setText("");
                        editText_Storage.setText("");
                        editText_Barcode.requestFocus();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    /** search main product db for the product exist */
    public void user_search_main_db_product_add_to_user_storage_list(){

    }
    /** user have to add it manually if it not exist in main product db */
    /** user_add_to_main_db -search on uid by the user family */
    public void user_search_user_db_product_add_to_user_storage_list(){
        String barcode = editText_Barcode.getText().toString().trim();
        String name = editText_Product.getText().toString().toLowerCase().trim();
        String company = editText_Company.getText().toString().toLowerCase().trim();
        String amount = editText_ProductAmount.getText().toString().trim();
        String quantity = editText_Quantity.getText().toString().trim();
        String storage =  editText_Storage.getText().toString().toLowerCase().trim();

        // this is only because i got Long from the snapshot ??!!! might take it bak to String!!!!
        int amountX;
        try {
            amountX = Integer.parseInt(quantity);
        }
        catch (NumberFormatException e) {
            amountX = 0;
        }
        if(!editText_Barcode.getText().toString().trim().isEmpty() &&
                !editText_Product.getText().toString().trim().isEmpty() &&
                !editText_ProductAmount.getText().toString().trim().isEmpty() &&
                !editText_Quantity.getText().toString().trim().isEmpty() &&
                !editText_Storage.getText().toString().trim().isEmpty()) {

            List<String> storage_list = new ArrayList<>();
            String string_array[] = storage.split(" ");
            for(int i=0; i< string_array.length;i++){
                storage_list.add( string_array[i] );
            }

            Product product = new Product(barcode, name, company, amount, amountX, storage_list);

            DocumentReference docRef = FirebaseFirestore.getInstance().collection("user_add_to_main_db").document(editText_Barcode.getText().toString().trim());
            docRef.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        //Toast.makeText(ShopingListView.this, "Successfully added data to database!!", Toast.LENGTH_LONG).show();
                        Log.d("TODATABASE", "Added data to database");
                        //editText_current_user.setText("Added Data To DB");
                        editText_Barcode.setText("");
                        editText_Product.setText("");
                        editText_Company.setText("");
                        editText_ProductAmount.setText("");
                        editText_Quantity.setText("");
                        editText_Storage.setText("");
                        editText_Barcode.requestFocus();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }







}