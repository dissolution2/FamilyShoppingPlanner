package com.fwa.app.database;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fwa.app.classes.FamilyMember;
import com.fwa.app.classes.Uid;
import com.fwa.app.classes.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FirebaseRWQ {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database; // = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");

    //private DatabaseReference databaseReference;
    /** Note: big different on addValueEventListener (onDataChange) vrs onCompleteListener (onComplete) */
    private DatabaseReference ref_refrigerator;
    private DatabaseReference ref_freezer;
    private DatabaseReference ref_dry_storage;
    private DatabaseReference ref_shopping_list_main;
    private DatabaseReference ref_shopping_list_weekend;
    private DatabaseReference ref_shopping_list_div;
    private DatabaseReference ref_user_option; // user_group & user_option

    private List<Uid> uid_string_list = new ArrayList<>();

    public FirebaseRWQ(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://authapp-e8559-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public DatabaseReference getRef_user_option(){
        return ref_user_option = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").child("Option");
    }

    public DatabaseReference getRef_refrigerator(){
        return database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Refrigerator");
    }

    public DatabaseReference getRef_user(){
        return database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("UsersGroup");
    }

    public void remove(String remove_key){}
    public void query(String query_string){}










    /** All under have to be check if we should use them ***/






    public String query_user_uid(){


        Query user = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .child("FamilyUid");//.orderByKey().equalTo("role");//.child("role"); // null // .child("hashFamily") // ok

        user.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().exists()){
                    Log.d("TAG QUERY","We got info");
                    boolean found_member = false;
                    boolean found_owner = false;
                    Log.d("TAG Q", "result: " + task.getResult().getChildrenCount());
                    for(DataSnapshot child : task.getResult().getChildren()){
                        Log.d("RES","Key: " + child.getKey() + " Value: " + child.getValue());


                        if(child.getValue().equals("member")){
                            // send data to next fragment - > member user
                            //family_info_txt.setText("Member Found");
                            Log.d("TAG Member","we got a member " );
                            found_member = true;


                            //join_a_family_btn.setEnabled(false);
                            //join_a_family_btn.getBackground().setColorFilter(Color.alpha(Color.GRAY), PorterDuff.Mode.DARKEN);
                            break;
                        }
                        if(child.getValue().equals("owner")){
                            // send data to next fragment - > owner user
                            Log.d("TAG Owner","we got a Owner");
                            found_owner = true;
                            //family_info_txt.setText("Owner");
                            break;
                        }
                    }
                    if(found_member){
                        Log.d("TAG Q Member Uid","looking for uid");

                        Query member = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                                .child("FamilyUid").child("hashFamily").child(mAuth.getCurrentUser().getUid());//.orderByKey().equalTo("role");//.child("role"); // null // .child("hashFamily") // ok

                        member.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.getResult().exists()){
                                    Log.d("TAG Q","hashFamily uid: " + task.getResult().getKey() + " Value: " + task.getResult().getValue() );
                                    //family_info_txt.setText("Member " + "Uid:" +  task.getResult().getValue());
                                    //returnValue_uid[0] = task.getResult().getValue().toString();
                                    //editor.putString(getString(R.string.uid_user), "oGm09XrH8tekko4bB0No7Qyp2oM2" );
                                    //editor.apply();
                                    //editor.commit();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY","Failed " + e);
                            }
                        });

                    }
                    if(found_owner){


                        Query owner = database.getReference("Users").child(mAuth.getCurrentUser().getUid())
                                .child("FamilyUid").child("uid");//.orderByKey().equalTo("uid");

                        owner.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.getResult().exists()){
                                    Log.d("TAG Q","hashFamily uid: " + task.getResult().getKey() + " Value: " + task.getResult().getValue() );

                                    //family_info_txt.setText("Owner " + "Uid:" +  task.getResult().getValue());
                                    //returnValue_uid[0] = task.getResult().getValue().toString();
                                    //editor.putString(getString(R.string.uid_user), "oGm09XrH8tekko4bB0No7Qyp2oM2" );
                                    //editor.apply();
                                    //editor.commit();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG QUERY","Failed " + e);
                            }
                        });


                    }

                    /** key = role, if value = member, search string is on hashFamily value  **/
                    /** key = role, if value = owner, search string is on uid value  **/

                    /** key = Not found = No owner or No member . direct to sett up screen  **/


                    // Directly to main app screen //
                    //startActivity(new Intent(main_app_pre_check_user.this, FragmentMainClass.class));
                }else {
                    Log.d("TAG Q","No result!!");

                    //family_info_txt.setText("Not Connected to a FamilyPlanner... Start a Family !");
                    //returnValue_uid[0] = "Null";
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG QUERY","Failed " + e);
            }
        });
