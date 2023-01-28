package com.example.kmapp.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kmapp.R;
import com.example.kmapp.adapter.HomeAdapter;
import com.example.kmapp.models.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {


    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<Home> list;
    private FirebaseUser user;
    private HomeAdapter homeAdapter;
    private DocumentReference userReference;
    private Activity activity;


    public HomeFragment() {

    }

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

        activity = getActivity();
        init(view);
        list = new ArrayList<>();
        homeAdapter = new HomeAdapter(list, getActivity());
        recyclerView.setAdapter(homeAdapter);

        loadDataFromFirestore();

        homeAdapter.OnPressed(new HomeAdapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
                        .document(uid)
                        .collection("Post Images")
                        .document(id);

                if (likeList.contains(user.getUid())) {
                    likeList.remove(user.getUid()); // unlike
                } else {
                    likeList.add(user.getUid()); // like
                }

                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);

                reference.update(map);
            }

            @Override
            public void setCommentCount(TextView textView) {
                commentCount.observe((LifecycleOwner) activity, integer -> {

                    assert commentCount.getValue() != null;

                    if (commentCount.getValue() == 0) {
                        textView.setVisibility(View.GONE);
                    } else
                        textView.setVisibility(View.VISIBLE);

                    StringBuilder builder = new StringBuilder();
                    builder.append("See all")
                            .append(commentCount.getValue())
                            .append(" comments");

                    textView.setText(builder);
//                    textView.setText("See all " + commentCount.getValue() + " comments");

                });
            }
        });
    }



    private void init(View view){
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Trang chủ");
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();





    }

    private void loadDataFromFirestore() {
//        final DocumentReference reference = FirebaseFirestore.getInstance().collection("Users")
//                .document(user.getUid());
//
//        final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users");
//
//        reference.addSnapshotListener((value, error) -> {
//
//            if (error != null) {
//                Log.d("Error: ", error.getMessage());
//                return;
//            }
//
//            if (value == null)
//                return;
//
//            Map<String, Object> followerMap= new HashMap<>();
//            followerMap= value.getData();
//            assert followerMap!= null;
//
//            for (Map.Entry<String, Object> e : followerMap.entrySet()) {
//
//
//                List<Object> uidList = (List<Object>) value.get("following");
//
//                uidList.add(e.getValue());
//
//
//                if (uidList == null || uidList.isEmpty())
//                    return;
//
//                collectionReference.whereIn("uid", uidList)
//                        .addSnapshotListener((value1, error1) -> {
//
//                            if (error1 != null) {
//                                Log.d("Error: ", error1.getMessage());
//                            }
//
//                            if (value1 == null)
//                                return;
//
//                            for (QueryDocumentSnapshot snapshot : value1) {
//
//                                snapshot.getReference().collection("Post Images")
//                                        .addSnapshotListener((value11, error11) -> {
//
//                                            if (error11 != null) {
//                                                Log.d("Error: ", error11.getMessage());
//                                            }
//
//                                            if (value11 == null)
//                                                return;
//
//                                            list.clear();
//
//                                            for (final QueryDocumentSnapshot snapshot1 : value11) {
//
//                                                if (!snapshot1.exists())
//                                                    return;
//
//                                                Home model = snapshot1.toObject(Home.class);
//
//                                                list.add(new Home(
//                                                        model.getName(),
//                                                        model.getProfileImage(),
//                                                        model.getImageUrl(),
//                                                        model.getUid(),
//                                                        model.getDescription(),
//                                                        model.getId(),
//                                                        model.getTimestamp(),
//                                                        model.getLikes()));
//
//                                            snapshot1.getReference().collection("Comments").get()
//                                                    .addOnCompleteListener(task -> {
//
//                                                        if (task.isSuccessful()) {
//
//                                                            Map<String, Object> map = new HashMap<>();
//                                                            for (QueryDocumentSnapshot commentSnapshot : task
//                                                                    .getResult()) {
//                                                                map = commentSnapshot.getData();
//                                                            }
//
//                                                            commentCount.setValue(map.size());
//
//                                                        }
//
//                                                    });
//
//                                            }
//                                            homeAdapter.notifyDataSetChanged();
//
//                                        });
//
//                            }
//
//                        });
//
////            loadStories(uidList);
//            }
//
//        });

        final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid())
                .collection("Post Images");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
                return;
            }

            if (value == null)
                    return;

            list.clear();


                for (QueryDocumentSnapshot snapshot : value) {

                    if (!snapshot.exists())
                        return;

                    Home model = snapshot.toObject(Home.class);
                    list.add(new Home(model.getName(),
                            model.getProfileImage(),
                            model.getImageUrl(),
                            model.getUid(),
                            model.getDescription(),
                            model.getId(),
                            model.getTimestamp(),
                            model.getLikes()));

                }
                homeAdapter.notifyDataSetChanged();

            }
        });
    }
}