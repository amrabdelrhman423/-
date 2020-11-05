package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shams1.Model.Products;
import com.example.shams1.Prevalent.Prevalent;
import com.example.shams1.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CategoryUserViewActivity extends AppCompatActivity {
    private EditText search ;

    private DatabaseReference ProductsRef;
    private DatabaseReference ProductsFav;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public FirebaseRecyclerOptions<Products> options ;

    String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_user_view);

        search=findViewById(R.id.search_category);
        String category =getIntent().getStringExtra("category").toString();
       String categoryDialog= getIntent().getExtras().get("categoryDialog").toString();



        if (categoryDialog==""){
            ProductsRef = FirebaseDatabase.getInstance().getReference().child(category);


        }else {
            ProductsRef = FirebaseDatabase.getInstance().getReference().child(category).child(categoryDialog);

        }
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class)
                .build();
        type=getIntent().getExtras().getString("Admin").toString();
        if (!type.equals("Admin")){

            ProductsFav = FirebaseDatabase.getInstance().getReference().child("Fav").child(Prevalent.currentOnlineUser.getPhone().toString());
        }

        recyclerView = findViewById(R.id.recycler_menu_category);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    options = new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef.orderByChild("pname").startAt(s.toString()).endAt(s+"\uf8ff"), Products.class)
                            .build();
                    onStart();
                }else {
                    options = new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef, Products.class)
                            .build();
                    onStart();
                }

            }
        });


        Toolbar toolbar =  findViewById(R.id.toolbar_category);
        toolbar.setTitle(category);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(  model.getPrice() + " ج.م");
                        holder.txtProductdescount.setText( model.getPdescount() + " ج.م");
                        holder.txtProductdescount.setPaintFlags(holder.txtProductdescount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("Admin")){
                                    Intent intent=new Intent(CategoryUserViewActivity.this,AdminMaintainProductActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("category",model.getCategory());
                                    intent.putExtra("categoryDialog",model.getCategoryDialog());
                                    startActivity(intent);
                                }else {
                                    Intent intent=new Intent(CategoryUserViewActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                        holder.addtocart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals("Admin")){
                                    Intent intent=new Intent(CategoryUserViewActivity.this,AdminMaintainProductActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }else {
                                    Intent intent=new Intent(CategoryUserViewActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });

                        holder.likeheart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (holder.likeheart.isSelected()){
                                    holder.likeheart.setSelected(false);

                                }else {
                                    holder.likeheart.setSelected(true);
                                    holder.likeheart.likeAnimation(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationCancel(Animator animation) {
                                            super.onAnimationCancel(animation);

                                        }
                                    });
                                    final HashMap<String, Object> productMap = new HashMap<>();
                                    productMap.put("pid", model.getPid());
                                    productMap.put("date", model.getDate());
                                    productMap.put("time", model.getTime());
                                    productMap.put("description", model.getDescription());
                                    productMap.put("image", model.getImage());
                                    productMap.put("category", model.getCategory());
                                    productMap.put("price", model.getPrice());
                                    productMap.put("pname", model.getPname());
                                    productMap.put("Pdescount",model.getPdescount());

                                    ProductsFav.child(model.getPid()).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(CategoryUserViewActivity.this, "Liked : "+model.getPname(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
