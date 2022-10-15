package com.fwa.app.familyshoppingplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerUser;
    private EditText editTextFullname, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private Button backButtonRegUserBtn;


    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        backButtonRegUserBtn = (Button) findViewById(R.id.backButtonRegUser);
        backButtonRegUserBtn.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullname = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword  = (EditText) findViewById(R.id.password);

        progressBar  = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event){
        if(key_code == KeyEvent.KEYCODE_BACK){
            super.onKeyDown(key_code, key_event);
            Toast.makeText(getApplicationContext(), "back press",
                    Toast.LENGTH_LONG).show();

            startActivity(new Intent(this,MainActivity.class));

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //case R.id.banner:
                //startActivity(new Intent(this,MainActivity.class));
            //    break;
            case R.id.registerUser:
                registerUser();
                break;
            case R.id.backButtonRegUser:
                startActivity(new Intent(this,MainActivity.class));
                break;


        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullname.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        String id = database.getReference().push().getKey();

        if(fullName.isEmpty()){
            editTextFullname.setError("Full name is required!");
            editTextFullname.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        if(age.isEmpty()){
            editTextAge.setError("Age is required!");
            editTextAge.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    User user = new User(fullName, age, email);
                    database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Family").child("UsersGroup").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(RegisterUser.this,"Successfully registered!! Check your Email to Verify account!!", Toast.LENGTH_LONG).show();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.sendEmailVerification();
                                startActivity(new Intent(RegisterUser.this, MainActivity.class));

                                // redirect to login Layout! // to main app view not logInn as if user just mad a successfully user we don't have to
                                // log them inn one more time !!!
                            }else{
                                Toast.makeText(RegisterUser.this,"Faild to register! Pleas try again!", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }else{
                    Toast.makeText(RegisterUser.this,"Faild to register! Pleas try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}