package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shams1.Model.Cart;
import com.example.shams1.Model.Codes;
import com.example.shams1.Prevalent.Prevalent;
import com.example.shams1.ViewHolder.CartViewHolder;
import com.example.shams1.ViewHolder.CodeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminCodesActivity extends AppCompatActivity {

    EditText codeAdmin,codeDescount;
    Button codeBtn;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference Codesref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_codes);
        codeAdmin=findViewById(R.id.code_admin);
        codeDescount=findViewById(R.id.code_descount);
        codeBtn =findViewById(R.id.code_btn);

        Codesref = FirebaseDatabase.getInstance().getReference().child("Codes");

        recyclerView=findViewById(R.id.recycler_code);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String code =codeAdmin.getText().toString();
                final String descount =codeDescount.getText().toString();
                codeAdmin.setText("");
                codeDescount.setText("");

                if (TextUtils.isEmpty(code)){
                    Toast.makeText(AdminCodesActivity.this, "Enter your code", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(descount)){
                    Toast.makeText(AdminCodesActivity.this, "Enter your code Descount", Toast.LENGTH_SHORT).show();
                }else {
                    HashMap<String, Object> codeMap = new HashMap<>();
                    codeMap.put("code", code);
                    codeMap.put("descount", descount);
                    Codesref.child(code).updateChildren(codeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(AdminCodesActivity.this, "Yor code "+code+"is added with descount"+descount, Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(AdminCodesActivity.this, "error with your code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference Codesreff = FirebaseDatabase.getInstance().getReference().child("Codes");

        FirebaseRecyclerOptions<Codes> options=new FirebaseRecyclerOptions.Builder<Codes>()
                .setQuery(Codesreff,Codes.class)
                .build();



        FirebaseRecyclerAdapter<Codes, CodeViewHolder> adapter
                =new FirebaseRecyclerAdapter<Codes, CodeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CodeViewHolder holder, int i, @NonNull final Codes codes) {

                holder.txtCode.setText(codes.getCode());
                holder.txtDescount.setText(codes.getDescount());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option []=new CharSequence[]{

                                "Remove",
                                "cancel"
                        };
                        AlertDialog.Builder builder =new AlertDialog.Builder(AdminCodesActivity.this);
                        builder.setTitle("Code remove ? ");

                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0){
                                    Codesreff.child(codes.getCode()).removeValue();
                                }
                                if (which==1){

                                }

                            }
                        });

                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.code_item,parent,false);
                CodeViewHolder holder =new CodeViewHolder(view);

                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
