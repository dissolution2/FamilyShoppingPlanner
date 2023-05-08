package com.fwa.app.testingViews.testingViews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fwa.app.familyshoppingplanner.MainActivity;
import com.fwa.app.menu.testing.MainMenuStarter;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.fwa.app.product.manualy.add.Main_t_manualy_add_products_to_db;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.familyshoppingplanner.ToolbarCaptureActivity;
import com.fwa.app.testingViews.testingViews.fragment.FragmentMainClass;
import com.fwa.app.testingViews.testingViews.fragment.test.FirstTimeRun_test;
import com.fwa.app.testingViews.testingViews.fragment.test.FirstTimeRunAddToFamily_test;
import com.fwa.app.testingViews.testingViews.fragment.test.main_app_pre_check_user_test;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Main_t_gui_menu_view extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "TEST:";
    private Button logout, test_DB_Insert, test_show_DataDBBtn, test_guiListViewBtn, test_recycleViewBtn, test_bareCodeReaderBtn, test_list_viewBtn;

    private TextView search_txt;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_main_test_menu);


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

        test_DB_Insert = (Button) findViewById(R.id.testDBBtn);
        test_DB_Insert.setOnClickListener(this);

        test_show_DataDBBtn = (Button) findViewById(R.id.show_DataDBBtn);
        test_show_DataDBBtn.setOnClickListener(this);

        test_guiListViewBtn = (Button) findViewById(R.id.main_app_test_btn);
        test_guiListViewBtn.setOnClickListener(this);


        test_bareCodeReaderBtn = (Button) findViewById(R.id.bareCodeReader);
        test_bareCodeReaderBtn.setOnClickListener(this);

        test_recycleViewBtn = findViewById(R.id.recycleView);
        test_recycleViewBtn.setOnClickListener(this);


        test_list_viewBtn = findViewById(R.id.list_view_test);
        test_list_viewBtn.setOnClickListener(this);

        logout = (Button) findViewById(R.id.signOut);
        logout.setOnClickListener(this);


        // ToDo: fix this only if it is her, tried a empty container inn between fragments
        /** works Need to refresh this  **/
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        //editor.clear();
        //editor.commit();

        /// Inn use !!! get the uid on all logged inn family's /// moved again to FragmentMainClass
