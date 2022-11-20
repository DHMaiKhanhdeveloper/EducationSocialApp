package com.example.educationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.educationapp.utils_service.UtilService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView cImagSetup;
    private Button btnSave;
    private EditText edtName, edtPhone, edtTruongHoc, edtLop;
    private RadioButton radiobtnJobSelected, radiobtnGenderSelected;
    private RadioGroup radioGroupJob, radioGroupGender;


    //edtTinhThanhPho,edtQuanHuyen,edtDiaChiCuThe, edtTenPhuHuynh, edtSdtPhuHuynh
    // strTinhThanhPho, strQuanHuyen,strDiaChiCuThe,strTenPhuHuynh, strSdtPhuHuynh


    private String strName, strJob, strGender, strPhone, strTruongHoc, strLop ;

    private String mobileRegex;
    private Matcher mobileMatcher,mobileMatcherParent;
    private Pattern mobilePattern;
    private UtilService utilService;
//    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference UsersRef;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        init();
    }
    private void init(){

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        loadingBar = new ProgressDialog(this);

        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        cImagSetup = findViewById(R.id.cir_img);
//        edtTinhThanhPho = findViewById(R.id.edt_tinh_thanh_pho);
//        edtQuanHuyen = findViewById(R.id.edt_quan_huyen);
//        edtDiaChiCuThe  = findViewById(R.id.edt_dia_chi_cu_the);
        edtTruongHoc = findViewById(R.id.edt_truong_hoc);
        edtLop = findViewById(R.id.edt_lop);
//        edtTenPhuHuynh = findViewById(R.id.edt_ten_phu_huynh);
//        edtSdtPhuHuynh  = findViewById(R.id.edt_sdt_phu_huynh);

        radioGroupJob = findViewById(R.id.radio_group_job_setup);
        radioGroupJob.clearCheck();


        radioGroupGender =  findViewById(R.id.radio_group_gender_setup);
        radioGroupGender.clearCheck();


        btnSave = findViewById(R.id.btn_setup);

//        progressBar = findViewById(R.id.progressBar);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickSetup(v);
            }
        });

        cImagSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    private void ClickSetup(View v) {
        strName = edtName.getText().toString().trim();

        int selectedJobId = radioGroupJob.getCheckedRadioButtonId();
        radiobtnJobSelected = findViewById(selectedJobId);
        strJob = radiobtnJobSelected.getText().toString();

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        radiobtnGenderSelected = findViewById(selectedGenderId);
        strGender  = radiobtnGenderSelected.getText().toString();

        strPhone = edtPhone.getText().toString().trim();
//        strTinhThanhPho = edtTinhThanhPho.getText().toString().trim();
//        strQuanHuyen = edtQuanHuyen.getText().toString().trim();
//        strDiaChiCuThe = edtDiaChiCuThe.getText().toString().trim();
        strTruongHoc= edtTruongHoc.getText().toString().trim();
        strLop = edtLop.getText().toString().trim();
//        strTenPhuHuynh = edtTenPhuHuynh.getText().toString().trim();
//        strSdtPhuHuynh = edtSdtPhuHuynh.getText().toString().trim();

        utilService = new UtilService();

        mobileRegex = "[0][0-9]{9}";
        mobilePattern = Pattern.compile(mobileRegex); // xác định mẫu di động
        mobileMatcher = mobilePattern.matcher(strPhone);
//        mobileMatcherParent = mobilePattern.matcher(strSdtPhuHuynh);






        if (TextUtils.isEmpty(strName)) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập đầy đủ họ và tên của bạn", Toast.LENGTH_LONG).show();
            edtName.setError("Bắt buộc nhập họ và tên");
            utilService.showSnackBar(v,"Vui lòng nhập đầy đủ họ và tên của bạn");
            edtName.requestFocus();// yeu cau nhap lai

        } else if (TextUtils.isEmpty((strJob))) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập công việc của bạn", Toast.LENGTH_LONG).show();
            radiobtnJobSelected.setError("Vui lòng nhập công việc của bạn");
            utilService.showSnackBar(v,"Vui lòng nhập công việc của bạn");
            radiobtnJobSelected.requestFocus();
        } else if (radioGroupJob.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập công việc của bạn", Toast.LENGTH_LONG).show();
            radiobtnJobSelected.setError("Vui lòng nhập công việc của bạn");
            utilService.showSnackBar(v,"Vui lòng nhập công việc của bạn");
            radiobtnJobSelected.requestFocus();
        } else if (TextUtils.isEmpty((strGender))) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập giới tính của bạn", Toast.LENGTH_LONG).show();
            radiobtnGenderSelected.setError("Bắt buộc nhập giới tính");
            utilService.showSnackBar(v,"Vui lòng nhập giới tính của bạn");
            radiobtnGenderSelected.requestFocus();
        } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SetupActivity.this, "Vui lòng chọn giới tính của bạn", Toast.LENGTH_LONG).show();
            radiobtnGenderSelected.setError("Bắt buộc phải chọn giới tính");
            utilService.showSnackBar(v,"Vui lòng chọn giới tính của bạn");
            radiobtnGenderSelected.requestFocus();
        } else if (TextUtils.isEmpty(strPhone)) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại của bạn ", Toast.LENGTH_LONG).show();
            edtPhone.setError("Bắt buộc nhập số điện thoại");
            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại của bạn");
            edtPhone.requestFocus();
        } else if (strPhone.length() != 10) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại của bạn ", Toast.LENGTH_LONG).show();
            edtPhone.setError("Điện thoại di động phải có 10 chữ số");
            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại của bạn");
            edtPhone.requestFocus();
        } else if (!mobileMatcher.find()) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại của bạn ", Toast.LENGTH_LONG).show();
            edtPhone.setError("Điện thoại di động không hợp lệ");
            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại của bạn");
            edtPhone.requestFocus();
        }
