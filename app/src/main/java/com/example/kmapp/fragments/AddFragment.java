package com.example.kmapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kmapp.R;
import com.example.kmapp.adapter.GalleryAdapter;
import com.example.kmapp.models.GalleryImages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddFragment extends Fragment {

    private Uri imageUri;
    private Dialog dialog;

    private EditText edtDesc;
    private ImageView imgShow;
    private RecyclerView rcvShow;
    private ImageButton btnBack, btnNext;
    private List<GalleryImages> list;
    private GalleryAdapter adapterGallery;
    private FirebaseUser user;

    public AddFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        rcvShow.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rcvShow.setHasFixedSize(true);

        list = new ArrayList<>();
        adapterGallery = new GalleryAdapter(list);

        rcvShow.setAdapter(adapterGallery);

        clickListener();

    }

    private void clickListener() {
        adapterGallery.SendImage(new GalleryAdapter.SendImage() {
            @Override
            public void onSend(Uri picUri) {
//                imageUri = picUri;
//
//                Glide.with(getContext())
//                        .load(imageUri)
//                        .into(imgShow);
//
//                imgShow.setVisibility(View.VISIBLE);

                CropImage.activity(picUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(4, 3)
                        .start(getContext(), AddFragment.this);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageReference = storage.getReference().child("Post Images/" + System.currentTimeMillis());

                dialog.show();

                storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    uploadData(uri.toString());
                                }
                            });
                        }else {
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Failed to upload post", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//        btnNext.setOnClickListener(v -> {
//
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            final StorageReference storageReference = storage.getReference().child("Post Images/" + System.currentTimeMillis());
//
//            dialog.show();
//
//            storageReference.putFile(imageUri)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//
//                            storageReference.getDownloadUrl().addOnSuccessListener(uri -> uploadData(uri.toString()));
//
//                        } else {
//                            dialog.dismiss();
//                            Toast.makeText(getContext(), "Failed to upload post", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        });
    }

    private void uploadData(String imageURL) {

//        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
//                .document(firebaseUser.getUid()).collection("Post Images");
//
//        String id = reference.document().getId();
//
//        String description = edtDesc.getText().toString();
//
//        List<String> list = new ArrayList<>();
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("description", description);
//        map.put("imageUrl", imageURL);
//        map.put("timestamp", FieldValue.serverTimestamp());
//
//
//        map.put("name", firebaseUser.getDisplayName());
//        map.put("profileImage", String.valueOf(firebaseUser.getPhotoUrl()));
//
//        map.put("likes", list);
//
//
//        map.put("uid", firebaseUser.getUid());
//
//        reference.document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//
//                    System.out.println();
//                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
//                            Toast.LENGTH_SHORT).show();
//                }
//                dialog.dismiss();
//            }
//
//        });

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid()).collection("Post Images");

        String id = reference.document().getId();

        String description = edtDesc.getText().toString();

        List<String> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("description", description);
        map.put("imageUrl", imageURL);
        map.put("timestamp", FieldValue.serverTimestamp());

        map.put("name", user.getDisplayName());
        map.put("profileImage", String.valueOf(user.getPhotoUrl()));

        map.put("likes", list);

        map.put("uid", user.getUid());

        reference.document(id).set(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println();
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();

                });

    }

    private void init(View view) {
        edtDesc = view.findViewById(R.id.descriptionET);
        imgShow = view.findViewById(R.id.imageView);
        rcvShow = view.findViewById(R.id.recyclerView);
        btnBack = view.findViewById(R.id.backBtn);
        btnNext = view.findViewById(R.id.nextBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.laoding_dialog);
        dialog.getWindow().setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dialog_bg, null));
        dialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Dexter.withContext(getContext())
//                        .withPermissions(
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE
//                        ).withListener(new MultiplePermissionsListener() {
//                            @Override
//                            public void onPermissionsChecked(MultiplePermissionsReport report) {
//                                if (report.areAllPermissionsGranted()) {
//                                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download"); // lấy đường dẫn  read a a stored image
//                                    if (file.exists()) {
//                                        File[] files = file.listFiles();
//                                        assert files != null;
//
//                                        mlistGalleryImages.clear();
//
//                                        for (File file1 : files) {
//
//                                            if (file1.getAbsolutePath().endsWith(".jpg") || file1.getAbsolutePath().endsWith(".png")) { // kiểm tra đuôi đường dẫn
//
//                                                mlistGalleryImages.add(new GalleryImages(Uri.fromFile(file1))); // fromFile tạo uri từ file
//                                                adapterGallery.notifyDataSetChanged();
//
//                                            }
//
//                                        }
//
//
//                                    }
//
//
//                                }
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
//
//                            }
//
//                        }).check();
//            }
//        });

        getActivity().runOnUiThread(() -> Dexter.withContext(getContext())
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download");

                            if (file.exists()) {
                                File[] files = file.listFiles();
                                assert files != null;

                                list.clear();

                                for (File file1 : files) {

                                    if (file1.getAbsolutePath().endsWith(".jpg") || file1.getAbsolutePath().endsWith(".png")) {

                                        list.add(new GalleryImages(Uri.fromFile(file1)));
                                        adapterGallery.notifyDataSetChanged();

                                    }

                                }


                            }

                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                assert result != null;
                imageUri = result.getUri();

                Glide.with(getContext())
                        .load(imageUri)
                        .into(imgShow);

                imgShow.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);

            }

        }

    }
}