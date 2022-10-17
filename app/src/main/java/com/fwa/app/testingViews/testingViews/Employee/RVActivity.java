package com.fwa.app.testingViews.testingViews.Employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.fwa.app.familyshoppingplanner.R;
import com.google.firebase.database.DataSnapshot;

public class RVActivity extends AppCompatActivity
{
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    //RVAdapter adapter;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    DAOEmployee dao;
    boolean isLoading=false;
    String key =null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);
        swipeRefreshLayout = findViewById(R.id.swip);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        //adapter= new RVAdapter(this);
        //recyclerView.setAdapter(adapter);
        dao = new DAOEmployee();


        FirebaseRecyclerOptions<Employee> options =
                new FirebaseRecyclerOptions.Builder<Employee>()
                        .setQuery(dao.get(), new SnapshotParser<Employee>() {
                            @NonNull
                            @Override
                            public Employee parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Employee emp = snapshot.getValue(Employee.class);
                                emp.setKey(snapshot.getKey());
                                return emp;
                            }
                        }).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter(options) {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(RVActivity.this).inflate(R.layout.layout_item,parent,false);
                return new EmployeeVH(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {
                EmployeeVH vh = (EmployeeVH) holder;
                Employee emp = (Employee)model;
                vh.txt_name.setText(emp.getName());
                vh.txt_position.setText(emp.getPosition());
                vh.txt_option.setOnClickListener(v->
                {
                    PopupMenu popupMenu =new PopupMenu(RVActivity.this,vh.txt_option);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(item->
                    {
                        switch (item.getItemId())
                        {
                            case R.id.menu_edit:
                                Intent intent=new Intent(RVActivity.this,EmployeeMainActivity.class);
                                intent.putExtra("EDIT",emp);
                                startActivity(intent);
                                break;
                            case R.id.menu_remove:
                                DAOEmployee dao=new DAOEmployee();
                                dao.remove(emp.getKey()).addOnSuccessListener(suc->
                                {
                                    Toast.makeText(RVActivity.this, "Record is removed", Toast.LENGTH_SHORT).show();
                                    //notifyItemRemoved(position);
                                    //list.remove(emp);
                                }).addOnFailureListener(er->
                                {
                                    Toast.makeText(RVActivity.this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                                break;
                        }
                        return false;
                    });
                    popupMenu.show();
                });
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                Toast.makeText(RVActivity.this, "Data change", Toast.LENGTH_SHORT).show();

            }

        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //adapter.startListening();
        //loadData();
/*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem< lastVisible+3)
                {
                    if(!isLoading)
                    {
                        isLoading=true;
                        loadData();
                    }
                }
            }
        });

 */
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
/*
    private void loadData()
    {

        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ArrayList<Employee> emps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren())
                {
                    Employee emp = data.getValue(Employee.class);
                    emp.setKey(data.getKey());
                    emps.add(emp);
                    key = data.getKey();
                }
                adapter.setItems(emps);
                adapter.notifyDataSetChanged();
                isLoading =false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

 */
}
