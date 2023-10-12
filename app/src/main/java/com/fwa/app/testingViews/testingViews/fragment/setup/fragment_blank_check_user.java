package com.fwa.app.testingViews.testingViews.fragment.setup;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fwa.app.classes.FamilyMember;
import com.fwa.app.classes.UserData;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.button_menu_fragment_option_product;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;
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
 * Use the {@link fragment_blank_check_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_blank_check_user extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String uid_user ="";

    private View list_view;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database; // = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_blank_check_user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_blank_check_user.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_blank_check_user newInstance(String param1, String param2) {
        fragment_blank_check_user fragment = new fragment_blank_check_user();
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
        list_view = inflater.inflate(R.layout.fragment_blank_check_user, container, false);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");


        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref",0);
        uid_user = pref.getString("key_name","NO_USER" );
        SharedPreferences.Editor editor = pref.edit();


        if(!uid_user.equals("NO_USER")){

            Log.d("!NO_USER", "Inn Blank user check uid_user: " + uid_user );

            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.Fragment_Container_Recycle_View_Main, button_one_fragment_storage.class,null, "option_menu_default_list_settings")
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();

       }

        if(uid_user.equals("NO_USER")) {
            Log.d("UID_USER == NO_USER", "Inn Blank user check");


            List members_list = new ArrayList();
            List member_found = new ArrayList();


            //Log.d("TAG QUERY", "User is: " + mAuth.getCurrentUser().getEmail());


            //[START First check on user's added - if true, add them to there family -Create user and delete the temporary user in Family/Members]
            Query ref3 = database.getReference("Family").child("Members");
            ref3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    Log.d("TASK", "task query Beginning");
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
                        Log.d("Member", "found: " + members_list);
                        for (int i = 0; i < members_list.size(); i++) {

                            if (((FamilyMember) members_list.get(i)).getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                                member_found.add((members_list.get(i)));
                                // family member found and will be added
                                break;
                            } else { //if(i == members_list.size() ){//&& ((FamilyMember) members_list.get(i)).getEmail().equals(family_email.getText().toString().trim().toLowerCase())){
                                // family_info_txt.setText("Not Found: \n" + mAuth.getCurrentUser().getEmail() ); //family_email.getText().toString().trim().toLowerCase());
                            }
                        }
                    } else {
                        Log.d("TAG QUERY", "Exists Data is Count: " + task.getResult().getChildrenCount());
                        //family_info_txt.setText("Not Found: \n" + mAuth.getCurrentUser().getEmail()); //family_email.getText().toString().trim().toLowerCase());
                        //family_email.setText("");
                    }
                    //[Found user logged inn, with inn Family/Member - Create User and Add it to there family]
                    if (task.isComplete() && member_found.size() > 0) {

                        Log.d("FOUND USER", "User Added: " + ((FamilyMember) member_found.get(0)).getEmail());

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
                        Query applesQuery = deleteUser.child("Family").child("Members").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail()); //user_email_searchString[0]);

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


                        //[Sett UDI that this user will be using to get data from the family]
                        editor.putString("key_name", ((FamilyMember) member_found.get(0)).getUid());
                        editor.commit();


                        /** Found a family for the user that logged inn take him to storage +4  **/
                        Toast.makeText(getActivity(), "Welcome to the family", Toast.LENGTH_LONG).show();

                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.Fragment_Container_Recycle_View_Main, button_one_fragment_storage.class, null, "button_one_fragment_storage")
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();

                    }
                    if (task.isComplete() && member_found.size() == 0) {
                        Log.d("MEMBER =0", "FRAG TO OPTION TO MAKE NEW FAMILY!!1");
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.Fragment_Container_Recycle_View_Main, fragmentMainSettUp.class, null, "button_one_fragment_storage")
                                .setReorderingAllowed(true)
                                .addToBackStack("name")
                                .commit();
                    }
                }
            });

        }


        return list_view;
    }
}