package com.fwa.app.testingViews.testingViews.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fwa.app.classes.Option;
import com.fwa.app.classes.Product;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link swipe_info_fragment_main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class swipe_info_fragment_main extends Fragment {

    private View list_view;
    private String data_sent_shopping_list_is="";
    private TextView txt_view_swipe_right;
    private String previus_txt_view_swipe_right;

    FirebaseRWQ firebaseRWQ = new FirebaseRWQ();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public swipe_info_fragment_main() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment swipe_info_fragment_main.
     */
    // TODO: Rename and change types and number of parameters
    public static swipe_info_fragment_main newInstance(String param1, String param2) {
        swipe_info_fragment_main fragment = new swipe_info_fragment_main();
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
        list_view = inflater.inflate(R.layout.fragment_swipe_info_main, container, false);
        txt_view_swipe_right = list_view.findViewById(R.id.txt_view_swipe_right);
        previus_txt_view_swipe_right = txt_view_swipe_right.toString();

        /** Note: big different on addValueEventListener (onDataChange) vrs onCompleteListener (onComplete) */
        DatabaseReference ref = firebaseRWQ.getRef_user_option();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    List product_List = new ArrayList();

                    for (DataSnapshot child : dataSnapshot.getChildren() ) {

                        String barcode = child.getValue().toString();
                        //Toast.makeText(getActivity(), "DB get List " + barcode, Toast.LENGTH_LONG).show();
                        product_List.add(new Option(barcode));
                    }
                    txt_view_swipe_right.setText(((Option)product_List.get(0)).getDefaultShoppingList());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERORR","data error " + databaseError.toString());
            }
        });


/** Send what is set as main List - with inn txt_view_swipe_right  */
/** resiver is button_menu_fragment_option_product.java */
/** fragment container inn main app view = fragmentContainerViewInfo_main_view */
/*
        Bundle result = new Bundle();
        String remove_spec_characters = txt_view_swipe_right.getText().toString().replaceAll("[^\\w\\s]", "");
        result.putString("shopping_list_set", remove_spec_characters);

        // sending the data
        getParentFragmentManager().setFragmentResult("data_send",result);
*/

        /** Resiver of data */
        /** Get option witch list is sett, value from swipe_info_fragment_main.java, so we can change the list here */
        //ToDo: Can use query db to get the default shopping List in use all so !!
     /*
        getParentFragmentManager().setFragmentResultListener("new_data_send", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                //data_sent_shopping_list_is ="null";
                data_sent_shopping_list_is = result.getString("shopping_list_set");
                Toast.makeText(getActivity(), "swipe_info_fragment: " + data_sent_shopping_list_is.toString() + " Pre: " + previus_txt_view_swipe_right.toString(), Toast.LENGTH_LONG).show();

                txt_view_swipe_right.setText(data_sent_shopping_list_is);

            }
        });
*/
        return list_view;
    }
}