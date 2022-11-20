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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.educationapp.utils_service.UtilService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText   edtEmail, edtPassword, edtConfirmPassword;
    private String  strEmail, strPassword , strConfirmPassword;
    private Button btnRegister;


    private ProgressDialog loadingBar;
    private UtilService utilService;
    private boolean passwordVisible;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        init();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnRegister =  findViewById(R.id.btn_register);
//        progressBar = findViewById(R.id.progressBar);
        loadingBar = new ProgressDialog(this);
        utilService = new UtilService();

        edtPassword.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= edtPassword.getRight() - edtPassword.getCompoundDrawables()[Right].getBounds().width()) {
                    @SuppressLint("ClickableViewAccessibility") int selection = edtPassword.getSelectionEnd();
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
        edtConfirmPassword.setOnTouchListener((v, event) -> {
            final int Right = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= edtConfirmPassword.getRight() - edtConfirmPassword.getCompoundDrawables()[Right].getBounds().width()) {
                    @SuppressLint("ClickableViewAccessibility") int selection = edtConfirmPassword.getSelectionEnd();
                    if (passwordVisible) {
                        edtConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_off_24, 0);
                        edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible = false;
                    } else {
                        edtConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_visibility_24, 0);
                        edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible = true;
                    }
                    edtConfirmPassword.setSelection(selection);
                    return true;
                }
            }

            return false;
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilService.hideKeyboard(v, RegisterActivity.this);
                clickSignUp(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void clickSignUp(View v) {

        strEmail = edtEmail.getText().toString().trim();

        strPassword = edtPassword.getText().toString().trim();
        strConfirmPassword = edtConfirmPassword.getText().toString().trim();
        Toast.makeText(RegisterActivity.this,strPassword,Toast.LENGTH_LONG).show();





         if (TextUtils.isEmpty((strEmail))) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập email của bạn", Toast.LENGTH_LONG).show();
            edtEmail.setError("Bắt buộc nhập email");
            utilService.showSnackBar(v,"Vui lòng nhập email của bạn");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) { // khác true
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập lại email của bạn", Toast.LENGTH_LONG).show();
            edtEmail.setError(" email không hợp lệ");
            utilService.showSnackBar(v,"Vui lòng nhập email của bạn");
            edtEmail.requestFocus();

        }      else if (TextUtils.isEmpty(strPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            edtPassword.setError("Bắt buộc phải nhập mật khẩu");
            utilService.showSnackBar(v,"Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
        } else if (strPassword.length() < 6) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu phải có ít nhất 6 kí tự ", Toast.LENGTH_LONG).show();
            edtPassword.setError("Mật khẩu quá yếu");
            utilService.showSnackBar(v,"Mật khẩu phải có ít nhất 6 kí tự");
            edtPassword.requestFocus();
        } else if (TextUtils.isEmpty(strConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập lại mật khẩu ", Toast.LENGTH_LONG).show();
            edtConfirmPassword.setError("Bắt buộc phải nhập mật khẩu xác nhận");
            utilService.showSnackBar(v,"Vui lòng nhập lại mật khẩu");
            edtConfirmPassword.requestFocus();
        } else if (!strPassword.equals(strConfirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập cùng một mật khẩu ", Toast.LENGTH_LONG).show();
            edtConfirmPassword.setError("Bắt buộc phải nhập mật khẩu xác nhận");
            utilService.showSnackBar(v,"Vui lòng nhập cùng một mật khẩu ");
            edtPassword.clearComposingText();
            edtConfirmPassword.clearComposingText();

        } else {

             ClickRegister();

        }

    }

    private void ClickRegister() {

        loadingBar.setTitle("Tạo tài khoản mới");
        loadingBar.setMessage("Vui lòng đợi, trong khi chúng tôi đang tạo Tài khoản mới của bạn...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);
        mAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    SendUserToSetupActivity();

                    Toast.makeText(RegisterActivity.this, "Bạn đã xác thực thành công...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, "Xuất hiện lỗi: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }



    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);

    }
}