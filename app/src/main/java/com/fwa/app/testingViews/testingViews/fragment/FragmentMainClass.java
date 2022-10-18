package com.fwa.app.testingViews.testingViews.fragment;



import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.shopping.button_menu_fragment_shopping_list;
import com.fwa.app.testingViews.testingViews.fragment.shopping.button_one_fragment_shopping;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_menu_fragment_storage_list;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;

public class FragmentMainClass extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_main_class);

        TextView text_main_info = findViewById(R.id.text_main_info);
        text_main_info.setText("Storage Cold -4");
        //Button btnOne = findViewById(R.id.button1);
        //Button btnTwo = findViewById(R.id.button2);
        //Button btnThree = findViewById(R.id.button3);

        Button btnCallMenu_storage_list_buttons = findViewById(R.id.btnStorageListMenu);
        Button btnCallMenu_shopping_list_buttons = findViewById(R.id.btnShoppingListMenu);

        FragmentManager fragmentManager = getSupportFragmentManager();

        btnCallMenu_storage_list_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                text_main_info.setText("Storage Cold -4");

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView2, button_menu_fragment_storage_list.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, button_one_fragment_storage.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        btnCallMenu_shopping_list_buttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                text_main_info.setText("Shopping Main List");

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView2, button_menu_fragment_shopping_list.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, button_one_fragment_shopping.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        //fragmentManager.get
/*
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, buttonOneFragmentCall.class, null ).
                        setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, buttonTwoFragmentCall.class, null ).
                        setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, buttonThreeFragmentCall.class, null ).
                        setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });
*/

    }
}
