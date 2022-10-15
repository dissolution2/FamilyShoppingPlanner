package com.fwa.app.testingViews.testingViews;


import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fwa.app.familyshoppingplanner.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class list_view_test extends AppCompatActivity {

    // creating variables for our list view.
    private ListView coursesLV;

    // creating a new array list.
    ArrayList<String> coursesArrayList;

    // creating a variable for database reference.
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_data_holder_view);

        // initializing variables for listviews.
        //coursesLV = findViewById(R.id.idLVCourses);

        // initializing our array list
        coursesArrayList = new ArrayList<String>();

        // calling a method to get data from
        // Firebase and set data to list view
        initializeListView();
    }

    private void initializeListView() {
        // creating a new array adapter for our list view.
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, coursesArrayList);
        coursesArrayList.add("Robin 2");
        // below line is used for getting reference
        // of our Firebase Database.
        //private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
        //reference = FirebaseDatabase.getInstance().getReference();
        reference = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        // in below line we are calling method for add child event
        // listener to get the child of our database.
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added to
                // our data base and after adding new child
                // we are adding that item inside our array list and
                // notifying our adapter that the data in adapter is changed.
                if(snapshot.exists()){
                    coursesArrayList.add(snapshot.getValue(String.class));
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(list_view_test.this, "onChildAdded snapshot don't exist!!", Toast.LENGTH_LONG).show();
                    Log.d("list_view_test", "snapshot don't exist");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when the new child is added.
                // when the new child is added to our list we will be
                // notifying our adapter that data has changed.
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // below method is called when we remove a child from our database.
                // inside this method we are removing the child from our array list
                // by comparing with it's value.
                // after removing the data we are notifying our adapter that the
                // data has been changed.
                //coursesArrayList.remove(snapshot.getValue(String.class));
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when we move our
                // child in our database.
                // in our code we are note moving any child.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // this method is called when we get any
                // error from Firebase with error.
            }
        });
        // below line is used for setting
        // an adapter to our list view.
        adapter.add("Robin");
        coursesLV.setAdapter(adapter);
    }
}