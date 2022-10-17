package com.fwa.app.familyshoppingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fwa.app.adapters.UserListAdapter;
import com.fwa.app.classes.User;
import com.fwa.app.testingViews.testingViews.Main_t_recycleview_two_test;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {

    private final String LIST_DEBUG = "Debugging!!";

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ArrayList<User> list;

    boolean isLoading = false;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    //DatabaseReference databaseReference;

    //DatabaseReference ref;
    UserListAdapter adapter;
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

        swipeRefreshLayout = findViewById(R.id.swip);
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
        adapter = new UserListAdapter(this,list);
        recyclerView.setAdapter(adapter);


        /** testing Start */
        loadData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
             public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                 LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                 int totalItem = linearLayoutManager.getItemCount();
                 int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                 if(totalItem< lastVisible+3)
                 {
                     if(!isLoading)
                     {
                         isLoading=true;
                         loadData();
                     }
                 }
             }
         });
                /** testing End */


/* testing new this works but not on realtime change by a other user
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
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

 */
    }

    private void loadData()
    {
        swipeRefreshLayout.setRefreshing(true);

        database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("UsersGroup").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren() ) {

                        Log.d(LIST_DEBUG, " :" + dataSnapshot.getValue().toString() );
                        //Toast.makeText(MainViewGuiShopping.this, "Read Data: " + child.getValue(Product.class).getBarcode() , Toast.LENGTH_LONG).show();
                        User user = dataSnapshot.getValue(User.class);
                        list.add(user);
                    }

                    //adapter.setItems(list);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}