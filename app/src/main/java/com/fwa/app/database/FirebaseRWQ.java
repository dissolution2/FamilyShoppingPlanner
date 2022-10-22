package com.fwa.app.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fwa.app.classes.Option;
import com.fwa.app.classes.Product;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.fwa.app.testingViews.testingViews.Employee.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRWQ {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database; // = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    //private DatabaseReference databaseReference;
    /** Note: big different on addValueEventListener (onDataChange) vrs onCompleteListener (onComplete) */
    private DatabaseReference ref_refrigerator;
    private DatabaseReference ref_freezer;
    private DatabaseReference ref_dry_storage;
    private DatabaseReference ref_shopping_list_main;
    private DatabaseReference ref_shopping_list_weekend;
    private DatabaseReference ref_shopping_list_div;
    private DatabaseReference ref_user_option; // user_group & user_option

    public FirebaseRWQ(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public DatabaseReference getRef_user_option(){
        return ref_user_option = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").child("Option");
    }

    public DatabaseReference getRef_refrigerator(){
        return database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator");
    }

    public DatabaseReference getRef_user(){
        return database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("UsersGroup");
    }

    public void remove(String remove_key){}
    public void query(String query_string){}

    public String query_user_shopping_list_default_to_use(){

        DatabaseReference ref = getRef_refrigerator();

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {

                }
            }
        });
        return "";
    }

    public void query_user_option_remove_value(){

        DatabaseReference ref = getRef_user_option();
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {
                    ref.getRef().removeValue();
                }
            }
        });
    }

    public Task<Void> updateUserOption(String key, HashMap<String ,Object> hashMap)
    {
        return getRef_user_option().child(key).updateChildren(hashMap);
    }


    /** Works Use: button_menu_fragment_option_product.java */
    public void updateUserOptionOnComplete(String value){

        DatabaseReference ref = getRef_user_option();
        /*
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Option");

         */
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("defaultList", value);
                    ref.updateChildren(updates);
                }
            }
        });
    }


    // fails - loops
    public void updateUserOptionOnEventL(String value){

        DatabaseReference ref = getRef_user_option();
        /*
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Option");
         */
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String, Object> updates = new HashMap<String,Object>();

                    updates.put("DefaultShoppingList", value);
                    //updates.put("homeScore", newscore);
                    //etc

                    ref.updateChildren(updates);
/*
                            List product_List = new ArrayList();

                            for (DataSnapshot child : dataSnapshot.getChildren() ) {

                                //String barcode = child.toString();
                                String barcode = child.getValue().toString();


                                product_List.add(new Option(barcode));
                            }

                            // test for fun!!
                            Log.d("TAG get data","BareCode: " + ((Option)product_List.get(0)).getDefaultShoppingList());
 */

                }else{
                    Log.d("TAG get data","Don't = dataSnapshot exists !!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERORR","data error " + databaseError.toString());
            }
        });



    }

    //ToDo: need more testing - bug control - ok, but more testing.
    /** Use: FragmentMainClass.java */
    public void write_user_shopping_list_default_to_use(String default_list_to_use){

        //String id = database.getReference().push().getKey();
        database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family")
                .child("List").child("Option").child("defaultList").setValue(default_list_to_use).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            //Log.e("TAG User_Option","data write");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("EXCEPTION","Failed to add the data: " + e.getMessage());
                    }
                });

        //return true;
    }

}
