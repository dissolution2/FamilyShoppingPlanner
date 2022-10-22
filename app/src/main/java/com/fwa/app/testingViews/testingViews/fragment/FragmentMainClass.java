package com.fwa.app.testingViews.testingViews.fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.fwa.app.classes.Product;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.fwa.app.testingViews.testingViews.fragment.shopping.button_menu_fragment_shopping_list;
import com.fwa.app.testingViews.testingViews.fragment.shopping.button_one_fragment_shopping;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_menu_fragment_storage_list;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentMainClass extends AppCompatActivity {

    String data_sent_shopping_list_is="";
    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_main_class);
        FragmentManager fragmentManager = getSupportFragmentManager();

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

        //ToDo: change when got time, query if value set, if not set it to default.
        /** Set's the user shopping list default - */
        firebaseRWQ.write_user_shopping_list_default_to_use("MAIN");




        //getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, new search_query_fragment()).commit();
/*
        //ToDo: Move this to new fragment Or might use it several fragments!!
        searchView_product.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("TAG DB", (String) searchView_product.getQuery().toString() );
                Toast.makeText(FragmentMainClass.this, "Query test: " + searchView_product.getQuery(), Toast.LENGTH_LONG).show();


                Bundle result = new Bundle();
                result.putString("search_value", searchView_product.getQuery().toString());

                getSupportFragmentManager().setFragmentResult("data_sendt",result);



                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewList, search_query_fragment.class,null, "search_list_recycle")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        Button btnAddProduct_shopping_list_buttons = findViewById(R.id.btnAddProductBareCode);

        btnAddProduct_shopping_list_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

*/
        /** Get option txt from fragment when is loaded !! is loaded with main fragment view !!*/
        /*
        fragmentManager.setFragmentResultListener("data_send", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                data_sent_shopping_list_is = result.getString("shopping_list_set");
                Toast.makeText(FragmentMainClass.this, "MainClass Data resive: " + data_sent_shopping_list_is.toString() , Toast.LENGTH_LONG).show();
            }
        });
*/



/** Here we gone add dif products and search with in database if not in any list -/storage /shopping */
        imageBtnAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

/** We call on fragment menu Button_menu_add_new_product view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewButton_menu, button_menu_fragment_add_new_product.class,null)
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


                //Bundle result = new Bundle();
                //String remove_spec_characters = txt_view_swipe_right.getText().toString().replaceAll("[^\\w\\s]", "");
                //result.putString("shopping_list_sett", remove_spec_characters);

                //getSupportFragmentManager().setFragmentResult("data_sendt",result);

/** We call on fragment menu Button_menu_option_product view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewButton_menu, button_menu_fragment_option_product.class,null, "option_menu_default_list_settings")
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

                //text_main_info_one.setText("Storage Cold -4");
                //text_main_info_two.setText("Main Shopping List");
/** We call on fragment button_menu_fragment_storage_list view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewList_menu, button_menu_fragment_storage_list.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
/** We call on fragment button_one_fragment_storage view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewList, button_one_fragment_storage.class,null, "storage_list_one")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        btnCallMenu_shopping_list_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //text_main_info_one.setText("Shopping Main List");
                //text_main_info_two.setText("Swipe -> designated storage");
/** We call on fragment button_menu_fragment_shopping_list view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewList_menu, button_menu_fragment_shopping_list.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
/** We call on fragment button_one_fragment_shopping view in fragment container */
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewList, button_one_fragment_shopping.class,null,"shopping_list_one")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
    }
}
