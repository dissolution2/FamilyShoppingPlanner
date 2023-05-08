package com.fwa.app.testingViews.testingViews.fragment.setup;

import static android.content.Context.MODE_PRIVATE;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fwa.app.classes.Uid;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.MainActivity;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.Main_t_gui_menu_view;
import com.fwa.app.testingViews.testingViews.fragment.button_menu_fragment_add_new_product;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentMainSettUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmentMainSettUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String user_uid="";
    private Button join_a_family_btn, start_a_family_btn, logg_user_out_btn;

    private View list_view;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();
    List<Uid> uid = new ArrayList<>();
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private TextView family_info_txt;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentMainSettUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentMainSettUp.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentMainSettUp newInstance(String param1, String param2) {
        fragmentMainSettUp fragment = new fragmentMainSettUp();
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
        list_view = inflater.inflate(R.layout.fragment_main_sett_up, container, false);
        FragmentManager fragmentManager = getParentFragmentManager();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this.getActivity(),gso);

        family_info_txt = (TextView)list_view.findViewById(R.id.family_info_txt);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        join_a_family_btn = list_view.findViewById(R.id.search_for_family_btn);
        start_a_family_btn = list_view.findViewById(R.id.sett_up_family_btn);
        logg_user_out_btn = list_view.findViewById(R.id.logg_out);


        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        user_uid = pref.getString("key_name","NO_USER");

        if(user_uid.equals("@")){
            family_info_txt.setText("User located in family not sett!!  \n&\n can be a new user want to start a family" + user_uid);
            join_a_family_btn.setEnabled(true);
            join_a_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0ED6B9"))); // active

        }else if (user_uid.equals("") || user_uid.equals("NO_USER")){

            family_info_txt.setText("User Found " + user_uid);
            join_a_family_btn.setEnabled(false);
            join_a_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7"))); // inactive

        }else{
            family_info_txt.setText("User Found " + user_uid);
            //start_a_family_btn.setEnabled(false);
            //start_a_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7")));

            join_a_family_btn.setEnabled(false);
            join_a_family_btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E8E7")));


            // ToDo: trie to get this one to work - works as now, but we then cant call on Option if user is Known!!
            /*
            fragmentManager.beginTransaction()
                    .replace(R.id.Fragment_Container_Recycle_View_Main, button_one_fragment_storage.class,null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();

             */
        }


        start_a_family_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_Container_Recycle_View_Main, fragment_sett_up_family.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        join_a_family_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_Container_Recycle_View_Main, fragment_join_a_family.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        if (gsc != null) {
            //gsc.getSignInIntent().toString();
            Log.d("TAG", "GOOGLE: " + gsc.getSignInIntent().toString());
        }
        if( mAuth.getCurrentUser() != null){
            //
            Log.d("TAG", "mAuth: " + mAuth.getUid().toString()) ;
        }

        logg_user_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {

                        //ToDo: move logg inn to a fragment ???

                        //this.getActivity() finish();
                        //mAuth.signOut(); // new added to sign out of it downt know it this is ok or just to change user to what we want!!
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });
            }
        });

        return list_view;
    }
}