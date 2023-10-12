package com.fwa.app.testingViews.testingViews.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.familyshoppingplanner.ToolbarCaptureActivity;
import com.fwa.app.testingViews.testingViews.Main_t_gui_menu_view;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_add_product_fragment_barcode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_add_product_fragment_barcode extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View list_view;

    private EditText barcode_txt;
    //edit editTextBarCode

    public fragment_add_product_fragment_barcode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_add_product_fragment_barcode.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_add_product_fragment_barcode newInstance(String param1, String param2) {
        fragment_add_product_fragment_barcode fragment = new fragment_add_product_fragment_barcode();
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

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");

                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");

                    }
                } else {
                    Log.d("TAG BARCODE", "BarCode is: " + result);
                    barcode_txt.setText(result.getContents());

                    //ToDo: now to query db !! if the product is in the db, else we have to add it our self to user db

                    /**
                    BarCode is: Format: EAN_13
                    Contents: 7048840081950
                    Raw bytes: (0 bytes)
                    Orientation: null
                    EC level: null
                    Barcode image: null
                    Original intent: Intent { act=com.google.zxing.client.android.SCAN flg=0x80000 (has extras) }
                     */

                    /** Save the Content and make a new product to the database */


                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        list_view = inflater.inflate(R.layout.fragment_add_product_fragment_barcode, container, false);

        barcode_txt = list_view.findViewById(R.id.editTextBarCode);

        ScanOptions options = new ScanOptions().setCaptureActivity(ToolbarCaptureActivity.class);
        barcodeLauncher.launch(options);

        return list_view;
    }
}