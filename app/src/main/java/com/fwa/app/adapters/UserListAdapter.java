package com.fwa.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fwa.app.classes.User;
import com.fwa.app.familyshoppingplanner.R;

import java.util.ArrayList;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {


    Context context;
    ArrayList<User> list;

    public UserListAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    //public void setItems(ArrayList<User> userList){ this.list = userList; }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.age.setText(user.getAge());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, email, age;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textname);
            email = itemView.findViewById(R.id.textemail);
            age = itemView.findViewById(R.id.textage);
        }
    }
}
