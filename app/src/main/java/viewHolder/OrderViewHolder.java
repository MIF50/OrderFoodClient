package viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import inter.ItemClickListener;
import mif50.com.orderfoodsapp.R;

/**
 * Created by mohamed on 12/2/17.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_id,txt_status,txt_phone,txt_address;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        txt_id=itemView.findViewById(R.id.txt_id);
        txt_status=itemView.findViewById(R.id.txt_status);
        txt_phone=itemView.findViewById(R.id.txt_phone);
        txt_address=itemView.findViewById(R.id.txt_address);
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
