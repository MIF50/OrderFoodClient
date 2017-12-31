package viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import inter.ItemClickListener;
import mif50.com.orderfoodsapp.R;

/**
 * Created by mohamed on 11/22/17.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView image_food_item;
    public TextView name_food_item;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        image_food_item=itemView.findViewById(R.id.image_food_item);
        name_food_item=itemView.findViewById(R.id.name_food_item);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
