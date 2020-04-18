package com.coursehub.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.coursehub.app.ModelClass;
import com.coursehub.app.R;
import com.coursehub.app.RecycleAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LatestFragment extends Fragment {

    private ShimmerFrameLayout shimmerFrameLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;
    private List<ModelClass> modelClassList;
    private RecyclerView recyclerView;
    private RecycleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_latest, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        modelClassList = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swip);
        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmerAnimation();
                modelClassList.clear();

            }
        });
        loadData();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        return view;
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        db = FirebaseFirestore.getInstance();
        db.collection("Latest").orderBy("timeStamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            for (DocumentSnapshot d : task.getResult()) {
                                ModelClass modelClass = new ModelClass(d.getString("courseTitle"), d.getString("courseBody"), d.getString("courseLink"));
                                modelClassList.add(modelClass);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        showData();
    }

    private void showData() {
        swipeRefreshLayout.setRefreshing(false);
        adapter = new RecycleAdapter(modelClassList);
        recyclerView.setAdapter(adapter);
    }
}

