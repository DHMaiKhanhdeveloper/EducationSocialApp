package com.example.SocialMedia1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import SocialMedia1.R;

public class Login extends AppCompatActivity {
    Button sign;
    GoogleSignInClient googleSignInClient;
    TextView goToSignUp;

    EditText email,password;
    Button login;
    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FirebaseApp.initializeApp(Login.this);
        init();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();




        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,MainActivity.class));

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e=email.getText().toString();
                String p=password.getText().toString();

                if (e.isEmpty())
                {
                    email.setError("Please enter valid email..");
                }else if (p.isEmpty())
                {
                    password.setError("Please enter password..");
                }else
                {
                    progressDialog=new ProgressDialog(Login.this);
                    progressDialog.setTitle("Logging");
                    progressDialog.setMessage("Please wait..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    auth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                startActivity(new Intent(Login.this,HomeActivity.class));
                                Toast.makeText(Login.this, "Login success...", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }else
                            {
                                Toast.makeText(Login.this, "Failed to login "+task.getException(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });







    }




    private void init()
    {
        sign=findViewById(R.id.signIn);
        goToSignUp=findViewById(R.id.goToSignUp);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
    }
}