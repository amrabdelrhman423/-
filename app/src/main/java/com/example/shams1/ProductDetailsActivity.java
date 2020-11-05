package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shams1.Model.Products;
import com.example.shams1.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {


    private Button addtoCartBtn;
    private ImageView productImage;
    private TextView productName ,productprice, productDescription,Productdescount;
    private ElegantNumberButton numberButton;
    private String productID="",State="Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID =getIntent().getStringExtra("pid");

        addtoCartBtn =findViewById(R.id.pd_add_to_cart_button);
        productImage =findViewById(R.id.product_image_details);
        productName =findViewById(R.id.product_name_details);
        productDescription =findViewById(R.id.product_description_details);
        productprice =findViewById(R.id.product_price_details);
        numberButton =findViewById(R.id.number_btn);
        Productdescount=findViewById(R.id.product_descount_details);

        getProductDetails(productID);

        addtoCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (State.equals("Order Placed")||State.equals("Order Shipped")){
                    Toast.makeText(ProductDetailsActivity.this, "you Can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                }else {
                    addingToCartList();
                }
            }
        });

    }

    private void addingToCartList()
    {
        String saveCurrentTime,saveCurrentData;

        Calendar calforData = Calendar.getInstance();

        SimpleDateFormat currentData =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentData=currentData.format(calforData.getTime());

        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calforData.getTime());

        final DatabaseReference cartListRef=  FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String,Object> cartMap =new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("pname",productName.getText());
        cartMap.put("price",productprice.getText());
        cartMap.put("data",saveCurrentData);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                .child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                     if(task.isSuccessful())
                      {
                          cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                  .child(productID)
                                  .updateChildren(cartMap)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){
                                              Toast.makeText(ProductDetailsActivity.this, "Added To Cart List", Toast.LENGTH_SHORT).show();
                                              Intent intent=new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                              startActivity(intent);
                                          }
                                      }
                                  });
                      }
                    }
                });
    }

    private void getProductDetails(String productID) {

        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Products products =dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productprice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Productdescount.setText(products.getPdescount());
                    Productdescount.setPaintFlags(Productdescount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                    Picasso.get().load(products.getImage()).into(productImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
    }

    private void checkOrderState(){
        DatabaseReference Ordersref ;
        Ordersref =FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        Ordersref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String shippingState=dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("Shipped"))
                    {
                        State ="Order Shipped";

                    }else if (shippingState.equals("not Shipped"))
                    {
                        State ="Order Placed";

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
