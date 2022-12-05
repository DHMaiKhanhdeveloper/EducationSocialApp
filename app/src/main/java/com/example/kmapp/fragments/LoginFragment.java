package com.example.kmapp.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kmapp.MainActivity;
import com.example.kmapp.R;
import com.example.kmapp.ReplaceActivity;
import com.example.kmapp.SplashActivity;
import com.example.kmapp.utils_service.UtilService;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {


    private Button btnLogin;
    private EditText edtEmail, edtPassword;
    private String strEmail, strPassword;
    private TextView tvLogin,tvForgotPassword;
    private LinearLayout linearLayoutGoogle;

    private UtilService utilService;
    private boolean passwordVisible;


    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private ImageView imgLogin;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(View view) {

        mAuth = FirebaseAuth.getInstance();

        btnLogin = view.findViewById(R.id.btn_login);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword = view.findViewById(R.id.edt_password);
        tvLogin = view.findViewById(R.id.tv_fragment_login);
        tvForgotPassword = view.findViewById(R.id.tv_forgot_password);
        imgLogin = view.findViewById(R.id.img_fragment_login);

        linearLayoutGoogle = view.findViewById(R.id.linear_google);
        utilService = new UtilService();
        loadingBar = new ProgressDialog(getActivity());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                clickSignIn(v);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplaceActivity) getActivity()).setFragment(new CreateAccountFragment());
//                getActivity().overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
            }
        });

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplaceActivity) getActivity()).setFragment(new CreateAccountFragment());
//                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReplaceActivity) getActivity()).setFragment(new ForgotPasswordFragment());
            }
        });

        linearLayoutGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("Tạo tài khoản google mới");
                loadingBar.setMessage("Vui lòng đợi, trong khi chúng tôi đang tạo Tài khoản mới của bạn...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);
                signIn();
            }
        });
    }


    private void clickSignIn(View view) {

        strEmail = edtEmail.getText().toString().trim();
        strPassword = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(strEmail)) {
            showMessage("Vui lòng nhập email của bạn");
            utilService.showSnackBar(view,"Vui lòng nhập email của bạn");
            edtEmail.setError("Bắt buộc phải nhập email");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            showMessage("Vui lòng nhập lại email của bạn");
            utilService.showSnackBar(view,"Vui lòng nhập email của bạn");
            edtEmail.setError("Bắt buộc phải nhập email");
            edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(strPassword)) {
            showMessage("Vui lòng nhập mật khẩu");
            utilService.showSnackBar(view,"Vui lòng nhập mật khẩu");
            edtEmail.setError("Bắt buộc phải nhập mật khẩu");
            edtEmail.requestFocus();
        } else {

            ShowSignIn(strEmail,strPassword);
        }
    }

    private void ShowSignIn(String email, String password) {
        loadingBar.setTitle("Đăng nhập tài khoản");
        loadingBar.setMessage("Vui lòng đợi, trong khi chúng tôi đang đăng nhập tài khoản của bạn...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (!user.isEmailVerified()) {
                                        showMessage("Please verify your email");
                                    }
                                    sendUserToMainActivity();
                                }else {
                                    String exception = "Error: " + task.getException().getMessage();
                                    Toast.makeText(getContext(), exception, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });

//        startActivity(new Intent(getActivity().getApplicationContext(), SplashActivity.class));
//
//        getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    private void sendUserToMainActivity() {
        if (getActivity() == null)
            return;

        loadingBar.dismiss();
        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
        getActivity().finish(); // chuyển fragment sang activity
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }

                    }
                });

    }

    private void updateUi(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        Map<String, Object> map = new HashMap<>();

        map.put("name", account.getDisplayName());
        map.put("email", account.getEmail());
        map.put("profileImage", String.valueOf(account.getPhotoUrl()));
        map.put("uid", user.getUid());
//        map.put("following", 0);
//        map.put("followers", 0);
//        map.put("status", " ");

        FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            assert getActivity() != null;
                            loadingBar.dismiss();
                            sendUserToMainActivity();

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}