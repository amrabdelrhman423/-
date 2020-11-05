package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shams1.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView bigbed, fota, mafrsh, batanya;
    private ImageView  rop, accs;

    private Button LogoutBtn, CheckOrdersBtn, MaintaineBtn,CodesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        LogoutBtn = findViewById(R.id.admin_logout_btn);
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        MaintaineBtn = (Button) findViewById(R.id.maintain_btn);
        CodesBtn = (Button) findViewById(R.id.code_btn_admin);

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });

        MaintaineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);
            }
        });

        CodesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminCategoryActivity.this,AdminCodesActivity.class);
                startActivity(intent);
            }
        });


        bigbed = (ImageView) findViewById(R.id.bigbed);
        fota = (ImageView) findViewById(R.id.fota);
        mafrsh = (ImageView) findViewById(R.id.mafrsh);
        batanya = (ImageView) findViewById(R.id.batanya);

        rop = (ImageView) findViewById(R.id.rop);
        accs = (ImageView) findViewById(R.id.accs);



        bigbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                CharSequence option []=new CharSequence[]{

                        "طقم سرير كبير",
                        "طقم سرير اطفال"
                };

                AlertDialog.Builder builder =new AlertDialog.Builder(AdminCategoryActivity.this);
                builder.setTitle("طقم سرير كبير و اطفال ");

                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                        intent.putExtra("category", "طقم سرير كبير و اطفال");
                        if(which==0){

                            intent.putExtra("categoryDialog","طقم سرير كبير");

                        }
                        if (which==1){

                            intent.putExtra("categoryDialog","طقم سرير اطفال");

                        }
                        startActivity(intent);
                        finish();

                    }
                });

                builder.show();


            }
        });


        fota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                CharSequence option []=new CharSequence[]{

                        "100x170",
                        "70x140",
                        "60x120",
                        "50x100",
                        "30x50",
                        "33x33",
                        "90x50"

                };

                AlertDialog.Builder builder =new AlertDialog.Builder(AdminCategoryActivity.this);
                builder.setTitle("فوط و مناشف");

                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                        intent.putExtra("category", "فوط");
                        if(which==0){

                            intent.putExtra("categoryDialog","100x170");

                        }
                        if (which==1){

                            intent.putExtra("categoryDialog","70x140");

                        }if (which==2){

                            intent.putExtra("categoryDialog","60x120");

                        }if (which==3){

                            intent.putExtra("categoryDialog","50x100");

                        }if (which==4){

                            intent.putExtra("categoryDialog","30x50");

                        }if (which==5){

                            intent.putExtra("categoryDialog","33x33");

                        }
                        if (which==6){

                            intent.putExtra("categoryDialog","90x50");

                        }
                        startActivity(intent);
                        finish();

                    }
                });

                builder.show();

            }
        });


        mafrsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "مفارش");
                intent.putExtra("categoryDialog","");
                startActivity(intent);
                finish();
            }
        });


        batanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "كوفرتة و بطانية");
                intent.putExtra("categoryDialog","");
                startActivity(intent);
                finish();
            }
        });





        rop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "روب");
                intent.putExtra("categoryDialog","");
                startActivity(intent);
                finish();
            }
        });



        accs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "اكسسوار منزل");
                intent.putExtra("categoryDialog","");
                startActivity(intent);
                finish();
            }
        });


    }

}
