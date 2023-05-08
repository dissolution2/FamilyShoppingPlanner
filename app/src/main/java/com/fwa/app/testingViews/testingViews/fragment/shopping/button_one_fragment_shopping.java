package com.fwa.app.testingViews.testingViews.fragment.shopping;

import android.content.SharedPreferences;
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
    private String family_uid="";
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
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        //SharedPreferences.Editor editor = pref.edit();

        //Log.d("TAG","test : " +pref.getString("key_name", "No data!!")) ; // getting String
        family_uid = pref.getString("key_name", "No data!!");
        Log.d("TAG ShoppingList","check family_uid: " + family_uid);
        recyclerView_list = (RecyclerView) list_view.findViewById(R.id.recycle_one_shopping);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));

        // old
/*
       ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("ShoppingList").child("MAIN");
*/
        ref = database.getReference("Groups")
                .child(family_uid)
                .child("Data").
                child("List").
                child("ShoppingList").child("Main");

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

                vh.txt_name.setText(emp.getName().toUpperCase());
                vh.txt_company.setText(emp.getCompany().toUpperCase());
                //vh.txt_position.setText(emp.getPosition());
                vh.txt_amount.setText(String.valueOf(emp.getAmount()));

                // default will be the last entry !!!
                // getting the last storage key eks c d = we get d;
                // getting the last storage key eks d c = we get c;
                String storage ="";
                String storage_made_string ="";
                String storage_one ="";
                String storage_two ="";
                if(!emp.getStorage().isEmpty()){

                    switch (emp.getStorage().size()){
                        case 1: // == 1;
                            storage_one = emp.getStorage().get(0).toLowerCase();

                            switch (storage_one){
                                case "c":
                                    //vh.txt_storage.setText("STORAGE COLD +4");
                                    storage_made_string = "STORAGE COLD +4";
                                    break;
                                case "f":
                                    //vh.txt_storage.setText("STORAGE FREEZER -18");
                                    storage_made_string = "STORAGE FREEZER -18";
                                    break;
                                case "d":
                                    //vh.txt_storage.setText("STORAGE DRY");
                                    storage_made_string = "STORAGE DRY";
                                    break;
                            }
                            break;
                        case 2: // = 2;
                            storage_one = emp.getStorage().get(0).toLowerCase();
                            storage_two = emp.getStorage().get(1).toLowerCase();
                            switch (storage_one){
                                case "c":
                                    storage_made_string = "Cold +4";
                                    break;
                                case "f":
                                    storage_made_string = "FREEZER -18";
                                    break;
                                case "d":
                                    storage_made_string = "DRY";
                                    break;
                            }
                            switch (storage_two){
                                case "c":
                                    storage_made_string = storage_made_string + " | " + "Cold +4";
                                    //vh.txt_storage.setText(storage_made_string);
                                    break;
                                case "f":
                                    storage_made_string = storage_made_string + " | " + "FREEZER -18";
                                    //vh.txt_storage.setText(storage_made_string);
                                    break;
                                case "d":
                                    storage_made_string = storage_made_string + " | " + "DRY";
                                    //vh.txt_storage.setText(storage_made_string);
                                    break;
                            }
                            break;
                    }
                    Log.d("STORAGE","to string: " + emp.getStorage());
                    Log.d("STORAGE","Made String is : " + storage_made_string);
                    vh.txt_storage.setText(storage_made_string);

                }
                //ToDo: change to a imageButton and se if we can get diff image's star empty / full if a user have it as a fav.
                vh.txt_option.setOnClickListener(v->
                {
                    PopupMenu popupMenu =new PopupMenu( getContext(),vh.txt_option);
                    popupMenu.inflate(R.menu.option_menu);
                    popupMenu.setOnMenuItemClickListener(item->
                    {
                        switch (item.getItemId())
                        {
                            case R.id.add_fav:



                                break;
                            case R.id.menu_remove:

                                DatabaseReference ref = database.getReference("Groups")
                                        .child(family_uid)
                                        .child("Data").
                                        child("List").
                                        child("ShoppingList").child("Main").child(emp.getKey());

                                ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                                        if (dataSnapshot.isSuccessful()) {
                                           // ref.getRef().removeValue();
                                        }else{
                                            Log.d("TAG menu_remove","not IsSuccessful!!");
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



            /** HERE WE NEED TO CHECK THE PRODUCT STORAGE - IF DRY - COLD +4 OR COLD -18 **/

            DatabaseReference refquery = database.getReference("Groups")
                    .child(family_uid)
                    .child("Data").
                    child("List").
                    child("ShoppingList").child("Main").child(firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
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
                                                child.getValue(Product.class).getQuantity(),
                                                child.getValue(Product.class).getStorage()
                                        ));
                                        Log.d("TAG Key Shop", "key we tak out: "  + firebaseRecyclerAdapter.getRef(viewHolder.getBindingAdapterPosition()).getRef().getKey());
                                        Log.d("TAG Key Shop", "firebase with recycler List post: "  + firebaseRecyclerAdapter.getRef(recyclerView_list.getChildLayoutPosition(viewHolder.itemView)));

                                        Log.d("TAG ViewHolder Pos Shop","int " + recyclerView_list.getChildLayoutPosition(viewHolder.itemView));
                                        //old
                                        //recyclerView_list.removeViewAt(recyclerView_list.getChildLayoutPosition(viewHolder.itemView));
                                        recyclerView_list.getLayoutManager().removeViewAt(viewHolder.itemView.getId());

                                        break;
                                    }
                                }


                                String id = database.getReference().push().getKey();
                                Product product = new Product(((Product) product_List.get(0)).getBarcode(),
                                        ((Product) product_List.get(0)).getName(), ((Product) product_List.get(0)).getCompany(),
                                        ((Product) product_List.get(0)).getAmount(),((Product) product_List.get(0)).getQuantity(), ((Product) product_List.get(0)).getStorage());

                                // default will be the last entry !!!
                                // getting the last storage key eks c d = we get d;
                                // getting the last storage key eks d c = we get c;
                                String storage ="";
                                String storage_container_to_use = "";
                                String storage_made_string ="";
                                String storage_one ="";
                                String storage_two ="";
                                if(!((Product) product_List.get(0)).getStorage().isEmpty()){

                                    if(((Product) product_List.get(0)).getStorage().size() > 0) {
                                        switch (((Product) product_List.get(0)).getStorage().size()) {
                                            case 1: // == 1;
                                                storage_one = ((Product) product_List.get(0)).getStorage().get(0).toLowerCase();

                                                switch (storage_one) {
                                                    case "c":
                                                        storage_made_string = "Refrigerator";
                                                        break;
                                                    case "f":
                                                        storage_made_string = "Freezer";
                                                        break;
                                                    case "d":
                                                        storage_made_string = "Dry";
                                                        break;
                                                }
                                                break;
                                            case 2: // = 2;
                                                storage_one = ((Product) product_List.get(0)).getStorage().get(0).toLowerCase();
                                                storage_two = ((Product) product_List.get(0)).getStorage().get(1).toLowerCase();
                                                switch (storage_one) {
                                                    case "c":
                                                        storage_made_string = "Refrigerator";
                                                        break;
                                                    case "f":
                                                        storage_made_string = "Freezer";
                                                        break;
                                                    case "d":
                                                        storage_made_string = "Dry";
                                                        break;
                                                }
                                                //ToDo: Need to get default storage value
                                                switch (storage_two) {
                                                    case "c":
                                                        storage_made_string = "Refrigerator";
                                                        break;
                                                    case "f":
                                                        storage_made_string = "Freezer";
                                                        break;
                                                    case "d":
                                                        storage_made_string = "Dry";
                                                        break;
                                                }
                                                break;
                                        }
                                    }
                                    //Log.d("STORAGE","to string: " + emp.getStorage());
                                    Log.d("STORAGE","Made String is : " + storage_made_string);
                                }
                                //ToDo: storage_container_to_use
                                /** save product to storage list = there storage criteria  */
                                database.getReference("Groups").child(family_uid).child("Data").child("List")
                                        .child(storage_made_string).child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.e("TAG isSuccessful", "isSuccessful" );

                                                }else{
                                                    Log.e("TAG Fail", "Failed to get data" );
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