package com.example.vietnamesecuisinehelper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    Context ct;
    public ArrayList<ItemObject> mListItems;

    public ItemAdapter(Context context, ArrayList<ItemObject> list) {
        this.ct = context;
        this.mListItems = list;
        //    notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.food_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvTitle.setText(mListItems.get(position).getTitle());
        holder.tvContent.setText(mListItems.get(position).getContent());
        //if(position%2==1)
        //  holder.tvContent.setBackgroundColor(Color.parseColor("#0000ff"));
        holder.imgUser.setImageResource(mListItems.get(position).getResourceID());
        holder.img_content.setImageResource(mListItems.get(position).getResourceID());
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
        holder.btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct.startActivity(new Intent(ct, MapActivity.class)
                        .putExtra("food_name", mListItems.get(position).getTitle()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListItems!=null){
            return mListItems.size();
        }
        return 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        FoldingCell foldingCell;
        TextView tvTitle;
        TextView tvContent;
        ImageView imgUser;
        ImageView img_content;
        Button btnMap;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            imgUser = itemView.findViewById(R.id.img_user);
            img_content = itemView.findViewById(R.id.img_content);
            btnMap = itemView.findViewById(R.id.btnMap);
        }
    }
}
