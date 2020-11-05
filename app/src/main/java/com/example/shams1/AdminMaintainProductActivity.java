package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductActivity extends AppCompatActivity {
    Button applyChanges,deletProduct;
    EditText name ,price ,description,descount;
    ImageView imageView;
    String category="";

    private String productID="";
    private DatabaseReference productref;
    private DatabaseReference categoryref;
    String categoryDialog="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_product);

        productID =getIntent().getStringExtra("pid");
        category=getIntent().getStringExtra("category");
        categoryDialog = getIntent().getStringExtra("categoryDialog");

        productref = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        if (categoryDialog=="") {
            categoryref = FirebaseDatabase.getInstance().getReference().child(category).child(productID);
        }else {
            categoryref = FirebaseDatabase.getInstance().getReference().child(category).child(categoryDialog).child(productID);

        }
        applyChanges =findViewById(R.id.btnShare_maintain);
        name =findViewById(R.id.product_name_maintain);
        price =findViewById(R.id.product_price_maintain);
        description =findViewById(R.id.product_description_maintain);
        imageView =findViewById(R.id.product_image_maintain);
        descount=findViewById(R.id.product_descount_maintain);
        deletProduct=findViewById(R.id.delet_product);

        displayProductInfo();

        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChange();
            }
        });

        deletProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletProduct();
            }
        });

    }

    private void DeletProduct() {

        productref.removeValue();
        categoryref.removeValue();
        finish();

    }

    private void applyChange() {
        String Pname =name.getText().toString();
        String Pprice =price.getText().toString();
        String Pdescrition =description.getText().toString();
        String Pdescount=descount.getText().toString();
        if (Pname.equals("")){
            Toast.makeText(this, "Write down Product Name", Toast.LENGTH_SHORT).show();
        }else if (Pprice.equals("")){
            Toast.makeText(this, "Write down Product Price", Toast.LENGTH_SHORT).show();
        }else if (Pname.equals("")){
            Toast.makeText(this, "Write down Product Description", Toast.LENGTH_SHORT).show();
        }else if (Pdescount.equals("")){
            Toast.makeText(this, "Write down Product Price befor descount", Toast.LENGTH_SHORT).show();
        }
        else {
            final HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", Pdescrition);
            productMap.put("price", Pprice);
            productMap.put("pname", Pname);
            productMap.put("Pdescount",Pdescount);

            productref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()){
                      Toast.makeText(AdminMaintainProductActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                      categoryref.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()){
                                  Toast.makeText(AdminMaintainProductActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();

                                  Intent intent = new Intent(AdminMaintainProductActivity.this, AdminCategoryActivity.class);
                                  startActivity(intent);
                                  finish();


                              }
                          }
                      });
                  }
                }
            });


        }

    }

    private void displayProductInfo() {

        productref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String Pname =dataSnapshot.child("pname").getValue().toString();
                    String Pprice =dataSnapshot.child("price").getValue().toString();
                    String Pdesciption =dataSnapshot.child("description").getValue().toString();
                    String pdescount =dataSnapshot.child("Pdescount").getValue().toString();
                    String Pimage =dataSnapshot.child("image").getValue().toString();
                    name.setText(Pname);
                    price.setText(Pprice);
                    description.setText(Pdesciption);
                    descount.setText(pdescount);
                    Picasso.get().load(Pimage).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
