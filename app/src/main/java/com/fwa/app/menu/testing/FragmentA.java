package com.fwa.app.menu.testing;

//import android.arch.lifecycle.Observer;
//import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.testingViews.testingViews.fragment.button_menu_fragment_add_new_product;
import com.fwa.app.menu.testing.FragmentA;
public class FragmentA extends Fragment {
    private SharedViewModel viewModel;
    private View list_view;
    private EditText editText;
    private SearchView searchView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static String testNewInstansString ="";

    public FragmentA() {
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
    public static FragmentA newInstance(String param1, String param2) {
        FragmentA fragment = new FragmentA();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        testNewInstansString = param1;
        Log.d("PARAM newIns A", "String 1: " + param1);
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





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        list_view = inflater.inflate(R.layout.fragment_a, container, false);

        Log.d("PARAM OnCre A", "String 1: " + mParam1 + " Static String 1: " + testNewInstansString);

        viewModel= new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        FragmentManager fragmentManager = getParentFragmentManager();

        //fragmentManager.getFragments().add(0, FragmentA.newInstance("Robin","Larsen"));


        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
                editText.setText(charSequence);
            }
        });


        searchView = list_view.findViewById(R.id.search_my_query);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                /*

                getParentFragment().getChildFragmentManager().beginTransaction()
                        .replace(R.id.container_b, FragmentB.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
*/

                viewModel.setText(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        editText = list_view.findViewById(R.id.edit_text);
        Button button = list_view.findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.setText(editText.getText());
            }
        });

        return list_view;
    }

}