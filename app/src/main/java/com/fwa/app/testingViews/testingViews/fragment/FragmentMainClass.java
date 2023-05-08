package com.fwa.app.testingViews.testingViews.fragment;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.fwa.app.classes.ItemViewModel;
import com.fwa.app.classes.Uid;
import com.fwa.app.classes.UserData;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.setup.fragment_blank_check_user;
import com.fwa.app.testingViews.testingViews.fragment.shopping.menu.button_menu_fragment_shopping_list;
import com.fwa.app.testingViews.testingViews.fragment.shopping.button_one_fragment_shopping;
import com.fwa.app.testingViews.testingViews.fragment.storage.menu.button_menu_fragment_storage_list;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Random;

public class FragmentMainClass extends AppCompatActivity {

    private ItemViewModel viewModel;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    String data_sent_shopping_list_is="";
    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();
    private String uid_user="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_main_class);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");


        SharedPreferences pref = getSharedPreferences("MyPref",0);
        uid_user = pref.getString("key_name","NO_USER" );
        Log.d("FRAG MAIN","getSharedPref: " + uid_user);//pref.getString("key_name","NO_USER" ) );



        ImageButton imageBtnAddProducts = findViewById(R.id.imageBtnAddProducts);
        ImageButton imageBtnOption = findViewById(R.id.imageBtnOption);

        /**Default fragment to load inn sett it to false right away so it cant be click if loaded inn */
        imageBtnOption.setEnabled(false);
        //TextView txt_view_swipe_right = findViewById(R.id.txt_view_swipe_right);

        //SearchView searchView_product = findViewById(R.id.search_product);


        //TextView text_main_info_one = findViewById(R.id.text_main_info_one);
        //text_main_info_one.setText("Storage Cold -4");

        //TextView text_main_info_two = findViewById(R.id.text_main_info_two);
        //text_main_info_two.setText("");
        //TextView text_main_info_three = findViewById(R.id.text_main_info_three);
        //text_main_info_three.setText("");

        Button btnCallMenu_storage_list_buttons = findViewById(R.id.btnStorageListMenu);
        Button btnCallMenu_shopping_list_buttons = findViewById(R.id.btnShoppingListMenu);

        //ToDo: Refactoring to add user's to a family
        // change when got time, query if value set, if not set it to default.
        /** Set's the user shopping list default - */

        /** set up first time use and connecting family user's **/
        String sett_up_app ="FIRST_TIME_USE";

        switch (sett_up_app){
            case "FIRST_TIME_USE":
                //firebaseRWQ.write_first_start_up_of_family();
                //firebaseRWQ.add_user_to_family_account();

                //firebaseRWQ.query_value_id_string_token();
                Log.d("TAG SETT_UP","FIRST_TIME_USE");
                break;
            case "CHECK_USER":
                Log.d("TAG SETT_UP","CHECK_USER");
                break;
            case "WRITE_DEFAULT_SHOPPING_LIST":
                firebaseRWQ.write_user_shopping_list_default_to_use("MAIN");
                Log.d("TAG SETT_UP","WRITE_DEFAULT_SHOPPING_LIST");
                break;
        }





/** Here we gone add dif products and search with in database if not in any list -/storage /shopping */
        imageBtnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               FragmentManager fragmentManager = getSupportFragmentManager();



/** We call on fragment menu Button_menu_add_new_product view in fragment container */
//ToDO: sett it up!!
                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_ContainerView_Main_Button_Menu, button_menu_fragment_add_new_product.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();


/** Enable / disable buttons when fragment is loaded */
                imageBtnAddProducts.setEnabled(false);
                imageBtnOption.setEnabled(true);
            }
        });

/** Here we gone set dif Option's  and search with in recycleView */
        imageBtnOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/** We call on fragment menu Button_menu_option_product view in fragment container */
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_ContainerView_Main_Button_Menu, button_menu_fragment_option_product.class,null, "option_menu_default_list_settings")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();

/** Enable / disable buttons when fragment is loaded */
                imageBtnOption.setEnabled(false);
                imageBtnAddProducts.setEnabled(true);
            }
        });

        btnCallMenu_storage_list_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


/** We call on fragment button_menu_fragment_storage_list view in fragment container */
                FragmentManager fragmentManager = getSupportFragmentManager();
/** Calls menu option - chose storage list - to buttons not used - and search view button */


                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_ContainerView_Call_Storage_Shopping_Recycle_Lists_menu, button_menu_fragment_storage_list.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();


/** We call on fragment button_one_fragment_storage view in fragment container */

                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_Container_Recycle_View_Main, button_one_fragment_storage.class,null, "storage_list_one")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();


            }
        });

        btnCallMenu_shopping_list_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();

/** We call on fragment button_menu_fragment_shopping_list view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_ContainerView_Call_Storage_Shopping_Recycle_Lists_menu, button_menu_fragment_shopping_list.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
/** We call on fragment button_one_fragment_shopping view in fragment container */




                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_Container_Recycle_View_Main, button_one_fragment_shopping.class,null,"shopping_list_one")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event){
        if(key_code == KeyEvent.KEYCODE_BACK){
            super.onKeyDown(key_code, key_event);
            Toast.makeText(getApplicationContext(), "back press Disabled!!",
                    Toast.LENGTH_LONG).show();

        }
        return false;
    }
}
