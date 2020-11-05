package com.example.shams1.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shams1.Interface.ItemClickListner;
import com.example.shams1.R;

public class CodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCode,txtDescount;
    public ItemClickListner itemClickListner;

    public CodeViewHolder(@NonNull View itemView) {
        super(itemView);
        txtCode=itemView.findViewById(R.id.code_item);
        txtDescount=itemView.findViewById(R.id.code_descount_item);

    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner){
        this.itemClickListner=itemClickListner;
    }
}
