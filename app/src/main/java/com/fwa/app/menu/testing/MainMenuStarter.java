package com.fwa.app.menu.testing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.button_menu_fragment_add_new_product;
import com.fwa.app.menu.testing.FragmentA;

public class MainMenuStarter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_layout_main);

        EditText editText = findViewById(R.id.editTextTextPersonName);
        Button button2 = findViewById(R.id.button2);
        FragmentManager fragmentManager = getSupportFragmentManager();

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fragmentManager.beginTransaction()
                        //.add(R.id.container_a, FragmentA.newInstance("is","melk"),null)
                        .add(R.id.container_b, FragmentB.newInstance(editText.getText().toString(),"melk"),null)
                        .commit();
            }
        });
        /*
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                //.add(R.id.container_a, FragmentA.newInstance("is","melk"),null)
                .add(R.id.container_b, FragmentB.newInstance("is","melk"),null)
                .commit();
*/

        //fragmentManager.getFragments().add(FragmentA.newInstance("Robin","Larsen"));


        //fragmentManager.getFragments().add(0, FragmentA.newInstance("Robin","Larsen"));

        /*
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.container_a, FragmentA.class,null)
                .add(R.id.container_b, FragmentB.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
*/
                /*
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container_a, new FragmentA())
                .add(R.id.container_b, new FragmentC())
                //.add(R.id.container_c, new FragmentC())
                //.add(R.id.container_d, new FragmentD())
                .commit();
*/
    }

}
