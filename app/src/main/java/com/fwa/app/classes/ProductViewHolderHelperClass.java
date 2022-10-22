package com.fwa.app.classes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fwa.app.familyshoppingplanner.R;

public class ProductViewHolderHelperClass extends RecyclerView.ViewHolder {
    public TextView text_name;
    public ProductViewHolderHelperClass(@NonNull View itemView) {
        super(itemView);
        text_name = itemView.findViewById(R.id.txt_name);
    }
}
