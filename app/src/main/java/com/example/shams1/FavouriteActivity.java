package com.example.shams1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.shams1.Model.Products;
import com.example.shams1.Prevalent.Prevalent;
import com.example.shams1.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FavouriteActivity extends AppCompatActivity {

    private EditText search ;
    private DatabaseReference ProductsFav;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public FirebaseRecyclerOptions<Products> options ;

    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        search=findViewById(R.id.search_fav);
        ProductsFav = FirebaseDatabase.getInstance().getReference().child("Fav").child(Prevalent.currentOnlineUser.getPhone().toString());
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsFav, Products.class)
                .build();
        recyclerView = findViewById(R.id.recycler_menu_fav);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        type=getIntent().getExtras().getString("Admin").toString();

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
                            .setQuery(ProductsFav.orderByChild("pname").startAt(s.toString()).endAt(s+"\uf8ff"), Products.class)
                            .build();
                    onStart();
                }else {
                    options = new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsFav, Products.class)
                            .build();
                    onStart();
                }

            }
        });


        Toolbar toolbar =  findViewById(R.id.toolbar_fav);
        toolbar.setTitle("FAVOURITE");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(  model.getPrice() + " ج.م");
                        holder.txtProductdescount.setText( model.getPdescount() + " ج.م");
                        holder.txtProductdescount.setPaintFlags(holder.txtProductdescount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);                        Picasso.get().load(model.getImage()).into(holder.imageView);
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                    Intent intent=new Intent(FavouriteActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("Admin",type);
                                    startActivity(intent);

                            }
                        });
                        holder.addtocart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                    Intent intent=new Intent(FavouriteActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("Admin",type);
                                    startActivity(intent);

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
