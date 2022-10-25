package com.fwa.app.testingViews.testingViews.fragment.storage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.fwa.app.classes.Option;
import com.fwa.app.classes.Product;
import com.fwa.app.classes.ProductVH;
import com.fwa.app.classes.ProductViewHolderHelperClass;
import com.fwa.app.database.FirebaseRWQ;
import com.fwa.app.familyshoppingplanner.R;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.fwa.app.testingViews.testingViews.Employee.DAOEmployee;
import com.fwa.app.testingViews.testingViews.Employee.Employee;
import com.fwa.app.testingViews.testingViews.Employee.EmployeeMainActivity;
import com.fwa.app.testingViews.testingViews.Employee.EmployeeVH;
import com.fwa.app.testingViews.testingViews.Employee.RVActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link button_one_fragment_storage#newInstance} factory method to
 * create an instance of this fragment.
 */
/** PLU PRINT ON HOW I DID THIS */
public class button_one_fragment_storage extends Fragment {
    private ProgressBar progressBar;
    private View list_view;
    private RecyclerView recyclerView_list;

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    private FirebaseRWQ firebaseRWQ = new FirebaseRWQ();
    private String user_shopping_list_in_use ="";

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;


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



        mAuth = FirebaseAuth.getInstance();
        list_view = inflater.inflate(R.layout.fragment_button_one_storage, container, false);
        //progressBar = (ProgressBar) list_view.findViewById(R.id.progressBar2);




