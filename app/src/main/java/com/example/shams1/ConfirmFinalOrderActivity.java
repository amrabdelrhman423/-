package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shams1.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEdittxt,phonrEdittxt,addressEdititxt,cityEdittxt,codeEdittxt;
    private Button confirmOrderBtn,codeBtn;

    private String totalAmount ="";
    TextView totalprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");

        totalprice=findViewById(R.id.total_amount);
        confirmOrderBtn =findViewById(R.id.confirm_final_order);
        nameEdittxt=findViewById(R.id.shippment_name);
        phonrEdittxt=findViewById(R.id.shippment_phone);
        addressEdititxt=findViewById(R.id.shippment_address);
        cityEdittxt=findViewById(R.id.shippment_city);
        codeEdittxt=findViewById(R.id.shippment_code);
        codeBtn=findViewById(R.id.confirm_btn_code);

        totalprice.setText("Total Price ="+totalAmount+" ج.م ");


        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(codeEdittxt.getText().toString())){
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Enter code", Toast.LENGTH_SHORT).show();
                }else {
                    String code = codeEdittxt.getText().toString();
                    codeEdittxt.setText("");
                    applyCode(code);
                }
            }
        });
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void applyCode(final String code) {
        DatabaseReference codesref =FirebaseDatabase.getInstance().getReference().child("Codes");

            codesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(code).exists()){
                        codeBtn.setVisibility(View.GONE);
                        codeEdittxt.setVisibility(View.GONE);
                        String codeenter=dataSnapshot.child(code).child("code").getValue().toString();
                        double descount =Double.valueOf(dataSnapshot.child(code).child("descount").getValue().toString());
                        double descountvalue =(descount/100)*Double.valueOf(totalAmount);
                        String total =String.valueOf(Double.valueOf(totalAmount)-descountvalue);
                        totalAmount=total;
                        totalprice.setText("Total Price After entering the code ="+totalAmount+" ج.م ");

                    }
                    else {
                        Toast.makeText(ConfirmFinalOrderActivity.this, "You enter Worng Code", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




    }


    private void check() {
        if (TextUtils.isEmpty(nameEdittxt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your full name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(phonrEdittxt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your phone number", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(addressEdititxt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your Address", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(cityEdittxt.getText().toString()))
        {
            Toast.makeText(this, "Please provide your City name", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        String saveCurrentData,saveCurrentTime;
        Calendar calforData = Calendar.getInstance();

        SimpleDateFormat currentData =new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentData=currentData.format(calforData.getTime());

        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentData.format(calforData.getTime());

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String,Object>ordersMap=new HashMap<>();

        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEdittxt.getText().toString());
        ordersMap.put("phone",phonrEdittxt.getText().toString());
        ordersMap.put("address",addressEdititxt.getText().toString());
        ordersMap.put("City",cityEdittxt.getText().toString());
        ordersMap.put("data",saveCurrentData);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("state","not Shipped");

        OrdersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              if (task.isSuccessful()){

                  FirebaseDatabase.getInstance().getReference()
                          .child("Cart List")
                          .child("User View")
                          .child(Prevalent.currentOnlineUser.getPhone())
                          .removeValue()
                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 Toast.makeText(ConfirmFinalOrderActivity.this, "Your final Order has been placed successfully.", Toast.LENGTH_SHORT).show();
                                 Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