/*
        Query getUserRole = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("FamilyUid").orderByKey().equalTo("role");

        getUserRole.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()) {
                    Log.d("QUERY role", "Exists Data Count -role: " + task.getResult().getChildrenCount());
                    //Long childrenCountLong = task.getResult().getChildrenCount();
                    //Log.d("TAG QUERY", "Key role: " + task.getResult().child("role").getValue());

                    //if( !childrenCountLong.toString().equals("0") ) {

                    if (task.getResult().child("role").getValue().toString().equals("member")) {

                        Query uid_member = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                                .child("FamilyUid").child("hashFamily");

                        uid_member.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.getResult().exists()) {
                                    Log.d("QUERY hashFamily", "Exists Data Count -hashFamily: " + task.getResult().getChildrenCount());
                                    Log.d("QUERY hashFamily", "Key: " + task.getResult().child(mAuth.getCurrentUser().getUid()).getValue());
                                    //editor.clear();
                                    getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();
                                    editor.putString("key_name", task.getResult().child(mAuth.getCurrentUser().getUid()).getValue().toString());
                                    editor.commit(); // commit changes
                                } else {
                                    Log.d("QUERY hashFamily", "Don't Exists Error !!! Count hashFamily " + task.getResult().getChildrenCount());
                                }
                            }
                        });
                    }
                    if (task.getResult().child("role").getValue().toString().equals("owner")) {

                        Log.d("TAG", "role owner we use its Own uid loggInn Data " + mAuth.getCurrentUser().getUid());
                        //editor.clear();
                        getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();
                        editor.putString("key_name", mAuth.getCurrentUser().getUid()); // Storing string
                        editor.commit(); // commit changes

                    }
                }else{
                    Log.d("TAG", "No User have to use current user on a new family !! User logged inn: " + mAuth.getCurrentUser().getUid());
                    /// But he/she can be in family/members - waiting to join a family ///

                    getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();
                    //String test = "NO_USER";
                    Query getFamilyMembers = database.getReference("Family").child("Members");

                    getFamilyMembers.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DataSnapshot> task) {
                             if (task.getResult().exists()) {

                                 editor.putString("key_name", "@"); // Storing string
                                 editor.commit(); // commit changes

                             }else {
                                 editor.putString("key_name", "NO_USER"); // Storing string
                                 editor.commit(); // commit changes
                             }
                         }
                     });

                }
                //} else {
                //    Log.d("TAG QUERY", "Don't Exists Count -role " + task.getResult().getChildrenCount());
                //    editor.putString("key_name", "Null"); // Storing string
                //    editor.commit(); // commit changes
                //}
            }
        });

/*



/*


        // Moved to FragmentMainClass
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        //Inn use !!! get the uid on all logged inn family's - removed testing main rules
        Query ref3 = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("FamilyUid").orderByKey().equalTo("role");//.child("role");//.orderByKey();//.equalTo(mAuth.getCurrentUser().getUid());//.orderByChild("uid");

        ref3.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()) {
                    Log.d("TAG QUERY", "Exists Data Count is: " + task.getResult().getChildrenCount());
                    Log.d("TAG QUERY", "Key role: " + task.getResult().child("role").getValue() );
                    //Log.d("TAG TASK", "Key hashFamily uid: " + task.getResult().child(mAuth.getCurrentUser().getUid()).getValue()); // .getKey() + " Value: " + task.getResult().getValue());

                    if( task.getResult().child("role").getValue().toString().equals("member") ){
                        //Log.d("TAG", "role member " + task.getResult().child(mAuth.getCurrentUser().getUid()).getValue() );

                        Query uid_member = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                                .child("FamilyUid").child("hashFamily");//.orderByKey().equalTo(mAuth.getCurrentUser().getUid());//.orderByChild("uid");

                        uid_member.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.getResult().exists()) {
                                    Log.d("TAG QUERY", "Exists Data Count is: " + task.getResult().getChildrenCount());
                                    Log.d("TAG TASK", "Key: " + task.getResult().child(mAuth.getCurrentUser().getUid()).getValue()); // .getKey() + " Value: " + task.getResult().getValue());

                                    editor.putString("key_name", task.getResult().child(mAuth.getCurrentUser().getUid()).getValue().toString()); // Storing string
                                    editor.commit(); // commit changes

                                } else {
                                    Log.d("TAG QUERY", "Exists Data is Count: " + task.getResult().getChildrenCount());
                                }
                            }
                        });

                    }

                    if( task.getResult().child("role").getValue().toString().equals("owner") ){

                        Log.d("TAG", "role owner we use its Own uid loggInn Data " + mAuth.getCurrentUser().getUid()   );
                        editor.putString("key_name", mAuth.getCurrentUser().getUid() ); // Storing string
                        editor.commit(); // commit changes

                    }
                } else {
                    Log.d("TAG QUERY", "Exists Data is Count: " + task.getResult().getChildrenCount());
                }
            }
        });
*/

  /*

        Query uid_member = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("FamilyUid").child("hashFamily");//.orderByKey().equalTo(mAuth.getCurrentUser().getUid());//.orderByChild("uid");

        uid_member.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().exists()) {
                    Log.d("TAG QUERY", "Exists Data Count is: " + task.getResult().getChildrenCount());
                    Log.d("TAG TASK", "Key: " + task.getResult().child(mAuth.getCurrentUser().getUid()).getValue()); // .getKey() + " Value: " + task.getResult().getValue());

                    if( task.getResult().child(mAuth.getCurrentUser().getUid()) )

                    editor.putString("key_name", task.getResult().child(mAuth.getCurrentUser().getUid()).getValue().toString()); // Storing string
                    editor.commit(); // commit changes

                } else {
                    Log.d("TAG QUERY", "Exists Data is Count: " + task.getResult().getChildrenCount());
                }
            }
        });
*/


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
                /** for testing - Add product to db by list */
                startActivity(new Intent(this, Main_t_manualy_add_products_to_db.class));
                break;
            case R.id.show_DataDBBtn:
                /** New Usage - Add User to Current Logged in Family**/
                startActivity(new Intent(this, FirstTimeRun_test.class));


                /*** Simple test get data to from - real time db, and collection cloud db *
                 *  Out of date - need working on if to test */
                //startActivity(new Intent(this, Main_t_simple_getdatafromdb.class));
                //Toast.makeText(Main_t_gui_menu_view.this, "Deactivated, Not active for testing!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.recycleView:
                /** testing creating new fragment to use with searching the data base list for products */
                startActivity(new Intent(this, MainMenuStarter.class));
                //Toast.makeText(Main_t_gui_menu_view.this, "Deactivated, Not active for testing!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.main_app_test_btn:

                /** Pre check screen to see if you a user **/ // Ok move to use fragment
                //startActivity(new Intent(this, main_app_pre_check_user_test.class));

                /** Directly to main app screen **/
                startActivity(new Intent(this, FragmentMainClass.class));

                /** Main App testing **/
                //startActivity(new Intent(this, Main_t_recycleview_one_test.class));
                break;

            case R.id.bareCodeReader:
                /** scanToolbar(v); testing  **/
                startActivity(new Intent(this, main_add_product_shopping_list_with_barcode_reader_db.class));
                break;
            case R.id.list_view_test:
                startActivity(new Intent(this, FirstTimeRunAddToFamily_test.class));
                //startActivity(new Intent(this, list_view_test.class));
                //startActivity(new Intent(this, FragmentFirstTimeRun.class));
                //Toast.makeText(Main_t_gui_menu_view.this, "Not active for testing!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.signOut:
                /** Logg user out of app / db Auth */
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