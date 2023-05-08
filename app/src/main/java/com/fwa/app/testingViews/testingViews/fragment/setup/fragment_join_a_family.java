package com.fwa.app.testingViews.testingViews.fragment.setup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fwa.app.classes.FamilyMember;
import com.fwa.app.classes.UserData;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_join_a_family#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_join_a_family extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View list_view;
    private Button add_btn, search_btn;
    private TextView family_id, family_email, family_info_txt;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database; // = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_join_a_family() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_join_a_family.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_join_a_family newInstance(String param1, String param2) {
        fragment_join_a_family fragment = new fragment_join_a_family();
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
        list_view = inflater.inflate(R.layout.fragment_join_a_family, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        family_id = list_view.findViewById(R.id.family_id_txt);
        family_email = list_view.findViewById(R.id.email_txt);
        family_info_txt = list_view.findViewById(R.id.family_info_txt);

        add_btn = list_view.findViewById(R.id.add_to_family_btn);
        add_btn.setEnabled(false);
        List members_list = new ArrayList();
        List member_found = new ArrayList();





        search_btn = list_view.findViewById(R.id.search_the_family_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG QUERY", "User is: " + mAuth.getCurrentUser().getEmail());



                /**
                 * 1 Query your email to se where you belong - Family
                 * 2 add the Uid string to your profile db // we can !! mark it for delete if all members have join in !!
                 * 3 all query's would have to have a variable the will be set at the logg inn from the db same as default shopping list !!!
                 *
                 * **/

                // Error on setting Class FamilyMember !! convert object of type string to class FamilyMember etc
                Log.d("TAG QUERY", "On user: " + mAuth.getCurrentUser().getEmail());
                Query ref3 = database.getReference("Family").child("Members");
                ref3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.getResult().exists()) {
                            Log.d("TAG QUERY", "Exists Data Count is: " + task.getResult().getChildrenCount());


                            for (DataSnapshot child : task.getResult().getChildren()) {
                                Log.d("TAG TASK", "Key: " + child.getKey() + " Value: " + child.getValue());

                                members_list.add(new FamilyMember(
                                        child.getValue(FamilyMember.class).getEmail(),
                                        child.getValue(FamilyMember.class).getUid(),
                                        child.getValue(FamilyMember.class).getDelete()
                                ));
                            }
                            for (int i = 0; i < members_list.size(); i++) {
                                //ToDo: instead of user typing in there mail address we should just use the loggInn email as that is what we want any way!!!!!
                                if (((FamilyMember) members_list.get(i)).getEmail().equals(mAuth.getCurrentUser().getEmail())) { //family_email.getText().toString().trim().toLowerCase())) {

                                    Log.d("TAG == ", "db email: " + ((FamilyMember) members_list.get(i)).getEmail() + " asked email: " + family_email.getText().toString().trim().toLowerCase());
                                    Log.d("TAG == ", "i: " + i);

                                    member_found.add((members_list.get(i)));

                                    add_btn.setEnabled(true);
                                    family_info_txt.setText("Found: \n" +
                                            "Email: " + ((FamilyMember) members_list.get(i)).getEmail() + "\n" +
                                            "Uid: " + ((FamilyMember) members_list.get(i)).getUid() + "\n" +
                                            "Press ADD TO FAMILY to be added!!");
                                    break;
                                }else{ //if(i == members_list.size() ){//&& ((FamilyMember) members_list.get(i)).getEmail().equals(family_email.getText().toString().trim().toLowerCase())){
                                    family_info_txt.setText("Not Found: \n" + mAuth.getCurrentUser().getEmail() ); //family_email.getText().toString().trim().toLowerCase());
                                }
                            }
                        } else {
                            Log.d("TAG QUERY", "Exists Data is Count: " + task.getResult().getChildrenCount());
                            family_info_txt.setText("Not Found: \n" + mAuth.getCurrentUser().getEmail()); //family_email.getText().toString().trim().toLowerCase());
                            //family_email.setText("");
                        }
                        if(task.isComplete() &&  member_found.size() > 0 ){

                            Log.d("FOUND USER", "User Added: " +  ((FamilyMember) member_found.get(0)).getEmail() );

                            /** Set Users **/
                            UserData userData = new UserData(mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getUid(), //((FamilyMember) member_found.get(0)).getUid(), //
                                    "false", "member", "Main", mAuth.getCurrentUser().getUid(), ((FamilyMember) member_found.get(0)).getUid(), "", "");

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

                            /** Delete User from Family DB **/
                            DatabaseReference deleteUser = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = deleteUser.child("Family").child("Members").orderByChild("email").equalTo( mAuth.getCurrentUser().getEmail() ); //user_email_searchString[0]);

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e("TAG", "onCancelled", databaseError.toException());
                                }
                            });

                        }
                    }
                });




                add_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TAG QUERY", "ADD FAMILY BUTTON PRESSED");
                        Log.d("TAG QUERY", "User Active " + mAuth.getCurrentUser().getEmail());


/** Set Users **/
                        UserData userData = new UserData(mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getUid(), //((FamilyMember) member_found.get(0)).getUid(), //
                                "false", "member", "Main", mAuth.getCurrentUser().getUid(), ((FamilyMember) member_found.get(0)).getUid(), "", "");

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

/** Delete User from Family DB **/
                        DatabaseReference refnew = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = refnew.child("Family").child("Members").orderByChild("email").equalTo( mAuth.getCurrentUser().getEmail() ); //user_email_searchString[0]);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("TAG", "onCancelled", databaseError.toException());
                            }
                        });

                        family_info_txt.setText(mAuth.getCurrentUser().getEmail() + "\n You have been added to the FamilyPlanner \n "); //family_email.getText().toString().toLowerCase().trim() + "\n You have been added to the FamilyPlanner \n ");
                        family_email.setText("");
                        add_btn.setEnabled(false);
                    }
                });

            }
        });
        return list_view;
    }
}