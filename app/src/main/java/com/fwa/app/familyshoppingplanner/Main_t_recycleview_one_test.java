package com.fwa.app.familyshoppingplanner;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main_t_recycleview_one_test extends AppCompatActivity implements View.OnClickListener{

    private final String ERORR = "DB eror";
    private final String LIST_PRODUCT_TEST = "List of Product:";

    String barcode ="";
    String product= "";
    int amount = 0;

    public ArrayList<Product> product_List = new ArrayList<Product>();

    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<Product> dataProductList = new ArrayList<Product>();
    private Button test_getDataToListBtn;


    public FirebaseDatabase database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view_test_two);

        test_getDataToListBtn = findViewById(R.id.getDataToListBtn);
        test_getDataToListBtn.setOnClickListener(this);

        ListView lv = (ListView) findViewById(R.id.listview);

        //generateListContent();

        //getDataToViewList();







        //if (product_List.size() > 0) {
            //generateListContent(product_List);
        //}else{
            //Toast.makeText(MainViewGuiShopping.this, "List.Count: " + product_List.size(), Toast.LENGTH_LONG).show();
        //}






        generateListContent();
        //getDataToViewList();

        lv.setAdapter(new MyListAdaper(this, R.layout.list_item, data));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Main_t_recycleview_one_test.this, "List item was clicked at " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataToViewList() {

        //ArrayList<Product> product_List = new ArrayList<Product>();
        DatabaseReference ref = database.getReference("kTDClQTUjRcjWZgxIkg7MtgJytA2")
                .child("Family").
                child("List").
                child("Refrigerator");
        //.child("0").child("productName");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot child : dataSnapshot.getChildren() ) {


                        //Log.d(LIST_PRODUCT_TEST, " :" + child.getValue(Product.class).getBarcode().toString() );
                        //Toast.makeText(MainViewGuiShopping.this, "Read Data: " + child.getValue(Product.class).getBarcode() , Toast.LENGTH_LONG).show();


                        String barcode = child.getValue(Product.class).getBarcode();
                        String product = child.getValue(Product.class).getProductName();
                        int amount = child.getValue(Product.class).getProductAmount();

                        product_List.add(new Product(barcode,product,amount));

                        //Toast.makeText(MainViewGuiShopping.this, "List 0: " + product_List.get(0).getBarcode(), Toast.LENGTH_LONG).show();

                    }
                    //Toast.makeText(MainViewGuiShopping.this, "List 0: " + product_List.get(0).getBarcode(), Toast.LENGTH_LONG).show();
                    //generateListContent(product_List);


                }else{
                    Log.d("DataSnapShot", "Don't Exist!!");
                }
                //Post post = dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
                Log.e(ERORR,"data errror " + databaseError.toString());
            }
        });

        //Toast.makeText(MainViewGuiShopping.this, "List 0: " + product_List.get(0).getBarcode(), Toast.LENGTH_LONG).show();
        //Toast.makeText(MainViewGuiShopping.this, "list.Cunt 1: " + product_List.size(), Toast.LENGTH_LONG).show();


        //return product_List;
    }

    private void generateListContent() {

        for(int i = 0; i < 3; i++) {
            data.add(""+ i); //"This is row number " + i);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;
        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.bareCode = (TextView) convertView.findViewById(R.id.list_item_textBareCode);
                viewHolder.productName = (TextView) convertView.findViewById(R.id.list_item_textProductName);
                viewHolder.productAmount = (TextView) convertView.findViewById(R.id.list_item_textProductAmount);
                //viewHolder.button = (Button) convertView.findViewById(R.id.list_item_btn);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            /*
            mainViewholder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
                }
            });
            */
            /*
            String convert = data.get(position);

            String str[] = convert.split(":");

            List<String> list = new ArrayList<>();

            list = Arrays.asList(str); // barcode:productName:productAmount
            */
            //list.get(0) == 1 string:;
            //list.get(1) == 2 string:string
            //list.get(2) == 3 string:string:string

            DatabaseReference ref = database.getReference("kTDClQTUjRcjWZgxIkg7MtgJytA2")
                    .child("Family").
                    child("List").
                    child("Refrigerator");
            //.child("0").child("productName");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        for (DataSnapshot child : dataSnapshot.getChildren() ) {


                            //Log.d(LIST_PRODUCT_TEST, " :" + child.getValue(Product.class).getBarcode().toString() );
                            //Toast.makeText(MainViewGuiShopping.this, "Read Data: " + child.getValue(Product.class).getBarcode() , Toast.LENGTH_LONG).show();


                            barcode = child.getValue(Product.class).getBarcode();
                            product = child.getValue(Product.class).getProductName();
                            amount = child.getValue(Product.class).getProductAmount();

                           // product_List.add(new Product(barcode,product,amount));

                            //Toast.makeText(MainViewGuiShopping.this, "List 0: " + barcode, Toast.LENGTH_LONG).show();

                        }
                        //Toast.makeText(MainViewGuiShopping.this, "List 0: " + product_List.get(0).getBarcode(), Toast.LENGTH_LONG).show();
                        //generateListContent(product_List);


                    }else{
                        Log.d("DataSnapShot", "Don't Exist!!");
                    }
                    //Post post = dataSnapshot.getValue(Post.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //System.out.println("The read failed: " + databaseError.getCode());
                    Log.e(ERORR,"data errror " + databaseError.toString());
                }
            });



            Toast.makeText(Main_t_recycleview_one_test.this, "R: " + barcode, Toast.LENGTH_LONG).show();

            mainViewholder.bareCode.setText( "test :" + barcode );  // product_List.get(0).getBarcode() );
            mainViewholder.productName.setText( "product"); //product_List.get(0).getProductName() );
            mainViewholder.productAmount.setText( "amount"); //product_List.get(0).getProductAmount());

            return convertView;
        }
    }
    public class ViewHolder {

        ImageView thumbnail;
        TextView bareCode;
        TextView productName;
        TextView productAmount;
        //Button button;
    }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.getDataToListBtn:
                    //getDataToViewList();
                    break;
            }
        }


}