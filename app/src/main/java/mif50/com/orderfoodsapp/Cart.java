package mif50.com.orderfoodsapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import common.Common;
import database.Database;
import model.Order;
import model.Request;
import viewHolder.CartAdapter;

public class Cart extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView_list_order;
    RecyclerView.LayoutManager layoutManager;

    TextView txt_total;
    Button btn_order_cart;

    List<Order> cart= new ArrayList<>();
    CartAdapter cartAdapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // init firebase

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        // get view
        recyclerView_list_order=findViewById(R.id.recycler_list_order);
        recyclerView_list_order.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView_list_order.setLayoutManager(layoutManager);

        txt_total=findViewById(R.id.txt_total);
        btn_order_cart=findViewById(R.id.btn_order_cart);
        btn_order_cart.setOnClickListener(this);

        loadListFood();
    }

    private void loadListFood() {
        cart=new Database(getBaseContext()).getCarts();
        cartAdapter=new CartAdapter(Cart.this,cart);
        recyclerView_list_order.setAdapter(cartAdapter);
        // calculate total price
        int total=0;
        for (Order order:cart){
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        }
        Locale local = new Locale("en","us");
        NumberFormat nft= NumberFormat.getCurrencyInstance(local);
        txt_total.setText(nft.format(total));
    }

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,Cart.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id==R.id.btn_order_cart){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Cart.this);
        dialog.setTitle("one more step");
        dialog.setMessage("Enter your address ");
        final EditText editAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        editAddress.setLayoutParams(params);
        dialog.setView(editAddress);
        dialog.setIcon(R.drawable.ic_card);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    // create new Request
                Request request = new Request(Common.currentUser.getPhone(),Common.currentUser.getName(),editAddress.getText().toString(),txt_total.getText().toString(),cart);
                // submit to firebase and we will user current time as key
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                // delete cart
                new Database(Cart.this).cleanCart();
                Toast.makeText(Cart.this, "thank you , order place", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
