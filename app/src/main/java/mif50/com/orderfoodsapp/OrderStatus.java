package mif50.com.orderfoodsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.Common;
import model.Request;
import viewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recycler_order;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database=FirebaseDatabase.getInstance();
        request=database.getReference("Requests");

        recycler_order=findViewById(R.id.recycler_order);
        recycler_order.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_order.setLayoutManager(layoutManager);
        // if we start orderStatus activity from Home Activity we will not put any exra ,
        // so we just load order phone from Common
        if (getIntent()==null){
            loadOrders(Common.currentUser.getPhone());
        }else {
            loadOrders(getIntent().getStringExtra("userPhone"));
        }


    }

    private void loadOrders(String phone) {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,R.layout.order_layout,OrderViewHolder.class,
                request.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txt_id.setText(adapter.getRef(position).getKey());
                viewHolder.txt_status.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txt_phone.setText(model.getPhone());
                viewHolder.txt_address.setText(model.getAddress());

            }
        };
        adapter.notifyDataSetChanged(); // load data when refresh
        recycler_order.setAdapter(adapter); // set up adapter
    }



    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,OrderStatus.class);
        return intent;
    }
}
