package com.fwa.app.testingViews.testingViews.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.fwa.app.classes.Product;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.fwa.app.classes.Option;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link button_menu_fragment_option_product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class button_menu_fragment_option_product extends Fragment {

    private View list_view;
    private String data_sent_shopping_list_is="";
    private String data_back_to_view ="";
    private int clickOptionPressed = 0;
    private FirebaseRWQ firebaseRWQ = new FirebaseRWQ();

    public FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public button_menu_fragment_option_product() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment button_menu_fragment_option.
     */
    // TODO: Rename and change types and number of parameters
    public static button_menu_fragment_option_product newInstance(String param1, String param2) {
        button_menu_fragment_option_product fragment = new button_menu_fragment_option_product();
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
        list_view = inflater.inflate(R.layout.fragment_button_menu_option, container, false);

        /** SearchView take the query send it to search_query_fragment
         *  When search key is used, call parentFragmentManager to use fragmentContainerViewList to do the search in that fragment
         *  search_query_fragment.java / fragment_search_query_fragment.xml
         * **/
        SearchView option_menu_search_listed_products = list_view.findViewById(R.id.option_menu_search_listed_products);

        //getParentFragmentManager().beginTransaction().add(R.id.fragmentContainerViewList, new search_query_fragment()).commit();

        option_menu_search_listed_products.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Bundle result = new Bundle();
                result.putString("search_value", option_menu_search_listed_products.getQuery().toString());
                getParentFragmentManager().setFragmentResult("data_send",result);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewList, search_query_fragment.class,null, "search_list_recycle")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });

        //ToDo:: this is not working big bug - if one press main option button to get the menu of option and then first option_change_user_list goes into a loop write -very bad!!
        //ToDo: Query set user db default shoppingList in use here Set Option with inn UsersGroup - userId - Option for that user !!
        //ToDo: change so we only write this once, not every time we load the activity



        /** Get option witch list is sett, value from swipe_info_fragment_main.java, so we can change the list here */
/*
        getParentFragmentManager().setFragmentResultListener("data_send", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                data_sent_shopping_list_is = result.getString("shopping_list_set");
                Toast.makeText(getActivity(), "Option menu data get: " + data_sent_shopping_list_is.toString() , Toast.LENGTH_LONG).show();
            }
        });
*/
        /* don't use went with db query instead!!
        Bundle result = new Bundle();
        result.putString("shopping_list_set", data_back_to_view);
        getParentFragmentManager().setFragmentResult("new_data_send",result);
        */

        //Toast.makeText(getActivity(), "Changed Shopping default List", Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(), "click option " + clickOptionPressed, Toast.LENGTH_LONG).show();

        //firebaseRWQ.updateUserOption(); need the value in the firebseRWQ to change it... later
        ImageButton imageBtnOptionOne = (ImageButton)list_view.findViewById(R.id.imageBtnOptionOne);
        imageBtnOptionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOptionPressed++;
                //Toast.makeText(getActivity(), "click option " + clickOptionPressed, Toast.LENGTH_LONG).show();
                switch (clickOptionPressed){

                    case 1:
                        data_back_to_view = "WEEKEND";
                        //updateUserOption(); // this ok!! why!!
                        //firebaseRWQ.updateUserOption(data_back_to_view); // calling this made it go loop
                        break;
                    case 2:
                        data_back_to_view = "DIV";
                        //updateUserOption();
                        //firebaseRWQ.updateUserOption(data_back_to_view);
                        break;
                    case 3:
                        data_back_to_view = "MAIN";
                        //updateUserOption();
                        //firebaseRWQ.updateUserOption(data_back_to_view);
                        break;
                }
                // onDataChange eventListener In object FirebaseRWQ // fails loops
                // onCompleteListener In object FirebaseRWQ  // ok
                // onCompleteListener with in this activity // fails loops
                // onDataChange eventListener with in this activity // ok

                firebaseRWQ.updateUserOptionOnComplete(data_back_to_view);
                //updateUserOption();
                /** Reset optionList pressed  */
                if(clickOptionPressed == 3){
                    clickOptionPressed = 0;
                }







/*
// change the fragment class into the the info view if we want!!!
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerViewInfo_main_view, swipe_info_fragment_main.class,null, "option_menu_default_list_settings")
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
*/

            }
        });

        return list_view;
    }

    public void updateUserOption(){

        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Option");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String, Object> updates = new HashMap<String,Object>();

                    updates.put("DefaultShoppingList", data_back_to_view);
                    //updates.put("homeScore", newscore);
                    //etc

                    ref.updateChildren(updates);
/*
                            List product_List = new ArrayList();

                            for (DataSnapshot child : dataSnapshot.getChildren() ) {

                                //String barcode = child.toString();
                                String barcode = child.getValue().toString();


                                product_List.add(new Option(barcode));
                            }

                            // test for fun!!
                            Log.d("TAG get data","BareCode: " + ((Option)product_List.get(0)).getDefaultShoppingList());
 */

                }else{
                    Log.d("TAG get data","Don't = dataSnapshot exists !!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERORR","data error " + databaseError.toString());
            }
        });



    }
}