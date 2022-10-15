package com.fwa.app.familyshoppingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


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
    ImageView googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Google - Sosial Nettwork login
        googleBtn = findViewById(R.id.google_btn);
        googleBtn.setOnClickListener(this);

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




    }


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

           if(result != null && result.getResultCode() == RESULT_OK) {

               if(result.getData() != null ){
                   Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                   try {

                       task.getResult(com.google.android.gms.common.api.ApiException.class); // .getResult(ApiException.class);
                       Toast.makeText(getApplicationContext(), "Token: " + task.getResult().getIdToken(), Toast.LENGTH_SHORT).show();
                       firebaseAuthWithGoogle(task.getResult().getIdToken());


                   } catch (ApiException e) {
                       Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                       Log.d("TAG", "signInWithCredential: wrong!!");
                   }

               }
           }

        }
    });

    void signIn(){

        // old onActivityResult(int ......
        // Old deprecated
        //Intent signInIntent = gsc.getSignInIntent();
        //startActivityForResult(signInIntent,1000);

        Intent signInIntent = gsc.getSignInIntent();
        startForResult.launch(signInIntent);

    }

/*
    // Old deprecated
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                //GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.i("TAG", "firebaseAuthWithGoogle: Id " + account.getId() + " Token: " + account.getIdToken() );


                //Toast.makeText(getApplicationContext(), "firebaseAuthWithGoogle: Id " + task.getResult().getId(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Token: " + task.getResult().getIdToken(), Toast.LENGTH_SHORT).show();


                firebaseAuthWithGoogle(task.getResult().getIdToken());


                //navigateToSecondActivity();




            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "signInWithCredential: wrong!!");
            }
        }

    }
*/

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /** For Debug */
                            /** Sign in success, update UI with the signed-in user's information */
                            Log.d("TAG", "signInWithCredential:success");
                            Toast.makeText(getApplicationContext(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            /** For Debug */
                            // If sign in fails, display a message to the user.

                            //ToDo: Message Bar bottom of app to user!!!
                            Log.d("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
    // [END auth_with_google]

    private void updateUI(FirebaseUser currentUser) {
        navigateToSecondActivity();
        Toast.makeText(getApplicationContext(), "updateUI", Toast.LENGTH_SHORT).show();
    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(MainActivity.this, Main_t_gui_menu_view.class);
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
            case R.id.google_btn:
                progressBar.setVisibility(View.VISIBLE);
                signIn();
                break;
        }
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





