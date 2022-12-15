package com.fwa.app.testingViews.testingViews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class main_app_pre_check_user extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app_pre_cehck_user);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        /** Check if user id Uid have a User in the database  Yes direct him to main app,
         *  else we direct him/her to Option Menu
         ***/
// On user gets first the uid group
        Query user = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("FamilyUid").orderByKey().equalTo("role");//.child("role"); // null // .child("hashFamily") // ok

        user.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    Log.d("TAG QUERY","We got info");

                    Log.d("TAG Q", "result: " + task.getResult().getChildrenCount());
                    for(DataSnapshot child : task.getResult().getChildren()){
                        Log.d("RES","Key: " + child.getKey() + " Value: " + child.getValue());


                        if(child.getValue().equals("member")){
                            // send data to next fragment - > member user
                            Log.d("TAG Member","we got a member");
                            break;
                        }
                        if(child.getValue().equals("owner")){
                            // send data to next fragment - > owner user
                            Log.d("TAG Owner","we got a Owner");
                            break;
                        }



                    }

                    /** key = role, if value = member, search string is on hashFamily value  **/
                    /** key = role, if value = owner, search string is on uid value  **/

                    /** key = Not found = No owner or No member . direct to sett up screen  **/


                    // Directly to main app screen //
                    //startActivity(new Intent(main_app_pre_check_user.this, FragmentMainClass.class));
                }else {
                    Log.d("TAG Q","No result!!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG QUERY","Failed " + e);
            }
        });



/*   /// this is a search to use later on.
        Query groups = database.getReference("Groups").child("oGm09XrH8tekko4bB0No7Qyp2oM2")
                .child("Data").child("List");

        groups.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    Log.d("TAG QUERY","We got info");

                    Log.d("TAG Q", "result: " + task.getResult().getChildrenCount());
                    for(DataSnapshot child : task.getResult().getChildren()){
                        Log.d("RES","Key: " + child.getKey() + " Value: " + child.getValue());

                    }
                    // Directly to main app screen //
                    //startActivity(new Intent(main_app_pre_check_user.this, FragmentMainClass.class));
                }else {
                    Log.d("TAG Q","No result!!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG QUERY","Failed " + e);
            }
        });
*/


/*
        DatabaseReference refnew = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = refnew.child("Users").child(mAuth.getCurrentUser().getUid()).child("FamilyUid").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG Q", "in Q ");

                if(dataSnapshot.exists()){
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        Log.d("TAG","Key: " + appleSnapshot.getKey() + " Value: " + appleSnapshot.getValue() );
                    }
                }

                //
//                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }

                 //


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
*/

        //DatabaseReference refnew = FirebaseDatabase.getInstance().getReference();
        //Query applesQuery = refnew.child("Users").child("FamilyUid").orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
/*
        java.lang.Exception: Index not defined,

        add ".indexOn": "uid", for path "/Users/oGm09XrH8tekko4bB0No7Qyp2oM2/FamilyUid", to the rules
*/
        /*
        Query ref = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("FamilyUid").orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()); // .child("uid").equalTo(mAuth.getCurrentUser().getUid()); // No result !!
                //.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid()); //.index wrong !!
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    Log.d("TAG QUERY","We got info");

                    Log.d("TAG Q", "result: " + task.getResult().getChildrenCount());
                    for(DataSnapshot child : task.getResult().getChildren()){
                        Log.d("RES","Key: " + child.getKey() + " Value: " + child.getValue());

                    }
                    // Directly to main app screen //
                    startActivity(new Intent(main_app_pre_check_user.this, FragmentMainClass.class));
                }else {
                        Log.d("TAG Q","No result!!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG QUERY","Failed " + e);
            }
        });

         */
        /*
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //  User is a member or a owner of a family sett Intent to main App.
                    Log.d("TAG Intent","sett to AppMainScreen");
                    // Directly to main app screen //
                    startActivity(new Intent(main_app_pre_check_user.this, FragmentMainClass.class));
                }else{
                    // No user so sett Intent to Sett up App.
                    Log.d("TAG Intent","sett to AppSettUp!!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
      */
    }
}