//Log.d("TAG Q ReturnV","Value is: " +returnValue_uid[0]);
        return "";//returnValue_uid[0];
    }



    public String query_user_shopping_list_default_to_use(){

        DatabaseReference ref = getRef_refrigerator();

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {

                }
            }
        });
        return "";
    }

    public void query_user_option_remove_value(){

        DatabaseReference ref = getRef_user_option();
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {
                    ref.getRef().removeValue();
                }
            }
        });
    }

    public Task<Void> updateUserOption(String key, HashMap<String ,Object> hashMap)
    {
        return getRef_user_option().child(key).updateChildren(hashMap);
    }


    /** Works Use: button_menu_fragment_option_product.java */
    public void updateUserOptionOnComplete(String value){

        DatabaseReference ref = getRef_user_option();
        /*
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Option");

         */
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("defaultList", value);
                    ref.updateChildren(updates);
                }
            }
        });
    }


    // fails - loops
    public void updateUserOptionOnEventL(String value){

        DatabaseReference ref = getRef_user_option();
        /*
        DatabaseReference ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").
                child("Option");
         */
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Map<String, Object> updates = new HashMap<String,Object>();

                    updates.put("DefaultShoppingList", value);
                    //updates.put("homeScore", newscore);
                    //etc

                    ref.updateChildren(updates);
/*
                            List product_List = new ArrayList();

                            for (DataSnapshot child : dataSnapshot.getChildren() ) {

                                //String barcode = child.toString();
                                String barcode = child.getValue().toString();


                                product_List.add(new Option(barcode));
                            }

                            // test for fun!!
                            Log.d("TAG get data","BareCode: " + ((Option)product_List.get(0)).getDefaultShoppingList());
 */

                }else{
                    Log.d("TAG get data","Don't = dataSnapshot exists !!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERORR","data error " + databaseError.toString());
            }
        });



    }

