package com.example.SocialMedia1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SocialMedia1.Model.Courses;

import java.util.List;

import SocialMedia1.R;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CoursesViewHolder>{

    Context mContext;
     List<Courses> mlistCourse;

    public CoursesAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData( List<Courses> list){
        mlistCourse =list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses,parent,false);
        return  new CoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        Courses courses = mlistCourse.get(position);

        if(courses == null){
            return;
        }

        holder.imgCourses.setImageResource(courses.getResourceId());
        holder.tvTitle.setText(courses.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mlistCourse != null){
            return mlistCourse.size();
        }
        return 0;
    }

    public class CoursesViewHolder extends RecyclerView.ViewHolder{
         ImageView imgCourses;
         TextView tvTitle;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCourses = itemView.findViewById(R.id.img_courses);
            tvTitle = itemView.findViewById(R.id.tv_name);
        }
    }
}
