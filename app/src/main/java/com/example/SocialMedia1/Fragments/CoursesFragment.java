package com.example.SocialMedia1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.SocialMedia1.Adapter.CoursesAdapter;
import com.example.SocialMedia1.Model.Courses;

import java.util.ArrayList;
import java.util.List;

import SocialMedia1.R;


public class CoursesFragment extends Fragment {

    ImageSlider imageSlider;
    RecyclerView rcvCourses247;
    CoursesAdapter coursesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_courses, container, false);

        imageSlider = view.findViewById(R.id.image_slider);
        rcvCourses247 = view.findViewById(R.id.rcv_247);
        coursesAdapter = new CoursesAdapter(getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

        rcvCourses247.setLayoutManager(linearLayoutManager);

        coursesAdapter.setData(getCourses());
        rcvCourses247.setAdapter(coursesAdapter);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.img_1,"Tuyển Sinh 247", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.img_2,"Học cùng marathon", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.img_3,"Vui học", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.img_4,"Học cùng Moon", ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels);
        return view;
    }

    private List<Courses> getCourses() {
        List<Courses> listCourses = new ArrayList<>();

        listCourses.add(new Courses(R.drawable.img_7,"Luyện Thi Cấp Tốc Vật Lý"));
        return listCourses;
    }

}

