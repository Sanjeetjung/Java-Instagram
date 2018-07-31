package com.atwebpages.awaillixa.merofirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment{

RecyclerView showList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        //HomeAdapter homeAdapter = new HomeAdapter();
        showList = view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showList.setLayoutManager(layoutManager);

        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
fetchFeedFromDatabase();
        return view;
    }



    private void fetchFeedFromDatabase(){
        FirebaseDatabase.getInstance().getReference().child("posts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("create","inside on data change");
                        List<DAO>postList = new ArrayList<>();
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()){
                            Log.d("create","inside has next");
                            DataSnapshot snap = iterator.next();
                            postList.add(snap.getValue(DAO.class));
                        }
                        Log.d("create","size of post list is:"+postList.size());
                        HomeAdapter homeAdapter = new HomeAdapter(postList, new HomeAdapter.OnFeedClickHandleListener() {
                            @Override
                            public void onFeedClicked(DAO post) {
                                Intent intent = new Intent(getContext(),Post_Detail.class);
                                intent.putExtra("title",post.getCaption());
                                intent.putExtra("description",post.getDescription());
                                intent.putExtra("postId",post.getPostId());
                                intent.putExtra("image",post.getImageUrl());

                                startActivity(intent);
                                //Toast.makeText(getContext(), "Clicked feed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        showList.setAdapter(homeAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

}
