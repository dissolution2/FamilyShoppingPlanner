package com.fwa.app.testingViews.testingViews.fragment.storage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fwa.app.classes.Product;
import com.fwa.app.familyshoppingplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link button_one_fragment_storage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class button_one_fragment_storage extends Fragment {
    private ProgressBar progressBar;
    private View list_view;
    private RecyclerView recyclerView_list;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");;
    private DatabaseReference ref;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public button_one_fragment_storage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment buttonOneFragmentCall.
     */
    // TODO: Rename and change types and number of parameters
    public static button_one_fragment_storage newInstance(String param1, String param2) {
        button_one_fragment_storage fragment = new button_one_fragment_storage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_button_one_call, container, false);

        list_view = inflater.inflate(R.layout.fragment_button_one_call, container, false);
        progressBar = (ProgressBar) list_view.findViewById(R.id.progressBar2);
        recyclerView_list = (RecyclerView) list_view.findViewById(R.id.recycle_list_one);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));


        ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator");

        return list_view;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(ref,Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

                final String productIds = getRef(position).getKey();

                ref.child(productIds).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String product_name = snapshot.getValue(Product.class).getName();
                            //int product_amount = snapshot.getValue(Product.class).getAmount();

                            holder.product_name.setText(product_name);
                            //holder.product_amount.setText(product_amount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_fragment_card_layout, parent,false);
                ProductViewHolder productViewHolder = new ProductViewHolder(view);
                return productViewHolder;

            }
        };
        progressBar.setVisibility(View.GONE);
        recyclerView_list.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView product_name, product_amount;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.txt_name);
            //product_amount = itemView.findViewById(R.id.txt_position);
        }
    }
}