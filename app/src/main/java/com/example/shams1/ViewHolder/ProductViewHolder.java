package com.example.shams1.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shams1.Interface.ItemClickListner;
import com.example.shams1.R;

import xyz.hanks.library.bang.SmallBangView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice,txtProductdescount;
    public ImageView imageView;
     public Button addtocart;
    public ItemClickListner listner;

    public SmallBangView likeheart ;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        addtocart=itemView.findViewById(R.id.btnShare);
        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        txtProductdescount=itemView.findViewById(R.id.product_descount);

        likeheart=itemView.findViewById(R.id.like_heart);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}