        recyclerView_list = (RecyclerView) list_view.findViewById(R.id.recycle_list_one);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));


        ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator");

        // this works with delete on option
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(ref, new SnapshotParser<Product>() {
                            @NonNull
                            @Override
                            public Product parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Product emp = snapshot.getValue(Product.class);
                                emp.setKey(snapshot.getKey());
                                return emp;
                            }
                        }).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter(options) {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_fragment_card_layout,parent,false);
                return new ProductVH(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull Object model) {
                //TextView txt_name,txt_amount,txt_storage,txt_company,txt_position,txt_option;
                ProductVH vh = (ProductVH) holder;
                Product emp = (Product) model;

                vh.txt_name.setText(emp.getName().toUpperCase());
                vh.txt_company.setText(emp.getCompany().toUpperCase());
                //int temp = emp.getAmount();
                //String tmpStr10 = String.valueOf(temp);
                vh.txt_amount.setText(String.valueOf(emp.getAmount()));


                //vh.txt_amount.setText(emp.getAmount());
                switch (emp.getStorage()){
                    case "c":
                        vh.txt_storage.setText("STORAGE COLD +4");
                        break;
                    case "f":
                        vh.txt_storage.setText("STORAGE FREEZER -18");
                        break;
                    case "d":
                        vh.txt_storage.setText("STORAGE DRY");
                        break;
                }
                //vh.txt_storage.setText(emp.getStorage());
                //vh.txt_position.setText(emp.getPosition());
                vh.txt_option.setOnClickListener(v->
                {
                    PopupMenu popupMenu =new PopupMenu( getContext(),vh.txt_option);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(item->
                    {
                        switch (item.getItemId())
                        {
                            case R.id.menu_edit:
                                //Intent intent=new Intent(getContext(), EmployeeMainActivity.class);
                                //intent.putExtra("EDIT",emp);
                                //startActivity(intent);
                                break;
                            case R.id.menu_remove:

                                DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("Family").
                                        child("List").
                                        child("Refrigerator").child(emp.getKey()); //.child("N");

                                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                                        if (dataSnapshot.isSuccessful()) {
                                            //ref.getRef().removeValue();
                                        }
                                    }
                                });

                                //DAOEmployee dao=new DAOEmployee();
                                //dao.remove(emp.getKey()).addOnSuccessListener(suc->
                                //{
                                    Toast.makeText(getContext(), "toDo: move Record shoppinglist", Toast.LENGTH_SHORT).show();
                                    //notifyItemRemoved(position);
                                    //list.remove(emp);
                                //}).addOnFailureListener(er->
                                //{
                                //    Toast.makeText(getContext(), ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                                //});

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
            }

        };
        recyclerView_list.setAdapter(firebaseRecyclerAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView_list);
        return list_view;
    }



    @Override
    public void onStart() {
        super.onStart();
        //progressBar.setVisibility(View.VISIBLE);
        firebaseRecyclerAdapter.startListening(); // this belong to // out in onCreateView

/*
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

                //ProductViewHolderHelperClass holderHelperClass = (ProductViewHolderHelperClass) holder;
                Product product = (Product)model;
                //product.getKey()

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

        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView_list);
        recyclerView_list.setAdapter(adapter);
        adapter.startListening();
*/
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView product_name, product_amount;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.txt_name);
            //product_amount = itemView.findViewById(R.id.txt_position);
        }
    }





    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**  REFRIGERATOR RECYCLERVIEW  **/
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //Toast.makeText(getActivity(), "direction : " +direction, Toast.LENGTH_LONG).show();

            if(direction == 8){
                Toast.makeText(getActivity(), "Added to Shopping List" , Toast.LENGTH_LONG).show();
                Log.d("swipe == 8 Key", "Key on swipe 8: " + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
            }
            if(direction == 4){
                Toast.makeText(getActivity(), "Deleted Item from Cold +4", Toast.LENGTH_LONG).show();
                Log.d("swipe == 4 Key", "Key on swipe 8: " + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
            }


            DatabaseReference ref_shopping_list_in_use = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Family").child("List").child("Option");
            ref_shopping_list_in_use.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                            if (dataSnapshot.isSuccessful()) {

                                for (DataSnapshot child : dataSnapshot.getResult().getChildren() ) {
                                    user_shopping_list_in_use = child.getValue().toString();
                                    Toast.makeText(getActivity(), "UserShoppingList: " + user_shopping_list_in_use, Toast.LENGTH_LONG).show();
                                    Log.e("query begin", "UserShoppingList: " + user_shopping_list_in_use);
                                }
                            }
                            if(dataSnapshot.isComplete()){
                                // call next query !!
                                Toast.makeText(getActivity(), "UserShoppingList: " + user_shopping_list_in_use, Toast.LENGTH_LONG).show();
                                Log.e("query end", "UserShoppingList: " + user_shopping_list_in_use);
                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("EXCEPTION", "Failed to get shopping list: " + e.getMessage());
                        }
                    });







            List product_List = new ArrayList();


            /** getKey() from firebaseRecyclerAdapter position of object */
            DatabaseReference refquery = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Family").
                    child("List").
                    child("Refrigerator").child(firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
            if(!firebaseRecyclerAdapter.getSnapshots().isEmpty()){
                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                        if (dataSnapshot.isSuccessful()) {
                            //ToDo: Refactoring needed!!
                            //Log.e("query begin", "getting object key to move");
                            if(direction == 8) {



                                /** Move Product to Shopping list - get first the product snapshot into a product class refactoring later !!*/
                                for (DataSnapshot child : dataSnapshot.getResult().getChildren()) {
                                    Log.d("TAG Key Stor", "child key: "  + child.getKey() );
                                    Log.d("TAG Key Stor", "firebase: "  + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());


                                    if(child.getKey().equals(firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey())){

                                        product_List.add(new Product(
                                                child.getValue(Product.class).getBarcode(),
                                                child.getValue(Product.class).getName(),
                                                child.getValue(Product.class).getCompany(),
                                                child.getValue(Product.class).getAmount(),
                                                child.getValue(Product.class).getStorage()
                                        ));
                                    Log.d("TAG Key Stor", "key we tak out: "  + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
                                    Log.d("TAG Key Stor", "firebase with recycler List post: "  + firebaseRecyclerAdapter.getRef(recyclerView_list.getChildLayoutPosition(viewHolder.itemView)));


                                        Log.d("TAG ViewHolder Pos Stor","int " + recyclerView_list.getChildLayoutPosition(viewHolder.itemView));
                                        recyclerView_list.removeViewAt(recyclerView_list.getChildLayoutPosition(viewHolder.itemView));

                                        break;
                                   }

                                    //Log.d("data.getRef key", "Key is: " + database.getReference().getKey() );
/*
                                    Log.d("DataSnap ref_key","Barcode : " + child.getValue(Product.class).getBarcode() +
                                            "\n Name: " + child.getValue(Product.class).getName() +
                                            "\n Company: " + child.getValue(Product.class).getCompany() +
                                            "\n Amount: " + child.getValue(Product.class).getAmount() +
                                            "\n Storage: " + child.getValue(Product.class).getStorage() +

                                            "\n fireBase key: " + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey()
                                    );
*/

                                }

                                String id = database.getReference().push().getKey(); // makes a new key on the move
                                //String id = firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey();
                                Product product = new Product(((Product) product_List.get(0)).getBarcode(),
                                        ((Product) product_List.get(0)).getName(), ((Product) product_List.get(0)).getCompany(),
                                        ((Product) product_List.get(0)).getAmount(), ((Product) product_List.get(0)).getStorage());


                                database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("List")
                                    .child("ShoppingList").child(user_shopping_list_in_use).child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.e("query begin", "Product moved/saved to shopping list");
                                                Toast.makeText(getActivity(), "Product moved/saved to shopping list: " + user_shopping_list_in_use, Toast.LENGTH_LONG).show();
                                            }
                                            if(task.isComplete()) {
                                                Log.e("query end", "Product moved/saved to shopping list");
                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Toast.makeText(ShopingListView.this, "Failed to added data!! " +e.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.e("EXCEPTION", "Failed to add the data: " + e.getMessage());
                                        }
                                    });

                                /** removers query key and data from the realtime db **/
                                refquery.getRef().removeValue();

                            }
                            if(direction == 4){
                                /** removers query key and data from the realtime db **/
                                refquery.getRef().removeValue();
                            }
                        }
                    }
                });
            }else{
                //Toast.makeText( getActivity(), "Swipe empty call", Toast.LENGTH_LONG).show();
            }



            //recyclerView_list.getAdapter().notifyDataSetChanged();

        }
    };

}