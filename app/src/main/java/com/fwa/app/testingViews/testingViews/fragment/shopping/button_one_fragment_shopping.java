package com.fwa.app.testingViews.testingViews.fragment.shopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fwa.app.classes.Product;
import com.fwa.app.familyshoppingplanner.R;
//import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link button_one_fragment_shopping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class button_one_fragment_shopping extends Fragment {

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

    public button_one_fragment_shopping() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment buttonOneFragmentShopping.
     */
    // TODO: Rename and change types and number of parameters
    public static button_one_fragment_shopping newInstance(String param1, String param2) {
        button_one_fragment_shopping fragment = new button_one_fragment_shopping();
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
        //Toast.makeText( getActivity(), "On create fragment shopping list", Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        list_view = inflater.inflate(R.layout.fragment_button_one_shopping, container, false);

        recyclerView_list = (RecyclerView) list_view.findViewById(R.id.recycle_one_shopping);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));

        ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("ShoppingList").child("ANNETTE");

        return list_view;
    }
    @Override
    public void onStart() {
        super.onStart();
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
                            //Toast.makeText( getActivity(), "Successfully Query", Toast.LENGTH_LONG).show();
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