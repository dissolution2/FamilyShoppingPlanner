package com.fwa.app.familyshoppingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

/**
 * Inserting of user as a test -- we have one on registration and one with product entry!!
 * */


public class Main_t_recycleview_two_test extends AppCompatActivity {

    private static final String EXCEPTION = "Exception: ";

    private FirebaseAuth mAuth;

    int userGroup_last_index_count;

    int amountX = 0;
    Button btnInsert, btnView;
    EditText name, email, age;
    //DatabaseReference databaseUsers;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    public FirebaseDatabase databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_test_one);
        btnInsert = findViewById(R.id.btninsert);
        btnView = findViewById(R.id.btview);
        name = findViewById(R.id.edtname);
        email = findViewById(R.id.edtemail);
        age = findViewById(R.id.edtage);

        mAuth = FirebaseAuth.getInstance();

        //databaseUsers.getInstance().getReference();//"https://recycleviewfirebase-38aa6-default-rtdb.europe-west1.firebasedatabase.app");
        //databaseUsers.getReference("https://recycleviewfirebase-38aa6-default-rtdb.europe-west1.firebasedatabase.app");

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_t_recycleview_two_test.this,UserList.class));
                finish();
            }
        });


    }

    private void InsertData() {

        String username = name.getText().toString().trim();
        String useremail = email.getText().toString().trim();
        String userage = age.getText().toString().trim();

        String id = database.getReference().push().getKey();

        // works testing***
/*
        DatabaseReference ref = database.getReference("kTDClQTUjRcjWZgxIkg7MtgJytA2")
                .child("Family").
                child("List").
                child("Refrigerator");
*/

        // testing***
/*
        DatabaseReference ref = database.getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //Log.d("CHILDREN COUNT", ": " + dataSnapshot.getChildrenCount());
                    // String Option1=String.valueOf(dataSnapshot.getValue(Long.class));
                    //Log.d("CHILDREN COUNT", "lastKeyIndex: " + lastKeyIndex );

                    Long convertToString = dataSnapshot.getChildrenCount();
                    String convertToInt = String.valueOf(convertToString);

                    try {
                        userGroup_last_index_count = Integer.parseInt(convertToInt);
                    }
                    catch (NumberFormatException e) {
                        userGroup_last_index_count = 0;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.e(ERORR,"data errror " + databaseError.toString());
            }
        });
        userGroup_last_index_count = userGroup_last_index_count++;
        String userGroup_last_Index = String.valueOf(userGroup_last_index_count);
        
        //String id = databaseUsers.getReference().push().getKey();
        //String id =  databaseUsers.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").push().getKey();
*/
        //UserTwo userTwo = new UserTwo(userGroup_last_Index, username, useremail, userage);
        User user = new User(username, useremail, userage);
        //databaseUsers.getReference().child("users").child(id).setValue(user)
        //databaseUsers.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").setValue(userTwo)

        /** this works but we only swap out child 1's data */

        database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("UsersGroup").child(id).setValue(user)

        //database.getReference().child("Users").child(id).setValue(userTwo)
        //database.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").setValue(userTwo)
        //database.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").child(userGroup_last_Index).setValue(userTwo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Main_t_recycleview_two_test.this, "User details Inserted", Toast.LENGTH_SHORT).show();

                            name.setText("");
                            name.requestFocus();
                            email.setText("");
                            age.setText("");

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(EXCEPTION,"Failed to add the data: " + e.getMessage());
                    }
                });

    }
}