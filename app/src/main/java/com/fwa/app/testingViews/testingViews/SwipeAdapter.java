package com.fwa.app.testingViews.testingViews;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder> {


    class SwipeViewHolder extends RecyclerView.ViewHolder{

        public SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }




    @NonNull
    @Override
    public SwipeAdapter.SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeAdapter.SwipeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
