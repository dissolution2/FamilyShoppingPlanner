package com.fwa.app.testingViews.testingViews.fragment.setup;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fwa.app.classes.FamilyMember;
import com.fwa.app.classes.UserData;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_sett_up_family#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_sett_up_family extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String uid_user="";
    private View list_view;
    private Button start_family_btn;
    private ImageButton add_member_1, remove_member_1, add_member_2, remove_member_2,add_member_3, remove_member_3,add_member_4, remove_member_4,add_member_5, remove_member_5,add_member_6, remove_member_6;
    private TextView family_id_group, family_members_txt;
    private EditText family_email_member_1,family_email_member_2,family_email_member_3,family_email_member_4,family_email_member_5,family_email_member_6;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database; // = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    //private String members="";

    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_sett_up_family() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_sett_up_family.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_sett_up_family newInstance(String param1, String param2) {
        fragment_sett_up_family fragment = new fragment_sett_up_family();
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
        list_view = inflater.inflate(R.layout.fragment_sett_up_family, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        //this.getActivity().getSharedPreferences("MyPref",0).edit().clear().commit();
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref",0);
        uid_user = pref.getString("key_name","NO_USER" );
        SharedPreferences.Editor editor = pref.edit();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        family_id_group = list_view.findViewById(R.id.family_id_txt);

        family_email_member_1 = (EditText) list_view.findViewById(R.id.email_txt_1);
        family_email_member_2 = (EditText) list_view.findViewById(R.id.email_txt_2);
        family_email_member_3 = (EditText) list_view.findViewById(R.id.email_txt_3);
        family_email_member_4 = (EditText) list_view.findViewById(R.id.email_txt_4);
        family_email_member_5 = (EditText) list_view.findViewById(R.id.email_txt_5);
        family_email_member_6 = (EditText) list_view.findViewById(R.id.email_txt_6);

        family_email_member_1.setEnabled(false);
        family_email_member_2.setEnabled(false);
        family_email_member_3.setEnabled(false);
        family_email_member_4.setEnabled(false);
        family_email_member_5.setEnabled(false);
        family_email_member_6.setEnabled(false);


        add_member_1 = list_view.findViewById(R.id.imageAddFamilyBtn1);
        remove_member_1 = list_view.findViewById(R.id.imageRemoveFamilyBtn1);

        add_member_2 = list_view.findViewById(R.id.imageAddFamilyBtn2);
        remove_member_2 = list_view.findViewById(R.id.imageRemoveFamilyBtn2);

        add_member_3 = list_view.findViewById(R.id.imageAddFamilyBtn3);
        remove_member_3 = list_view.findViewById(R.id.imageRemoveFamilyBtn3);

        add_member_4 = list_view.findViewById(R.id.imageAddFamilyBtn4);
        remove_member_4 = list_view.findViewById(R.id.imageRemoveFamilyBtn4);

        add_member_5 = list_view.findViewById(R.id.imageAddFamilyBtn5);
        remove_member_5 = list_view.findViewById(R.id.imageRemoveFamilyBtn5);

        add_member_6 = list_view.findViewById(R.id.imageAddFamilyBtn6);
        remove_member_6 = list_view.findViewById(R.id.imageRemoveFamilyBtn6);

        add_member_1.setEnabled(false);
        remove_member_1.setEnabled(false);
        add_member_2.setEnabled(false);
        remove_member_2.setEnabled(false);
        add_member_3.setEnabled(false);
        remove_member_3.setEnabled(false);
        add_member_4.setEnabled(false);
        remove_member_4.setEnabled(false);
        add_member_5.setEnabled(false);
        remove_member_5.setEnabled(false);
        add_member_6.setEnabled(false);
        remove_member_6.setEnabled(false);





        start_family_btn = list_view.findViewById(R.id.start_the_family_btn);

        String dataBaseKey = database.getReference().push().getKey();
        String randomValue = getRandomString(32);
        //family_id_group.setText(mAuth.getCurrentUser().getUid());

        String uid="";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
        }



// ToDo: finish the allowed user / add user to family nr. 2 - > 6. and function delete member for owner and member!!

        /** check user what buttons should be active and query db for family user if the user is owner, If a member add the option to remove him/her from the family **/



        if(!uid_user.equals("NO_USER")){

            start_family_btn.setEnabled(false);
            start_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

            // Owner or Member !! Query Users on -FamilyUid-
            // If Member role - We should only be able to remove the user logged inn not admin.
            // member role - se only there user @ and be able to remove it

            Query getUserRole = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                    .child("FamilyUid").orderByKey().equalTo("role");

            getUserRole.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task_has_role) {
                    if (task_has_role.getResult().exists()) {
                        Log.d("QUERY role", "Exists Data Count -role: " + task_has_role.getResult().getChildrenCount());

                        if (task_has_role.getResult().child("role").getValue().toString().equals("owner")) {

                            Log.d("TAG", "role owner");

                            // Should have all access
                            //start_family_btn.setEnabled(false);
                            //start_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

                            List members = new ArrayList();
                            // We need to get all the member's in the family
                            Query uid_member = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                                    .child("FamilyUid").child("hashFamily");
                            uid_member.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task_has_family) {
                                    if (task_has_family.getResult().exists()) {

                                        Log.d("QUERY hashFamily", "Exists Data Count -hashFamily: " + task_has_family.getResult().getChildrenCount());
                                        for(DataSnapshot child : task_has_family.getResult().getChildren()){
                                            Log.d("QUERY hashFamily", "Value: " + child.getValue());
                                            members.add(child.getValue().toString());

                                        }

                                        switch (members.size()){
                                            case 1:
                                                remove_member_1.setEnabled(true);
                                                remove_member_1.setVisibility(View.VISIBLE);

                                                family_email_member_1.setVisibility(View.VISIBLE);
                                                family_email_member_1.setText(members.get(0).toString());
                                                family_email_member_1.setEnabled(false);
                                                family_email_member_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

                                                add_member_2.setEnabled(true);
                                                add_member_2.setVisibility(View.VISIBLE);
                                                family_email_member_2.setVisibility(View.VISIBLE);
                                                family_email_member_2.setEnabled(false);
                                                family_email_member_2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0ED6B9"))); // active
                                                break;
                                            case 2:
                                                family_email_member_1.setText(members.get(0).toString());
                                                family_email_member_2.setText(members.get(1).toString());



                                                break;
                                            case 3:
                                                family_email_member_1.setText(members.get(0).toString());
                                                family_email_member_2.setText(members.get(1).toString());
                                                family_email_member_3.setText(members.get(2).toString());

                                                break;
                                            case 4:
                                                family_email_member_1.setText(members.get(0).toString());
                                                family_email_member_2.setText(members.get(1).toString());
                                                family_email_member_3.setText(members.get(2).toString());
                                                family_email_member_4.setText(members.get(3).toString());

                                                break;
                                            case 5:
                                                family_email_member_1.setText(members.get(0).toString());
                                                family_email_member_2.setText(members.get(1).toString());
                                                family_email_member_3.setText(members.get(2).toString());
                                                family_email_member_4.setText(members.get(3).toString());
                                                family_email_member_5.setText(members.get(4).toString());
                                                break;
                                            case 6:
                                                family_email_member_1.setText(members.get(0).toString());
                                                family_email_member_2.setText(members.get(1).toString());
                                                family_email_member_3.setText(members.get(2).toString());
                                                family_email_member_4.setText(members.get(3).toString());
                                                family_email_member_5.setText(members.get(4).toString());
                                                family_email_member_6.setText(members.get(5).toString());

                                                break;
                                        }

                                        remove_member_1.setEnabled(true);
                                        remove_member_1.setVisibility(View.VISIBLE);

                                        family_email_member_1.setVisibility(View.VISIBLE);
                                        //family_email_member_1.setText("We have a member");
                                        family_email_member_1.setEnabled(false);
                                        family_email_member_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

                                    } else {
                                        Log.d("QUERY hashFamily", "Don't Exists Error !!! Count hashFamily " + task_has_family.getResult().getChildrenCount());
                                    }
                                    if(task_has_family.isComplete() && task_has_family.getResult().exists() ){

                                    }
                                }
                            });

                        }
                        if (task_has_role.getResult().child("role").getValue().toString().equals("member")) {

                            remove_member_1.setEnabled(true);
                            remove_member_1.setVisibility(View.VISIBLE);

                            family_email_member_1.setVisibility(View.VISIBLE);
                            family_email_member_1.setText(mAuth.getCurrentUser().getEmail());
                            family_email_member_1.setEnabled(false);
                            family_email_member_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive
                        }
                    }
                }
            });
        }



        //Log.d("TAG UID","Uid : " + uid);
        //Log.d("TAG UID","mAuth : " + mAuth.getCurrentUser().getUid());


        // {button or } // .setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive
        // {button or } // .setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0ED6B9"))); // active

        start_family_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                family_email_member_1.setVisibility(View.VISIBLE);
                family_email_member_1.setEnabled(true);

                add_member_1.setVisibility(View.VISIBLE);
                add_member_1.setEnabled(true);

                editor.putString("key_name", mAuth.getCurrentUser().getUid());
                editor.commit();

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
                start_family_btn.setEnabled(false);
                start_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive
            }
        });


        add_member_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




