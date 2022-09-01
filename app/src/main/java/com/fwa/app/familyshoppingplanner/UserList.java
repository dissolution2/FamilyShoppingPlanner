package com.fwa.app.familyshoppingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> list;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    //DatabaseReference databaseReference;

    //DatabaseReference ref;
    MyAdapter adapter;
    private FirebaseAuth mAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserList.this, Main_t_recycleview_two_test.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recycleview);

        //ref = database.getReference("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("UserGroup");
        mAuth = FirebaseAuth.getInstance();

        //databaseReference = FirebaseDatabase.getInstance().getReference("UsersGroup");
        //databaseReference = FirebaseDatabase.getInstance().getReference("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup");
        /**
         * D/SNAPSHOT: ?
         * for (DataSnapshot dataSnapshot : snapshot.getChildren() ) { NUll info inn but got two post
         *
         * Log.d =
         * {kTDClQTUjRcjWZgxIkg7MtgJytA2={Family={UsersGroup={name=Robin Larsen, age=46, email=framworkgames@gmail.com}}},
         * PY3GSOln3whU753UZsWqxJEBDkw1={Family={UsersGroup=[{fullName=Robin Larsen, age=46, email=frameworkgames1975@gmail.com}]}}}
         *
         * D/SNAPSHOT: ? com.fwa.app.authapp.UserTwo@cf38709 after putting class Call ?
         * */

        //database.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").child("0");
        //database.getReference();//.child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").child("0");

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this,list);
        recyclerView.setAdapter(adapter);

        //database.getReference().child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").addListenerForSingleValueEvent(new ValueEventListener() {
        database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("UsersGroup").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren() ) {
                        User user = dataSnapshot.getValue(User.class);
                        list.add(user);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

  /*

        //databaseReference.addValueEventListener(new ValueEventListener() {
        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    //Toast.makeText(UserList.this,"? " + snapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                    Log.d("SNAPSHOT","getV UserG? " + snapshot.child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").getValue().toString() );
                    Log.d("SNAPSHOT","getV? " + snapshot.getValue().toString() );
                    Log.d("SNAPSHOT","class snap? " + snapshot.getValue(UserTwo.class) );



                    //for (DataSnapshot dataSnapshot : snapshot.child("kTDClQTUjRcjWZgxIkg7MtgJytA2").child("Family").child("UsersGroup").getChildren() ) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren() ) {
                        UserTwo userTwo = dataSnapshot.getValue(UserTwo.class);
                        list.add(userTwo);

                        Log.d("SNAPSHOT","Class? " + userTwo.getName() + " " + userTwo.getEmail() + " " + userTwo.getAge() );
                        //Toast.makeText(UserList.this,"Name: " + userTwo.getName(), Toast.LENGTH_LONG).show();


                    }
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(UserList.this,"SnapShot Don't Exists", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }
}