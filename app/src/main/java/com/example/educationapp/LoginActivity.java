package com.example.educationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.educationapp.models.User;
import com.example.educationapp.utils_service.UtilService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText edtEmail, edtPassword;
    private String strEmail, strPassword;

    private UtilService utilService;
    private boolean passwordVisible;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        mAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btn_login);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);


        utilService = new UtilService();
        loadingBar = new ProgressDialog(this);


        edtPassword.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= edtPassword.getRight() - edtPassword.getCompoundDrawables()[Right].getBounds().width()) {
                    int selection = edtPassword.getSelectionEnd();
                    if (passwordVisible) {
                        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    edtPassword.setSelection(selection);
                    return true;
                }
            }

            return false;
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, LoginActivity.this);
                clickSignIn(v);
            }
        });


    }
    private void clickSignIn(View view) {

        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();


        if (TextUtils.isEmpty(strEmail)) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập email của bạn", Toast.LENGTH_SHORT).show();
            utilService.showSnackBar(view,"Vui lòng nhập email của bạn");
            edtEmail.setError("Bắt buộc phải nhập email");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập lại email của bạn", Toast.LENGTH_LONG).show();
            utilService.showSnackBar(view,"Vui lòng nhập email của bạn");
            edtEmail.setError("Bắt buộc phải nhập email");
            edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            utilService.showSnackBar(view,"Vui lòng nhập mật khẩu");
            edtEmail.setError("Bắt buộc phải nhập mật khẩu");
            edtEmail.requestFocus();
        } else {

                ShowSignIn();
        }


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser != null)
//        {
//            SendUserToLoginActivity();
//        }else {
//
//        }
//    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendUserToLoginActivity() {
        Intent setupIntent = new Intent(LoginActivity.this, LoginActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void ShowSignIn() {
        loadingBar.setTitle("Đăng nhập");
        loadingBar.setMessage("Vui lòng đợi trong khi chúng tôi cho phép bạn đăng nhập vào Tài khoản của mình.....");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        mAuth.signInWithEmailAndPassword(strEmail ,strPassword ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    SendUserToMainActivity();

                    Toast.makeText(LoginActivity.this, "Bạn đã đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, "Không có tên người đăng nhập " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

}