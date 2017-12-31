package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import model.Order;

/**
 * Created by mohamed on 11/27/17.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="food.db";
    private static final int DB_VERSION=1;
    private Context context;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    // method to get data from database and save it to list<Order>
    public List<Order> getCarts(){
        SQLiteDatabase db=this.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder=new SQLiteQueryBuilder();
        String []sqlSelect={"productId","productName","quantity","price","discount"};
        String sqlTable="orderDetail";
        queryBuilder.setTables(sqlTable);
        Cursor c=queryBuilder.query(db,sqlSelect,null,null,null,null,null);
        final List<Order> result=new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Order(c.getString(c.getColumnIndex("productId")),
                        c.getString(c.getColumnIndex("productName")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("discount"))
                        ));
            }while(c.moveToNext());
        }
        return result;
    }
    // add data to database orderDetail
    public void addToCart(Order order){
        SQLiteDatabase db=this.getReadableDatabase();
        String query=String.format("insert into orderDetail (productId,productName,quantity,price,discount) " +
                "values ('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
        db.close();
    }
    // delete data from database orderDetail
    public void cleanCart(){
        SQLiteDatabase db=getReadableDatabase();
        String query=String.format("delete from orderDetail");
        db.execSQL(query);
    }

}
