package com.fwa.app.testingViews.testingViews.fragment.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fwa.app.classes.FamilyMember;
import com.fwa.app.classes.UserData;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class FirstTimeRun_test extends AppCompatActivity {
    private Button start_family_btn, search_family_members_btn,add_user_btn, search_family;
    private TextView family_id_group, family_members_txt;
    private EditText family_email;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database; // = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first_time_run);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        family_id_group = findViewById(R.id.family_id_txt);
        family_email = (EditText) findViewById(R.id.email_txt_1);
        family_email.setEnabled(false);

        family_members_txt = (TextView) findViewById(R.id.family_members_txt);
        family_members_txt.setEnabled(false);

        search_family_members_btn = findViewById(R.id.search_the_family_btn);
        search_family_members_btn.setEnabled(false);

        add_user_btn = findViewById(R.id.add_to_family_btn);
        add_user_btn.setEnabled(false);

        start_family_btn = findViewById(R.id.start_the_family_btn);

        String dataBaseKey = database.getReference().push().getKey();
        String randomValue = getRandomString(32);
        family_id_group.setText(mAuth.getCurrentUser().getUid());

        String uid="";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }


        Log.d("TAG UID","Uid : " + uid);
        Log.d("TAG UID","mAuth : " + mAuth.getCurrentUser().getUid());




        start_family_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family_email.setEnabled(true);
                family_members_txt.setEnabled(true);
                add_user_btn.setEnabled(true);

                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Owner").setValue(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });

                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Data").child("List").child("ShoppingList").setValue("Main").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TAG ID STRING", "Sett value 32 random string to Group Id = value ");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong first task " + e);
                            }
                        });




/*
                // works but index wrong!! on query testing new structure again....
                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Owner").setValue(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });

                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Data").child("List").child("ShoppingList").setValue("Main").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("TAG ID STRING", "Sett value 32 random string to Group Id = value ");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong first task " + e);
                            }
                        });
*/
            }
        });

        search_family_members_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /** User frameworkgames1975@gmail.com **/

    Log.d("TAG QUERY","Exists Don't // we sett a value to test ");

    add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//family_email.getText()
                Log.d("TAG UID","mAuth : " + mAuth.getCurrentUser().getUid());
                Log.d("TAG QUERY","ADD FAMILY BUTTON PRESSED");
/** Test on second db store user strings with Owner !!!  **/

                /** sett string and value as database rules checks on value on the string key. **/

/** Groups -DB **/
                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Allowed").setValue(family_email.getText().toString().trim().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });
/** Family -DB **/
                FamilyMember member = new FamilyMember(family_email.getText().toString().trim().toLowerCase(), mAuth.getUid(),"false");
                database.getReference("Family")
                        .child("Members").child(database.getReference().push().getKey())
                        .setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){

                                    family_members_txt.setText("Family members search on there @mail to be added. Added: " + member.getEmail());


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });
/** Users -DB **/
                UserData userData = new UserData(mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getUid(),
                        "false","owner","Main","",null,
                        database.getReference().push().getKey(),family_email.getText().toString().trim().toLowerCase());

                // sett: UserUid ? mAuth.getCurrentUser().getUid()
                // sett: FamilyUid ? mAuth.getCurrentUser().getUid()
                database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("FamilyUid").setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });
 /*

                database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("FamilyUid").setValue(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });


                // sett: ShoppingDefaultList
                database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("Option").setValue("Main").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });


                // sett: Email mAuth.getCurrentUser().getEmail()
                database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("Email").setValue(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });
                // sett
                database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("Members").child(database.getReference().push().getKey()).setValue(family_email.getText().toString().trim().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });
  */
            }
     });




        /*
        Query ref = database.getReference().child("Groups").child("oGm09XrH8tekko4bB0No7Qyp2oM2").child("Data");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            Log.d("TAG QUERY","Exists Data is: " + dataSnapshot.toString() );
                            Log.d("TAG QUERY","On user: " + mAuth.getCurrentUser().getEmail() );


                            for(DataSnapshot child : dataSnapshot.getChildren()){

                                Log.d("TAG Child", "key: " + child.getKey() + " Value: " + child.getValue());

                                if(child.getValue().equals(family_email.getText().toString())){
                                    Log.d("TAG QUERY", "Email is found the key is: " + child.getKey() );
                                }
                                break;

                            }


                        }else {
                            Log.d("TAG QUERY","Exists Don't // we sett a value to test ");
                            Log.d("TAG QUERY","Child count: " + dataSnapshot.getChildrenCount());

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("ERORR 1","data error " + databaseError.toString());
                    }
                });
         */


        search_family = findViewById(R.id.search_the_family_btn);
        search_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family_id_group.setText(mAuth.getCurrentUser().getUid());
                family_email.getText().toString();


                //database.ref("Groups/${groupId}");


             }
        });

 }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