//ToDo Begin work - 30 Okt adding user and refactoring it's use


    public void set_up_user_group(){




    }


    public void add_user_to_family_account(){ //String email, String id_string

        Log.d("TAG TEST USER Beginning","Current User: " + mAuth.getCurrentUser().getEmail());


        Query ref = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Family").
                child("List").child("Users").child("Group");//.child(mAuth.getCurrentUser().getUid());

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> dataSnapshot) {
                if (dataSnapshot.isSuccessful()) {

                    Log.d("TAG TEST USER Result", "get user data: " + dataSnapshot.getResult().getChildrenCount());

                    for (DataSnapshot child : dataSnapshot.getResult().getChildren() ){
                        Log.d("TAG TEST USER Result", "get user data: " + child.toString());

                    }



                    //ref.getRef().removeValue();
                }
                if(dataSnapshot.isComplete()){
                    Log.d("TAG TEST USER Ended", "If no data !!!");
                }

            }
        });



    }


    public void write_user_id_token(){
        database.getReference().child("Family").child("List")
                .child("Users").child(mAuth.getCurrentUser().getUid()).child("Id_token").setValue(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getApplicationContext(),"Store",Toast.LENGTH_LONG).show();
                        if(task.isComplete()){
                            //Log.d("TAG TEST USER Result", "get user data: " + child.toString());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void sett_value_id_string_token_on_uid_user(String value){
        database.getReference().child("Family").child("List")
                .child("Users").child(mAuth.getCurrentUser().getUid()).child("Id").setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG Id String", "Sett value 32 random string to Group Id = value ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void sett_value_id_string_token(){

        String randomValue = getRandomString(32);


        database.getReference().child("Family").child("List")
                .child("Users").child("Group").child(database.getReference().push().getKey()).child("Id").setValue(randomValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG Id String", "Sett value 32 random string to Group Id = value ");

                        if(task.isComplete()){

                            sett_value_id_string_token_on_uid_user(randomValue); // set string all so to user, so a query to get string

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void query_value_id_string_token(){// String email){

        // So we don't share the id_token.
        // this mAuth.getCurrentUser().getUid()
        // family mAuth = email + id_string


        Query ref = database.getReference()
                .child("Family").
                child("List").child("Users").child("Group");//.child(mAuth.getCurrentUser().getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.d("TAG QUERY","Exists Data is: " + dataSnapshot.toString() );
                    Log.d("TAG QUERY","On user: " + mAuth.getCurrentUser().getEmail() );
                }else {
                    Log.d("TAG QUERY","Exists Don't // we sett a value to test ");
                    sett_value_id_string_token();

                }


            }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("ERORR","data error " + databaseError.toString());
                }
            });
    }

    public void query_value_id_string_token_write_to_user_uid(){// String email){

        Query ref = database.getReference(mAuth.getCurrentUser().getUid())
                .child("Family").
                child("List").child("Users").child("Group").child("Id");//.getKey();//.child(mAuth.getCurrentUser().getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.d("TAG QUERY","Exists Data is: " + dataSnapshot.toString() );
                    Log.d("TAG QUERY","On user: " + mAuth.getCurrentUser().getEmail() );



                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERORR","data error " + databaseError.toString());
            }
        });
    }




    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public void write_first_start_up_of_family(){
        // .child(mAuth.getCurrentUser().getUid())
        /** new structure testing so we can add more user's to data base account **/
        database.getReference().child("Family").child("List")
                .child("Users").child(mAuth.getCurrentUser().getUid()).child("Mail").setValue(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            //Log.e("TAG User_Option","data write");
                            /** Write the sharing key to family members to join **/
                            //write_user_sharing_identity_string(mAuth.getCurrentUser().getEmail());
                            //write_user_sharing_identity_string("");

                        }
                        if(task.isComplete()){


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("EXCEPTION","Failed to add the data: " + e.getMessage());
                    }
                });
    }

//ToDo END work - 30 Okt adding user and refactoring it's use


/*
//** Set Default Shopping List /
                            database.getReference().child("Family").child("List")
                                    .child("Users").child(mAuth.getCurrentUser().getUid()).child("DefaultList").setValue("Main").addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()) {
            }
        }
    })
            .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.e("EXCEPTION","Failed to add the data: " + e.getMessage());
        }
    });
 */







    //ToDo: need more testing - bug control - ok, but more testing.
    /** Use: FragmentMainClass.java */
    public void write_user_shopping_list_default_to_use(String default_list_to_use){

        Log.d("TAG First Write","DB User first write: " + mAuth.getCurrentUser().getEmail());


        // .child(mAuth.getCurrentUser().getUid())
        /** new structure testing so we can add more user's to data base account **/
        database.getReference().child("Family").child("List")
                .child("Users").child(mAuth.getCurrentUser().getUid()).child("Option").child("DefaultList").setValue(default_list_to_use).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                          }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("EXCEPTION","Failed to add the data: " + e.getMessage());
                    }
                });


/*

// OLD refactoring to use User's to one family account etc.

        //String id = database.getReference().push().getKey();
        database.getReference().child(mAuth.getCurrentUser().getUid()).child("Family")
                .child("List").child("Option").child("defaultList").setValue(default_list_to_use).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            //Log.e("TAG User_Option","data write");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("EXCEPTION","Failed to add the data: " + e.getMessage());
                    }
                });
*/
        //return true;
    }

}
