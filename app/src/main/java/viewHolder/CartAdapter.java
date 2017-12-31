package viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import inter.ItemClickListener;
import mif50.com.orderfoodsapp.R;
import model.Order;

/**
 * Created by mohamed on 11/27/17.
 */


class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
     TextView txt_card_item_name,txt_card_item_price;
     ImageView card_item_count;
     ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_card_item_name=itemView.findViewById(R.id.txt_card_item_name);
        txt_card_item_price=itemView.findViewById(R.id.txt_card_item_price);
        card_item_count=itemView.findViewById(R.id.card_item_count);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    List<Order> dataList=new ArrayList<>();
    Context context;
    public CartAdapter(Context context,List<Order> dataList){
        this.context=context;
        this.dataList=dataList;
    }
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+dataList.get(position).getQuantity(), Color.RED);
        holder.card_item_count.setImageDrawable(drawable);
        Locale local =new Locale("en","us");
        NumberFormat nft= NumberFormat.getCurrencyInstance(local);
        int price =(Integer.parseInt(dataList.get(position).getPrice()))*(Integer.parseInt(dataList.get(position).getQuantity()));
        holder.txt_card_item_price.setText(nft.format(price));
        holder.txt_card_item_name.setText(dataList.get(position).getProductName());


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
