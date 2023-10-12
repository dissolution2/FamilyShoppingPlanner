package com.fwa.app.familyshoppingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.fwa.app.testingViews.testingViews.Main_t_gui_menu_view;
import com.fwa.app.testingViews.testingViews.fragment.FragmentMainClass;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String ApiException = "ApiException";
    private static final String TAG = "TAG";
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    private FirebaseAuth mAuth;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private ProgressBar progressBar;
    private TextView register;
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    ImageView googleBtn, facebookBtn, twitterBtn;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google - Sosial Nettwork login
        googleBtn = findViewById(R.id.google_btn);
        googleBtn.setOnClickListener(this);

        facebookBtn = findViewById(R.id.facebook_btn);
        facebookBtn.setOnClickListener(this);

        twitterBtn = findViewById(R.id.twitter_btn);
        twitterBtn.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        mAuth = FirebaseAuth.getInstance();

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        Log.d("OnCreate Main", "We CLEAR getSharedPreferences");

}


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.d("TAG startForResult", "this is called result: " + result);
           if(result != null && result.getResultCode() == RESULT_OK) {
                Log.d("TAG onActivityResult", "== RESULT_OK");
               if(result.getData() != null ){
                   Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                   try {

                       //Log.d("OnActivityResult", " AutoLoggInn, We CLEAR AND SETT getSharedPreferences");
                       //getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();

                       task.getResult(com.google.android.gms.common.api.ApiException.class); // .getResult(ApiException.class);
                       //Toast.makeText(getApplicationContext(), "Token: " + task.getResult().getIdToken(), Toast.LENGTH_SHORT).show();
                       firebaseAuthWithGoogle(task.getResult().getIdToken());

                       //editor.putString("key_name", task.getResult().getIdToken() );
                       //editor.commit(); // commit changes

                   } catch (ApiException e) {
                       Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                       Log.d("LOG INN EX", "signInWithCredential: wrong!!");
                   }

               }
           }else{
               Log.d("TAG startForResult", "this is NUll ???");
               progressBar.setVisibility(View.GONE);
               Toast.makeText(getApplicationContext(), "resultCode=RESULT_CANCELED", Toast.LENGTH_SHORT).show();
           }

        }
    });

    void signIn(){

        Intent signInIntent = gsc.getSignInIntent();

        startForResult.launch(signInIntent);

        Log.d("TAG signIn()", "signInIntent is called" + signInIntent);
    }
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        Log.d("START auth_with_google", "We CLEAR getSharedPreferences");
        //getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();
        FirebaseUser user = mAuth.getCurrentUser();

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /** For Debug */
                            /** Sign in success, update UI with the signed-in user's information */
                            //Log.d("TAG", "signInWithCredential:success");
                            //Toast.makeText(getApplicationContext(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                            //FirebaseUser user = mAuth.getCurrentUser();
                            //Log.d("TAG FIREBASE user","user is: " + user.getDisplayName());
                            //Log.d("TAG FIREBASE user","user is: " + user.getUid());
                           //updateUI(user);
                        } else {
                            /** For Debug */
                            // If sign in fails, display a message to the user.

                            //ToDo: Message Bar bottom of app to user!!!
                            //Log.d("TAG", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(getApplicationContext(), "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if(task.isComplete() && task.isSuccessful()){
                            // ToDo: fix this only if it is her, tried a empty container inn between fragments
                            /** works Need to refresh this  **/
                            //SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            //SharedPreferences.Editor editor = pref.edit();


                            /// Inn use !!! get the uid on all logged inn family's /// moved again to FragmentMainClass

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

                                        if (task.getResult().child("role").getValue().toString().equals("owner")) { /** OK */

                                            Log.d("TAG", "role owner we use its Own uid loggInn Data " + mAuth.getCurrentUser().getUid());
                                            //editor.clear();
                                            getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();
                                            editor.putString("key_name", mAuth.getCurrentUser().getUid()); // Storing string
                                            editor.commit(); // commit changes



                                            Log.d("TAG FIREBASE user","user is: " + user.getDisplayName());
                                            Log.d("TAG FIREBASE user","user is: " + user.getUid());

                                            Log.d("UID",  "User is: " + pref.getString("key_name","NO_USER" ));

                                            updateUI(user);



                                        }
                                        if (task.getResult().child("role").getValue().toString().equals("member")) {

                                            Query uid_member = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                                                    .child("FamilyUid").child("hashFamily");

                                            uid_member.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task_has_family) {
                                                    if (task_has_family.getResult().exists()) {
                                                        Log.d("QUERY hashFamily", "Exists Data Count -hashFamily: " + task_has_family.getResult().getChildrenCount());
                                                        Log.d("QUERY hashFamily", "Key: " + task_has_family.getResult().child(mAuth.getCurrentUser().getUid()).getValue());
                                                        //editor.clear();
                                                        getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();


                                                        Log.d("Member check", "sett's editor with: " + task_has_family.getResult().child(mAuth.getCurrentUser().getUid()).getValue().toString());


                                                        editor.putString("key_name", task_has_family.getResult().child(mAuth.getCurrentUser().getUid()).getValue().toString());
                                                        editor.commit(); // commit changes

                                                    } else {
                                                        Log.d("QUERY hashFamily", "Don't Exists Error !!! Count hashFamily " + task_has_family.getResult().getChildrenCount());
                                                    }

                                                    if(task_has_family.isComplete() && task_has_family.getResult().exists() ){
                                                        Log.d("TAG FIREBASE user","user is: " + user.getDisplayName());
                                                        Log.d("TAG FIREBASE user","user is: " + user.getUid());

                                                        Log.d("UID",  "User is: " + pref.getString("key_name","NO_USER" ));

                                                        updateUI(user);
                                                    }
                                                }
                                            });
                                        }

                                    }else{
                                        Log.d("TAG", "No User Check on Family if a user is waiting there that now logg's inn !!! : " + mAuth.getCurrentUser().getUid() );
                                        getApplicationContext().getSharedPreferences("MyPref",0).edit().clear().commit();

                                        editor.putString("key_name", "NO_USER");
                                        editor.commit();
                                        Log.d("UID",  "User is: " + pref.getString("key_name","NO_USER" ));

                                        updateUI(user);
                                    }

                                }
                            });

                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
    // [END auth_with_google]

    private void updateUI(FirebaseUser currentUser) {
        navigateToSecondActivity();
        //Toast.makeText(getApplicationContext(), "updateUI", Toast.LENGTH_SHORT).show();
    }

    void navigateToSecondActivity(){
        finish();
        //Intent intent = new Intent(MainActivity.this, Main_t_gui_menu_view.class);
        Intent intent = new Intent(this, FragmentMainClass.class);
        //startActivity(new Intent(this, FragmentMainClass.class));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signIn:
                userLogIn();
                break;
            case R.id.twitter_btn:
                Toast.makeText(this,"Twitter Not implemented!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.facebook_btn:
                //progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(this,"FaceBook Not implemented!!", Toast.LENGTH_LONG).show();
                break;
            case R.id.google_btn:
                progressBar.setVisibility(View.VISIBLE);
                Log.d("AUTH", "call: signIn()" );
                signIn();
                break;
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

    private void userLogIn() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, Main_t_gui_menu_view.class));
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Verify you account!! Please check your email account!!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "User not found!! Check your credentials or Register an Account!!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}





