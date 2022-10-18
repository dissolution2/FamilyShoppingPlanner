package com.fwa.app.testingViews.testingViews.fragment.shopping;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link button_menu_fragment_shopping_list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class button_menu_fragment_shopping_list extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public button_menu_fragment_shopping_list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment button_menu_fragment_shopping_list.
     */
    // TODO: Rename and change types and number of parameters
    public static button_menu_fragment_shopping_list newInstance(String param1, String param2) {
        button_menu_fragment_shopping_list fragment = new button_menu_fragment_shopping_list();
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
        View view = inflater.inflate(R.layout.fragment_button_menu_shopping_list, container, false);

        Button btnOne = (Button) view.findViewById(R.id.btnShoppingOne);

        FragmentManager fragmentManager = getParentFragmentManager();
        //FragmentManager fragmentManager = getFragmentManager();

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, button_one_fragment_shopping.class, null ).
                        setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        return view;
    }
}