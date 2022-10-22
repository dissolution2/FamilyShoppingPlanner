package com.fwa.app.classes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fwa.app.familyshoppingplanner.R;

public class ProductVH extends RecyclerView.ViewHolder{
    public TextView txt_name,txt_amount,txt_storage,txt_company,txt_position,txt_option;
    public ProductVH(@NonNull View itemView)
    {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_company = itemView.findViewById(R.id.txt_company);
        txt_storage = itemView.findViewById(R.id.txt_storage);
        txt_amount = itemView.findViewById(R.id.txt_amount);
        //txt_position = itemView.findViewById(R.id.txt_position);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}
