package mif50.com.orderfoodsapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import database.Database;
import model.Food;
import model.Order;

public class FoodDetails extends AppCompatActivity implements View.OnClickListener{

    private final static String FOOD_ID="MENU_ID";

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab_card;
    ElegantNumberButton number_button;



    FirebaseDatabase database;
    DatabaseReference food;

    String foodId;
    Food model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        // init firebase
        database=FirebaseDatabase.getInstance();
        food=database.getReference("Foods");
        // get Views
        food_name=findViewById(R.id.food_name_details);
        food_price=findViewById(R.id.food_price);
        food_description=findViewById(R.id.food_description);
        food_image=findViewById(R.id.image_food_details);
        fab_card=findViewById(R.id.fab_card);
        fab_card.setOnClickListener(this);
        number_button=findViewById(R.id.number_button);
        collapsingToolbarLayout=findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppBar);



        // get menu id from Intent
        if (getIntent()!=null){
            foodId=getIntent().getStringExtra(FOOD_ID);
            if (!foodId.isEmpty()&&foodId!=null){
                getDetailsFood(foodId);
            }
        }
    }

    private void getDetailsFood(String foodId) {
        food.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 model=dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(model.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(model.getName());
                food_price.setText(model.getPrice());
                food_description.setText(model.getDescription());
                food_name.setText(model.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static Intent newIntent(Context context, String menuId){
        Intent intent=new Intent(context,FoodDetails.class);
        intent.putExtra(FOOD_ID,menuId);
        return intent;
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id==R.id.fab_card){
            Order order =new Order(foodId,model.getName(),number_button.getNumber(),model.getPrice(),model.getDiscount());
            new Database(FoodDetails.this).addToCart(order);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        }
    }
}
