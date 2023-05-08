package com.fwa.app.testingViews.testingViews.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.setup.fragment_sett_up_family;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link button_menu_fragment_add_new_product#newInstance} factory method to
 * create an instance of this fragment.
 */
public class button_menu_fragment_add_new_product extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View list_view;

    private ImageButton temp_manual_add_product_btn;

    public button_menu_fragment_add_new_product() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment button_menu_fragment_add_new_product.
     */
    // TODO: Rename and change types and number of parameters
    public static button_menu_fragment_add_new_product newInstance(String param1, String param2) {
        button_menu_fragment_add_new_product fragment = new button_menu_fragment_add_new_product();
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
        list_view = inflater.inflate(R.layout.fragment_button_menu_add_new_product, container, false);
        FragmentManager fragmentManager = getParentFragmentManager();

        temp_manual_add_product_btn = list_view.findViewById(R.id.imageBtnOptionOne);
        temp_manual_add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // open inn main view app
                fragmentManager.beginTransaction()
                        .replace(R.id.Fragment_Container_Recycle_View_Main, fragment_add_product_manually_admin.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();


            }
        });








        return list_view;
    }
}