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
        mobilePattern = Pattern.compile(mobileRegex); // x??c ?????nh m???u di ?????ng
        mobileMatcher = mobilePattern.matcher(strPhone);
//        mobileMatcherParent = mobilePattern.matcher(strSdtPhuHuynh);






        if (TextUtils.isEmpty(strName)) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p ?????y ????? h??? v?? t??n c???a b???n", Toast.LENGTH_LONG).show();
            edtName.setError("B???t bu???c nh???p h??? v?? t??n");
            utilService.showSnackBar(v,"Vui l??ng nh???p ?????y ????? h??? v?? t??n c???a b???n");
            edtName.requestFocus();// yeu cau nhap lai

        } else if (TextUtils.isEmpty((strJob))) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p c??ng vi???c c???a b???n", Toast.LENGTH_LONG).show();
            radiobtnJobSelected.setError("Vui l??ng nh???p c??ng vi???c c???a b???n");
            utilService.showSnackBar(v,"Vui l??ng nh???p c??ng vi???c c???a b???n");
            radiobtnJobSelected.requestFocus();
        } else if (radioGroupJob.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p c??ng vi???c c???a b???n", Toast.LENGTH_LONG).show();
            radiobtnJobSelected.setError("Vui l??ng nh???p c??ng vi???c c???a b???n");
            utilService.showSnackBar(v,"Vui l??ng nh???p c??ng vi???c c???a b???n");
            radiobtnJobSelected.requestFocus();
        } else if (TextUtils.isEmpty((strGender))) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p gi???i t??nh c???a b???n", Toast.LENGTH_LONG).show();
            radiobtnGenderSelected.setError("B???t bu???c nh???p gi???i t??nh");
            utilService.showSnackBar(v,"Vui l??ng nh???p gi???i t??nh c???a b???n");
            radiobtnGenderSelected.requestFocus();
        } else if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(SetupActivity.this, "Vui l??ng ch???n gi???i t??nh c???a b???n", Toast.LENGTH_LONG).show();
            radiobtnGenderSelected.setError("B???t bu???c ph???i ch???n gi???i t??nh");
            utilService.showSnackBar(v,"Vui l??ng ch???n gi???i t??nh c???a b???n");
            radiobtnGenderSelected.requestFocus();
        } else if (TextUtils.isEmpty(strPhone)) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i c???a b???n ", Toast.LENGTH_LONG).show();
            edtPhone.setError("B???t bu???c nh???p s??? ??i???n tho???i");
            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i c???a b???n");
            edtPhone.requestFocus();
        } else if (strPhone.length() != 10) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i c???a b???n ", Toast.LENGTH_LONG).show();
            edtPhone.setError("??i???n tho???i di ?????ng ph???i c?? 10 ch??? s???");
            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i c???a b???n");
            edtPhone.requestFocus();
        } else if (!mobileMatcher.find()) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i c???a b???n ", Toast.LENGTH_LONG).show();
            edtPhone.setError("??i???n tho???i di ?????ng kh??ng h???p l???");
            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i c???a b???n");
            edtPhone.requestFocus();
        }
//        else if (TextUtils.isEmpty(strSdtPhuHuynh)) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i ph??? huynh c???a b???n ", Toast.LENGTH_LONG).show();
//            edtSdtPhuHuynh.setError("B???t bu???c nh???p s??? ??i???n tho???i ph??? huynh");
//            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i ph??? huynh c???a b???n");
//            edtSdtPhuHuynh.requestFocus();
//        } else if (strSdtPhuHuynh.length() != 10) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i c???a b???n ", Toast.LENGTH_LONG).show();
//            edtSdtPhuHuynh.setError("??i???n tho???i di ?????ng ph???i c?? 10 ch??? s???");
//            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i c???a b???n");
//            edtSdtPhuHuynh.requestFocus();
//        } else if (!mobileMatcherParent.find()) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i ph??? huynh c???a b???n ", Toast.LENGTH_LONG).show();
//            edtSdtPhuHuynh.setError("??i???n tho???i di ?????ng kh??ng h???p l???");
//            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i ph??? huynh c???a b???n");
//            edtSdtPhuHuynh.requestFocus();
//        } else if (TextUtils.isEmpty(strTinhThanhPho)) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? t???nh/th??n ph??? c???a b???n ", Toast.LENGTH_LONG).show();
//            edtTinhThanhPho.setError("B???t bu???c nh???p t???nh/th??n ph??? ");
//            utilService.showSnackBar(v,"Vui l??ng nh???p s??? t???nh/th??n ph??? c???a b???n");
//            edtTinhThanhPho.requestFocus();
//        }else if (TextUtils.isEmpty(strQuanHuyen)) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p qu???n huy???n c???a b???n ", Toast.LENGTH_LONG).show();
//            edtQuanHuyen.setError("B???t bu???c nh???p qu???n huy???n");
//            utilService.showSnackBar(v,"Vui l??ng nh???p qu???n huy???n c???a b???n");
//            edtQuanHuyen.requestFocus();
//        }else if (TextUtils.isEmpty(strDiaChiCuThe)) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p ?????a ch??? c??? th??? c???a b???n ", Toast.LENGTH_LONG).show();
//            edtDiaChiCuThe.setError("B???t bu???c nh???p ?????a ch??? c??? th???");
//            utilService.showSnackBar(v,"Vui l??ng nh???p ?????a ch??? c??? th??? c???a b???n");
//            edtDiaChiCuThe.requestFocus();
//        }
        else if (TextUtils.isEmpty(strTruongHoc)) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p t??n tr?????ng h???c c???a b???n ", Toast.LENGTH_LONG).show();
            edtTruongHoc.setError("B???t bu???c nh???p t??n tr?????ng h???c");
            utilService.showSnackBar(v,"Vui l??ng nh???p t??n tr?????ng h???c c???a b???n");
            edtTruongHoc.requestFocus();
        }else if (TextUtils.isEmpty(strLop)) {
            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p l???p c???a b???n ", Toast.LENGTH_LONG).show();
            edtLop.setError("B???t bu???c nh???p s??? l???p");
            utilService.showSnackBar(v,"Vui l??ng nh???p l???p c???a b???n");
            edtLop.requestFocus();
        }
//        else if (TextUtils.isEmpty(strTenPhuHuynh)) {
//            Toast.makeText(SetupActivity.this, "Vui l??ng nh???p s??? ??i???n tho???i c???a b???n ", Toast.LENGTH_LONG).show();
//            edtTenPhuHuynh.setError("B???t bu???c nh???p s??? ??i???n tho???i");
//            utilService.showSnackBar(v,"Vui l??ng nh???p s??? ??i???n tho???i c???a b???n");
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