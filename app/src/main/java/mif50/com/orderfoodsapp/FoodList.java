package mif50.com.orderfoodsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import inter.ItemClickListener;
import model.Food;
import viewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {
    private final static String CATEGORY_ID="CATEGORYID";

    FirebaseDatabase database;
    DatabaseReference food;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    RecyclerView recycler_food_list;
    RecyclerView.LayoutManager layoutManager;

    MaterialSearchBar search_item;
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestion=new ArrayList<>();

    private String categoryId;// id of menu that contain all food in this menu


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        // init firebase
        database=FirebaseDatabase.getInstance();
        food=database.getReference("Foods");
        // inti recycler view
        recycler_food_list=findViewById(R.id.recycler_food_list);
        recycler_food_list.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recycler_food_list.setLayoutManager(layoutManager);
        //get categoryId from Home Activity
        if (getIntent()!=null){
            categoryId=getIntent().getStringExtra(CATEGORY_ID);
            if (!categoryId.isEmpty() && categoryId!=null){
                // load food list item
                loadFoodList(categoryId);
            }
        }

        search_item=findViewById(R.id.search_item);
        search_item.setHint("enter your food");
        loadSuggest(); // load suggestion from firebase
        search_item.setLastSuggestions(suggestion);
        search_item.setCardViewElevation(10);
        search_item.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<String> suggest=new ArrayList<>();
                for (String value:suggestion){
                    if (value.toLowerCase().contains(search_item.getText().toString().toLowerCase())){
                        suggest.add(value);
                    }
                }
                search_item.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search_item.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                // when search bar is closed restore original adapter
                if(!enabled){
                    recycler_food_list.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString());

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(String text) {
        // use new adapter
        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,FoodViewHolder.class,
                food.orderByChild("name").equalTo(text)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.name_food_item.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.image_food_item);
                final Food localFood=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // move to foodDetails and sent food id
                        startActivity(FoodDetails.newIntent(FoodList.this,searchAdapter.getRef(position).getKey()));
                        //Toast.makeText(FoodList.this, ""+localFood.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        recycler_food_list.setAdapter(searchAdapter);// set adapter for search result
    }

    private void loadSuggest() {

        Query query=food.orderByChild("menuId").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    Food item =data.getValue(Food.class);
                    suggestion.add(item.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadFoodList(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,R.layout.food_item,FoodViewHolder.class,
                food.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.name_food_item.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.image_food_item);
                final Food localFood=model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // move to foodDetails and sent food id
                        startActivity(FoodDetails.newIntent(FoodList.this,adapter.getRef(position).getKey()));
                        //Toast.makeText(FoodList.this, ""+localFood.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        // set up adapter
        recycler_food_list.setAdapter(adapter);
    }

    public static Intent newIntent(Context context,String categoryId){
        Intent intent=new Intent(context,FoodList.class);
        intent.putExtra(CATEGORY_ID,categoryId);
        return intent;
    }
}