//        else if (TextUtils.isEmpty(strSdtPhuHuynh)) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại phụ huynh của bạn ", Toast.LENGTH_LONG).show();
//            edtSdtPhuHuynh.setError("Bắt buộc nhập số điện thoại phụ huynh");
//            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại phụ huynh của bạn");
//            edtSdtPhuHuynh.requestFocus();
//        } else if (strSdtPhuHuynh.length() != 10) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại của bạn ", Toast.LENGTH_LONG).show();
//            edtSdtPhuHuynh.setError("Điện thoại di động phải có 10 chữ số");
//            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại của bạn");
//            edtSdtPhuHuynh.requestFocus();
//        } else if (!mobileMatcherParent.find()) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại phụ huynh của bạn ", Toast.LENGTH_LONG).show();
//            edtSdtPhuHuynh.setError("Điện thoại di động không hợp lệ");
//            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại phụ huynh của bạn");
//            edtSdtPhuHuynh.requestFocus();
//        } else if (TextUtils.isEmpty(strTinhThanhPho)) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập số tỉnh/thàn phố của bạn ", Toast.LENGTH_LONG).show();
//            edtTinhThanhPho.setError("Bắt buộc nhập tỉnh/thàn phố ");
//            utilService.showSnackBar(v,"Vui lòng nhập số tỉnh/thàn phố của bạn");
//            edtTinhThanhPho.requestFocus();
//        }else if (TextUtils.isEmpty(strQuanHuyen)) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập quận huyện của bạn ", Toast.LENGTH_LONG).show();
//            edtQuanHuyen.setError("Bắt buộc nhập quận huyện");
//            utilService.showSnackBar(v,"Vui lòng nhập quận huyện của bạn");
//            edtQuanHuyen.requestFocus();
//        }else if (TextUtils.isEmpty(strDiaChiCuThe)) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập địa chỉ cụ thể của bạn ", Toast.LENGTH_LONG).show();
//            edtDiaChiCuThe.setError("Bắt buộc nhập địa chỉ cụ thể");
//            utilService.showSnackBar(v,"Vui lòng nhập địa chỉ cụ thể của bạn");
//            edtDiaChiCuThe.requestFocus();
//        }
        else if (TextUtils.isEmpty(strTruongHoc)) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập tên trường học của bạn ", Toast.LENGTH_LONG).show();
            edtTruongHoc.setError("Bắt buộc nhập tên trường học");
            utilService.showSnackBar(v,"Vui lòng nhập tên trường học của bạn");
            edtTruongHoc.requestFocus();
        }else if (TextUtils.isEmpty(strLop)) {
            Toast.makeText(SetupActivity.this, "Vui lòng nhập lớp của bạn ", Toast.LENGTH_LONG).show();
            edtLop.setError("Bắt buộc nhập số lớp");
            utilService.showSnackBar(v,"Vui lòng nhập lớp của bạn");
            edtLop.requestFocus();
        }
//        else if (TextUtils.isEmpty(strTenPhuHuynh)) {
//            Toast.makeText(SetupActivity.this, "Vui lòng nhập số điện thoại của bạn ", Toast.LENGTH_LONG).show();
//            edtTenPhuHuynh.setError("Bắt buộc nhập số điện thoại");
//            utilService.showSnackBar(v,"Vui lòng nhập số điện thoại của bạn");
//            edtTenPhuHuynh.requestFocus();
//        }
        else {
//            progressBar.setVisibility(View.VISIBLE);
            SaveAccountSetupInformation();


        }


    }

    private void SaveAccountSetupInformation()
    {

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

        HashMap<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("username", strName);
            userMap.put("job", strJob);
            userMap.put("gender", strGender);
            userMap.put("phone", strPhone);
            userMap.put("school", strTruongHoc);
            userMap.put("class", strLop);
            userMap.put("city", "none");
            userMap.put("district", "none");
            userMap.put("address_details", "none");
            userMap.put("username_parent", "none");
            userMap.put("phone_parent", "none");

        UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    SendUserToLoginActivity();
                    Toast.makeText(SetupActivity.this, "your Account is created Successfully.", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
                else
                {
                    String message =  task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });



    }

    private void SendUserToLoginActivity() {
        Intent setupIntent = new Intent(SetupActivity.this, LoginActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}