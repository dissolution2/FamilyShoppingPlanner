package com.fwa.app.testingViews.testingViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.fwa.app.familyshoppingplanner.MainActivity;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.fwa.app.product.manualy.add.Main_t_manualy_add_products_to_db;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.familyshoppingplanner.ToolbarCaptureActivity;
import com.fwa.app.testingViews.testingViews.Employee.EmployeeMainActivity;
import com.fwa.app.testingViews.testingViews.fragment.FragmentMainClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Main_t_gui_menu_view extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TEST:";
    private Button logout, test_DB_Insert, test_show_DataDBBtn, test_guiListViewBtn, test_recycleViewBtn, test_bareCodeReaderBtn, test_list_viewBtn;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_main_test_menu);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        test_DB_Insert = (Button) findViewById(R.id.testDBBtn);
        test_DB_Insert.setOnClickListener(this);

        test_show_DataDBBtn = (Button) findViewById(R.id.show_DataDBBtn);
        test_show_DataDBBtn.setOnClickListener(this);

        test_guiListViewBtn = (Button) findViewById(R.id.guiListViewBtn);
        test_guiListViewBtn.setOnClickListener(this);


        test_bareCodeReaderBtn = (Button) findViewById(R.id.bareCodeReader);
        test_bareCodeReaderBtn.setOnClickListener(this);

        test_recycleViewBtn = findViewById(R.id.recycleView);
        test_recycleViewBtn.setOnClickListener(this);

        test_list_viewBtn = findViewById(R.id.list_view_test);
        test_list_viewBtn.setOnClickListener(this);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);


        /** Se how and what loggedIn !!! ???  */

        if (gsc != null) {
            //gsc.getSignInIntent().toString();
            Log.d(TAG, "GOOGLE: " + gsc.getSignInIntent().toString());
        }
        if( mAuth.getCurrentUser() != null){
            //
            Log.d(TAG, "mAuth: " + mAuth.getUid().toString()) ;
        }
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


    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(Main_t_gui_menu_view.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(Main_t_gui_menu_view.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("MainActivity", "BarCode is");
                    Toast.makeText(Main_t_gui_menu_view.this, "Scanned is: " + result.getContents(), Toast.LENGTH_LONG).show();

                    /** Save the Content and make a new product to the database */


                }
            });



    public void scanToolbar(View view) {
        ScanOptions options = new ScanOptions().setCaptureActivity(ToolbarCaptureActivity.class);
        barcodeLauncher.launch(options);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.testDBBtn:
                startActivity(new Intent(this, Main_t_manualy_add_products_to_db.class));
                break;
            case R.id.show_DataDBBtn:
                startActivity(new Intent(this, Main_t_simple_getdatafromdb.class));
                break;
            case R.id.recycleView:
                //startActivity(new Intent(this, Main_t_recycleview_two_test.class));
                Toast.makeText(Main_t_gui_menu_view.this, "Deactivated, Not active for testing!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.guiListViewBtn:
                //startActivity(new Intent(this, Main_t_recycleview_one_test.class));
                startActivity(new Intent(this, FragmentMainClass.class));
                break;

            case R.id.bareCodeReader:
                //scanToolbar(v);  // test
                startActivity(new Intent(this, main_add_product_shopping_list_with_barcode_reader_db.class));
                break;
            case R.id.list_view_test:
                //startActivity(new Intent(this, list_view_test.class));
                startActivity(new Intent(this, EmployeeMainActivity.class));
                //Toast.makeText(Main_t_gui_menu_view.this, "Not active for testing!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.signOut:

                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        finish();
                        mAuth.signOut(); // new added to sign out of it downt know it this is ok or just to change user to what we want!!
                        startActivity(new Intent(Main_t_gui_menu_view.this, MainActivity.class));
                    }
                });

                //startActivity(new Intent(this, MainActivity.class));
                //finish();
                break;

        }


    }
}