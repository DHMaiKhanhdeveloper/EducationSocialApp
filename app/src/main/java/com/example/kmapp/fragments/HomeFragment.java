package com.example.kmapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kmapp.R;
import com.example.kmapp.adapter.HomeAdapter;
import com.example.kmapp.models.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<Home> list;
    private FirebaseUser user;
    private HomeAdapter homeAdapter;
    private DocumentReference userReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

        loadDataFromFirestore();
    }



    private void init(View view){
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Trang chủ");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        list = new ArrayList<>();
        homeAdapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(homeAdapter);



    }

    private void loadDataFromFirestore() {

        userReference = FirebaseFirestore.getInstance().collection("Posts")
                .document(user.getUid());
//                .collection("Post Images")
//                .document(id);

        list.add(new Home("Khanh1","10","","","1",1));
        list.add(new Home("Khanh2","11","","","2",2));
        list.add(new Home("Khanh3","12","","","3",3));
        list.add(new Home("Khanh4","9","","","4",4));
        list.add(new Home("Khanh5","8","","","5",5));
        list.add(new Home("Khanh6","7","","","6",6));
        list.add(new Home("Khanh7","6","","","7",7));

    }
}