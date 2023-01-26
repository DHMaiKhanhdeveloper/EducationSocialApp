package com.example.kmapp.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.kmapp.utils_service.UtilService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateAccountFragment extends Fragment {

    private EditText edtName,edtEmail, edtPassword, edtConfirmPassword;
    private TextView tvRegister;
    private ImageView imgRegister;
    private String  strName,strEmail, strPassword , strConfirmPassword;
    private Button btnRegister;
    private LinearLayout linearGoogle;

    private ProgressDialog loadingBar;
    private UtilService utilService;
    private boolean passwordVisible;
    private FirebaseAuth mAuth;

    public CreateAccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        changeStatusBarColor();
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(View view) {
        mAuth = FirebaseAuth.getInstance();

        edtName  = view.findViewById(R.id.edt_name);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPassword =  view.findViewById(R.id.edt_password);
        edtConfirmPassword =  view.findViewById(R.id.edt_confirm_password);

        tvRegister = view.findViewById(R.id.tv_fragment_register);
        imgRegister =  view.findViewById(R.id.img_fragment_register);
        btnRegister =   view.findViewById(R.id.btn_register);
        linearGoogle = view.findViewById(R.id.linear_google);

        loadingBar = new ProgressDialog(getActivity());
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
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                clickSignUp(view);
            }
        });

        imgRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplaceActivity) getActivity()).setFragment(new LoginFragment());
//                getActivity().overridePendingTransition(R.anim.slide_in_from_left,R.anim.slide_out_to_right);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReplaceActivity) getActivity()).setFragment(new LoginFragment());
//                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
    }

    private void clickSignUp(View v) {

        strName =edtName.getText().toString().trim();
        strEmail = edtEmail.getText().toString().trim();

        strPassword = edtPassword.getText().toString().trim();
        strConfirmPassword = edtConfirmPassword.getText().toString().trim();




        if (TextUtils.isEmpty((strName))) {
            showMessage("Vui lòng nhập name của bạn");
            edtName.setError("Bắt buộc nhập name ");
            utilService.showSnackBar(v,"Vui lòng nhập name của bạn");
            edtName.requestFocus();
        } else if (TextUtils.isEmpty((strEmail))) {
            showMessage("Vui lòng nhập email của bạn");
            edtEmail.setError("Bắt buộc nhập email");
            utilService.showSnackBar(v,"Vui lòng nhập email của bạn");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) { // khác true
            showMessage("Vui lòng nhập lại email của bạn");
            edtEmail.setError(" email không hợp lệ");
            utilService.showSnackBar(v,"Vui lòng nhập email của bạn");
            edtEmail.requestFocus();
        }else if (TextUtils.isEmpty(strPassword)) {
            showMessage("Vui lòng nhập mật khẩu");
            edtPassword.setError("Bắt buộc phải nhập mật khẩu");
            utilService.showSnackBar(v,"Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
        } else if (strPassword.length() < 6) {
            showMessage("Vui lòng nhập mật khẩu");
            edtPassword.setError("Mật khẩu quá yếu");
            utilService.showSnackBar(v,"Mật khẩu phải có ít nhất 6 kí tự");
            edtPassword.requestFocus();
        } else if (TextUtils.isEmpty(strConfirmPassword)) {
            showMessage("Vui lòng nhập mật khẩu");
            edtConfirmPassword.setError("Bắt buộc phải nhập mật khẩu xác nhận");
            utilService.showSnackBar(v,"Vui lòng nhập lại mật khẩu");
            edtConfirmPassword.requestFocus();
        } else if (!strPassword.equals(strConfirmPassword)) {
            showMessage("Vui lòng nhập cùng một mật khẩu");
            edtConfirmPassword.setError("Bắt buộc phải nhập mật khẩu xác nhận");
            utilService.showSnackBar(v,"Vui lòng nhập cùng một mật khẩu ");
            edtPassword.clearComposingText();
            edtConfirmPassword.clearComposingText();

        } else {

            ClickRegister(strName,strEmail,strPassword);

        }
    }

    private void ClickRegister(String name, String email, String password) {
        loadingBar.setTitle("Tạo tài khoản mới");
        loadingBar.setMessage("Vui lòng đợi, trong khi chúng tôi đang tạo Tài khoản mới của bạn...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);
//        ((ReplaceActivity) getActivity()).setFragment(new LoginFragment());
//        getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    String image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwp--EwtYaxkfsSPIpoSPucdbxAo6PancQX1gw6ETSKI6_pGNCZY4ts1N6BV5ZcN3wPbA&usqp=CAU";

                    UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                    request.setDisplayName(name);
                    request.setPhotoUri(Uri.parse(image));
//
                    user.updateProfile(request.build());


                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Email verification link send", Toast.LENGTH_SHORT).show();
                                showAlertDialog();
                            }
                        }
                    });
                    uploadUser(user,name,email);
                    loadingBar.dismiss();
                }else {
                    String exception = task.getException().getMessage();
                    Toast.makeText(getContext(), "Error: " + exception, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }
    private void showAlertDialog() {
        //thiết lập trình tạo cảnh báo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Email chưa được xác minh");
        builder.setMessage("Vui lòng xác minh email của bạn ngay bây giờ. Bạn không thể đăng nhập mà không xác minh email. ");


        builder.setPositiveButton("Tiếp tục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                //Gửi ứng dụng email trong cửa sổ mới và không phải trong ứng dụng của chúng tôi
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void uploadUser(FirebaseUser user, String name, String email) {

        List<Object> list = new ArrayList<>();
        List<Object> list1 = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("profileImage", " ");
        map.put("uid", user.getUid());
        map.put("status", " ");
        map.put("search", name.toLowerCase());

        map.put("followers", list );
        map.put("following", list1 );
        FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            assert getActivity() != null;
                            loadingBar.dismiss();
//                            startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
//                            getActivity().finish();
                            ((ReplaceActivity) getActivity()).setFragment(new LoginFragment());
//                            getActivity().finish();
                        }else {

                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}