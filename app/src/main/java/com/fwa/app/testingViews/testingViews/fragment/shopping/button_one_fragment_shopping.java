package com.fwa.app.testingViews.testingViews.fragment.shopping;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.fwa.app.classes.Product;
import com.fwa.app.classes.ProductVH;
import com.fwa.app.familyshoppingplanner.R;
//import com.fwa.app.testingViews.testingViews.fragment.storage.button_one_fragment_storage;
import com.fwa.app.product.manualy.add.main_add_product_shopping_list_with_barcode_reader_db;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
 * Use the {@link button_one_fragment_shopping#newInstance} factory method to
 * create an instance of this fragment.
 */
public class button_one_fragment_shopping extends Fragment {

    private View list_view;
    private RecyclerView recyclerView_list;

    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

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
        mAuth = FirebaseAuth.getInstance();
        list_view = inflater.inflate(R.layout.fragment_button_one_shopping, container, false);

        recyclerView_list = (RecyclerView) list_view.findViewById(R.id.recycle_one_shopping);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));

        ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("ShoppingList").child("MAIN");

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
                ProductVH vh = (ProductVH) holder;
                Product emp = (Product) model;

                vh.txt_name.setText(emp.getName());
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
                                        child("ShoppingList").child("MAIN").child(emp.getKey());

                                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                                        if (dataSnapshot.isSuccessful()) {
                                           // ref.getRef().removeValue();
                                        }
                                    }
                                });

                                //DAOEmployee dao=new DAOEmployee();
                                //dao.remove(emp.getKey()).addOnSuccessListener(suc->
                                //{
                                //+ emp.getKey()
                                Toast.makeText(getContext(), "toDo: Myst add record to storage!!: " , Toast.LENGTH_SHORT).show();
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
        firebaseRecyclerAdapter.startListening(); // this belong to // out in onCreateView
    }

    //ToDo: change ongoing!!
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**  MAIN SHOPPING LIST RECYCLERVIEW  **/
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(direction == 8){
                Toast.makeText(getActivity(), "Added to Storage List" , Toast.LENGTH_LONG).show();
            }
            if(direction == 4){
                Toast.makeText(getActivity(), "Deleted Item from Shopping List", Toast.LENGTH_LONG).show();
            }

            List product_List = new ArrayList();


            //JOBBER HERE!!
            /** HERE WE NEED TO CHECK THE PRODUCT STORAGE - IF DRY - COLD +4 OR COLD -18 **/

            DatabaseReference refquery = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Family").
                    child("List").
                    child("ShoppingList").child("MAIN").child(firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
            if(!firebaseRecyclerAdapter.getSnapshots().isEmpty()){
                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                        if (dataSnapshot.isSuccessful()) {
                            //ToDo: Refactoring needed!!

                            if(direction == 8) {

                                /** Move Product to Shopping list - get first the product snapshot into a product class refactoring later !!*/
                                for (DataSnapshot child : dataSnapshot.getResult().getChildren()) {
                                    Log.d("TAG Key Shop", "child key: "  + child.getKey() );
                                    Log.d("TAG Key Shop", "firebase: "  + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());


                                    if(child.getKey().equals(firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey())) {

                                        product_List.add(new Product(
                                                child.getValue(Product.class).getBarcode(),
                                                child.getValue(Product.class).getName(),
                                                child.getValue(Product.class).getCompany(),
                                                child.getValue(Product.class).getAmount(),
                                                child.getValue(Product.class).getStorage()
                                        ));
                                        Log.d("TAG Key Shop", "key we tak out: "  + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
                                        Log.d("TAG Key Shop", "firebase with recycler List post: "  + firebaseRecyclerAdapter.getRef(recyclerView_list.getChildLayoutPosition(viewHolder.itemView)));

                                        Log.d("TAG ViewHolder Pos Shop","int " + recyclerView_list.getChildLayoutPosition(viewHolder.itemView));
                                        recyclerView_list.removeViewAt(recyclerView_list.getChildLayoutPosition(viewHolder.itemView));

                                        break;
                                    }
                                }


                                String id = database.getReference().push().getKey();
                                Product product = new Product(((Product) product_List.get(0)).getBarcode(),
                                        ((Product) product_List.get(0)).getName(), ((Product) product_List.get(0)).getCompany(),
                                        ((Product) product_List.get(0)).getAmount(), ((Product) product_List.get(0)).getStorage());


                                String storage_container_to_use = "";
                                switch ( ((Product) product_List.get(0)).getStorage().toString() ){
                                    case "c":
                                        storage_container_to_use = "Refrigerator";
                                        break;
                                    case "f":
                                        storage_container_to_use = "Freezer";
                                        break;
                                    case "d":
                                        storage_container_to_use = "DryStorage";
                                        break;
                                }
                                /** save product to storage list = there storage criteria  */
                                database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("List")
                                        .child(storage_container_to_use).child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Toast.makeText(getActivity(), "Product moved to cold -4 list", Toast.LENGTH_LONG).show();

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
                                refquery.getRef().removeValue();
                            }
                            if(direction == 4){
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