//family_email.getText()
                Log.d("TAG UID","mAuth : " + mAuth.getCurrentUser().getUid());
                Log.d("TAG QUERY","ADD FAMILY BUTTON PRESSED");
/** Test on second db store user strings with Owner !!!  **/

                /** sett string and value as database rules checks on value on the string key. **/


                HashMap<String, Object> allowedMap = new HashMap<>();
                allowedMap.put("Allowed_1", "user1@example.com");
                allowedMap.put("Allowed_2", "user2@example.com");






/** Groups -DB **/ // toDo: test on Allowed change to add database.getReference().push().getKey() // and test on database rule change from Allowed to Allowed_1 etc
                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Allowed_1").setValue(family_email_member_1.getText().toString().trim().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                FamilyMember member = new FamilyMember(family_email_member_1.getText().toString().trim().toLowerCase(), mAuth.getUid(),"false");
                database.getReference("Family")
                        .child("Members").child(database.getReference().push().getKey())
                        .setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){

                                    //ToDo: info ? not sure
                                    //family_members_txt.setText("Family members search on there @mail to be added. Added: " + member.getEmail());


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
                        database.getReference().push().getKey(),family_email_member_1.getText().toString().trim().toLowerCase());

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


                add_member_1.setEnabled(false);
                //add_member_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive
                add_member_1.setVisibility(View.GONE);

                remove_member_1.setEnabled(true);
                remove_member_1.setVisibility(View.VISIBLE);

                family_email_member_1.setEnabled(false);
                family_email_member_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

                add_member_2.setEnabled(true);
                add_member_2.setVisibility(View.VISIBLE);

                family_email_member_2.setEnabled(true);
                family_email_member_2.setVisibility(View.VISIBLE);

                family_email_member_2.setFocusable(true);
                family_email_member_2.requestFocus();

                Toast.makeText(getActivity(), "Family Member Added", Toast.LENGTH_SHORT).show();

            }
        });


        //ToDo: error on adding a second member : On users FamilyUid - hasFamily got changed from 1. user to the 2. user // we want to add the 2 user etc.
        //ToDo: Groups on adding a second member : On owner Uid - Allowed: got all so changed to the 2. user etc // again we want to add the 2 user etc.


        add_member_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




