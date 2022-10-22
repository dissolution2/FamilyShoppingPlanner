package com.fwa.app.testingViews.testingViews.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.fwa.app.classes.Product;
import com.fwa.app.classes.ProductVH;
import com.fwa.app.familyshoppingplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link search_query_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class search_query_fragment extends Fragment {

    private View list_view;
    private RecyclerView recyclerView_list;

    FirebaseRecyclerAdapter firebaseRecyclerAdapter;

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

    public search_query_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search_query_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static search_query_fragment newInstance(String param1, String param2) {
        search_query_fragment fragment = new search_query_fragment();
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
        list_view = inflater.inflate(R.layout.fragment_search_query_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();

        //progressBar = (ProgressBar) list_view.findViewById(R.id.progressBar2);
        recyclerView_list = (RecyclerView) list_view.findViewById(R.id.search_list_recycle);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(getContext()));

        /** Get search value from main fragment to do the actual searching here */
        getParentFragmentManager().setFragmentResultListener("data_sendt", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String data = result.getString("search_value");
                Toast.makeText(getActivity(), "Search txt is: " + data.toString() , Toast.LENGTH_LONG).show();
            }
        });
        /** Get search value from main fragment to do the actual searching here */


        /** under is just a test remove every thing
         *
         *  1, implement the search in our own db list , and sett default where we want the search item to go to when we swipe""
         * 2. if not found on our own db list. Search the main DB.
         * 3 if not we must make a new intent to make the user add the product him/her self !!??
         *
         **/

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
        return  list_view;
    }
        @Override
        public void onStart() {
            super.onStart();
            //progressBar.setVisibility(View.VISIBLE);
            firebaseRecyclerAdapter.startListening();
        }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //Toast.makeText(getActivity(), "direction : " +direction, Toast.LENGTH_LONG).show();

                if(direction == 8){
                    Toast.makeText(getActivity(), "Added to Shopping List" , Toast.LENGTH_LONG).show();
                }
                if(direction == 4){
                    Toast.makeText(getActivity(), "Deleted Item from Cold -4", Toast.LENGTH_LONG).show();
                }


                List product_List = new ArrayList();

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

                                if(direction == 8) {

                                    /** Move Product to Shopping list - get first the product snapshot into a product class refactoring later !!*/
                                    for (DataSnapshot child : dataSnapshot.getResult().getChildren()) {
                                        product_List.add(new Product(
                                                child.getValue(Product.class).getBarcode(),
                                                child.getValue(Product.class).getName(),
                                                child.getValue(Product.class).getCompany(),
                                                child.getValue(Product.class).getAmount(),
                                                child.getValue(Product.class).getStorage()
                                        ));
                                        break;
                                    }
                                    String id = database.getReference().push().getKey();
                                    Product product = new Product(((Product) product_List.get(0)).getBarcode(),
                                            ((Product) product_List.get(0)).getName(), ((Product) product_List.get(0)).getCompany(),
                                            ((Product) product_List.get(0)).getAmount(), ((Product) product_List.get(0)).getStorage());

                                    /** N = Norway, Here we must add more list's as a variable  */
                                    database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family").child("List")
                                            .child("ShoppingList").child("ANNETTE").child(id).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        //Toast.makeText(getActivity(), "Product moved to shopping list", Toast.LENGTH_LONG).show();
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
                                    //refquery.getRef().removeValue();
                                    recyclerView_list.removeViewAt(viewHolder.getBindingAdapterPosition());
                                    recyclerView_list.getAdapter().notifyDataSetChanged();
                                }
                                if(direction == 4){
                                    //refquery.getRef().removeValue();
                                    recyclerView_list.removeViewAt(viewHolder.getBindingAdapterPosition());
                                    recyclerView_list.getAdapter().notifyDataSetChanged();

                                }
                            }
                        }
                    });
                }else{
                    //Toast.makeText( getActivity(), "Swipe empty call", Toast.LENGTH_LONG).show();
                }


                recyclerView_list.removeViewAt(viewHolder.getBindingAdapterPosition());
                recyclerView_list.getAdapter().notifyDataSetChanged();

            }
        };
    }