package com.example.shams1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.example.shams1.Model.Products;
import com.example.shams1.Prevalent.Prevalent;
import com.example.shams1.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference ProductsRef;
    private DatabaseReference ProductsFav;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout categorylayout;
    private EditText search ;
    public  FirebaseRecyclerOptions<Products> options ;
    private CardView bed;
    private CardView kof;
    private CardView mfrsh;
    private CardView fota;
    private CardView aces;
    private CardView rope;

    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocal();

        setContentView(R.layout.activity_home);

        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();
        if (bundle !=null){

            type=getIntent().getExtras().getString("Admin").toString();
        }
        search =findViewById(R.id.search_home);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        if (!type.equals("Admin")){
            ProductsFav = FirebaseDatabase.getInstance().getReference().child("Fav").child(Prevalent.currentOnlineUser.getPhone().toString());

        }

        categorylayout =findViewById(R.id.fragment_top_id);
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef, Products.class)
                .build();

        bed =findViewById(R.id.bed);
        kof =findViewById(R.id.kof);
        mfrsh =findViewById(R.id.mfr);
        fota =findViewById(R.id.fot);
        aces =findViewById(R.id.aces);
        rope =findViewById(R.id.rope);



        Paper.init(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!type.equals("Admin")){

                    Intent intent =new Intent(HomeActivity.this,CartActivity.class);
                    startActivity(intent);
                }

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);


        if (!type.equals("Admin")) {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }

        recyclerView = findViewById(R.id.recycler_menu);
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
                    categorylayout.setVisibility(View.GONE);
                    onStart();
                }else {
                    options = new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(ProductsRef, Products.class)
                            .build();
                    categorylayout.setVisibility(View.VISIBLE);
                    onStart();
                }

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        bed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence option []=new CharSequence[]{

                        "طقم سرير كبير",
                        "طقم سرير اطفال"
                };
                if (type.equals("Admin")){


                    AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("طقم سرير كبير و اطفال ");

                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity.this, CategoryUserViewActivity.class);
                            intent.putExtra("category", "طقم سرير كبير و اطفال");
                            intent.putExtra("Admin", "Admin");
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
                else {
                    AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("طقم سرير كبير و اطفال ");

                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity.this, CategoryUserViewActivity.class);
                            intent.putExtra("category", "طقم سرير كبير و اطفال");
                            intent.putExtra("Admin", "");
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

            }
        });
        kof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,CategoryUserViewActivity.class);
                intent.putExtra("category", "كوفرتة و بطانية");
                if (type.equals("Admin")){
                    intent.putExtra("Admin", "Admin");
                    intent.putExtra("categoryDialog","");

                }
                else {
                    intent.putExtra("Admin", "");
                    intent.putExtra("categoryDialog","");

                }
                startActivity(intent);

            }
        });
        mfrsh.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent =new Intent(HomeActivity.this,CategoryUserViewActivity.class);
            intent.putExtra("category", "مفارش");
            if (type.equals("Admin")){
                intent.putExtra("Admin", "Admin");
                intent.putExtra("categoryDialog","");

            }
            else {
                intent.putExtra("Admin", "");
                intent.putExtra("categoryDialog","");


            }
            startActivity(intent);
        }
    });
        fota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence option []=new CharSequence[]{

                        "100x170",
                        "70x140",
                        "60x120",
                        "50x100",
                        "30x50",
                        "33x33",
                        "90x50"

                };

                if (type.equals("Admin")){
                    AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("فوط و مناشف");

                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity.this, CategoryUserViewActivity.class);
                            intent.putExtra("category", "فوط");
                            intent.putExtra("Admin", "Admin");
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
                else {
                    AlertDialog.Builder builder =new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("فوط و مناشف");

                    builder.setItems(option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(HomeActivity.this, CategoryUserViewActivity.class);
                            intent.putExtra("category", "فوط");
                            intent.putExtra("Admin", "");
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

            }
        });
        aces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,CategoryUserViewActivity.class);
                intent.putExtra("category", "اكسسوار منزل");
                if (type.equals("Admin")){
                    intent.putExtra("Admin", "Admin");
                    intent.putExtra("categoryDialog","");

                }
                else {
                    intent.putExtra("Admin", "");
                    intent.putExtra("categoryDialog","");


                }
                startActivity(intent);
            }
        });
        rope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,CategoryUserViewActivity.class);
                intent.putExtra("category", "روب");
                if (type.equals("Admin")){
                    intent.putExtra("Admin", "Admin");
                    intent.putExtra("categoryDialog","");

                }
                else {
                    intent.putExtra("Admin", "");
                    intent.putExtra("categoryDialog","");


                }
                startActivity(intent);
            }
        });

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, int position, @NonNull final Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText(  model.getPrice() + " ج.م");
                        holder.txtProductdescount.setText( model.getPdescount() + " ج.م");
                        holder.txtProductdescount.setPaintFlags(holder.txtProductdescount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        Picasso.get().load(model.getImage()).into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("Admin")){
                                    Intent intent=new Intent(HomeActivity.this,AdminMaintainProductActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("category",model.getCategory());
                                    intent.putExtra("categoryDialog",model.getCategoryDialog());
                                    startActivity(intent);

                                }else {
                                    Intent intent=new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }

                            }
                        });
                        holder.addtocart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("Admin")){

                                    Intent intent=new Intent(HomeActivity.this,AdminMaintainProductActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);

                                }else {
                                    Intent intent=new Intent(HomeActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }

                            }
                        });

                        holder.likeheart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (!type.equals("Admin")){
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
                                                    Toast.makeText(HomeActivity.this, "Liked : "+model.getPname(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }else {
                                    Toast.makeText(HomeActivity.this, "You Are Admin Not User", Toast.LENGTH_SHORT).show();
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




            @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_arabic)
        {
            setLocale("AR");
            recreate();
            return true;
        }else if (id==R.id.nav_english){
            setLocale("EN");
            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocale(String ar) {

        Locale locale=new Locale(ar);
        locale.setDefault(locale);
        Configuration config =new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",ar);
        editor.apply();
    }


    public void loadLocal(){

        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String lang=prefs.getString("My_Lang","");
        setLocale(lang);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            if (!type.equals("Admin")){

                Intent intent =new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_orders)
        {

        }
        else if (id == R.id.nav_favourites)
        {
            Intent intent =new Intent(HomeActivity.this,FavouriteActivity.class);
            if (!type.equals("Admin")){

                intent.putExtra("Admin",type);
                startActivity(intent);

            }else {
                intent.putExtra("Admin",type);
                Toast.makeText(this, "You Are Admin Not User", Toast.LENGTH_LONG).show();
            }

        }
        else if (id == R.id.nav_settings)
        {
            if (!type.equals("Admin")){

                Intent intent = new Intent(HomeActivity.this, SettinsActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}