//family_email.getText()
                Log.d("TAG UID","mAuth : " + mAuth.getCurrentUser().getUid());
                Log.d("TAG QUERY","ADD FAMILY BUTTON PRESSED");
/** Test on second db store user strings with Owner !!!  **/

                /** sett string and value as database rules checks on value on the string key. **/

/** Groups -DB **/
                database.getReference("Groups").child(mAuth.getCurrentUser().getUid())
                        .child("Allowed_2").setValue(family_email_member_2.getText().toString().trim().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                FamilyMember member = new FamilyMember(family_email_member_2.getText().toString().trim().toLowerCase(), mAuth.getUid(),"false");
                database.getReference("Family")
                        .child("Members").child(database.getReference().push().getKey())
                        .setValue(member).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isComplete()){

                                    //ToDo: info ? not sure
                                    //family_members_txt.setText("Family members search on there @mail to be added. Added: " + member.getEmail());


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });
/** Users -DB **/
                /* don't use this
                UserData userData = new UserData(mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getUid(),
                        "false","owner","Main","",null,
                        database.getReference().push().getKey(),family_email_member_2.getText().toString().trim().toLowerCase());
                */

                // sett: UserUid ? mAuth.getCurrentUser().getUid()
                // sett: FamilyUid ? mAuth.getCurrentUser().getUid()
                database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .child("FamilyUid").child("hashFamily").child(database.getReference().push().getKey()).setValue(family_email_member_2.getText().toString().trim().toLowerCase()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY", "Exceptiong Middle task " + e);
                            }
                        });

                add_member_2.setEnabled(false);
                //add_member_1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive
                add_member_2.setVisibility(View.GONE);

                remove_member_2.setEnabled(true);
                remove_member_2.setVisibility(View.VISIBLE);

                family_email_member_2.setEnabled(false);
                family_email_member_2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

                add_member_3.setEnabled(true);
                add_member_3.setVisibility(View.VISIBLE);

                family_email_member_3.setEnabled(true);
                family_email_member_3.setVisibility(View.VISIBLE);

                family_email_member_3.setFocusable(true);
                family_email_member_3.requestFocus();

                Toast.makeText(getActivity(), "Family Member Added", Toast.LENGTH_SHORT).show();

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

        return list_view;
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