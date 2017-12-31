package mif50.com.orderfoodsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.User;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    TextView txt_name,txt_phone,txt_password;
    Button btn_sign_up;
    FirebaseDatabase database;
    DatabaseReference user_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database=FirebaseDatabase.getInstance();
        user_table=database.getReference("User");

        txt_name=findViewById(R.id.txt_name_sign_up);
        txt_password=findViewById(R.id.txt_password_sign_up);
        txt_phone=findViewById(R.id.txt_phone_sign_up);
        btn_sign_up=findViewById(R.id.btn_sign_up);
        btn_sign_up.setOnClickListener(this);
    }
    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,SignUp.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.btn_sign_up){

            final ProgressDialog dialog=new ProgressDialog(this);
            dialog.setMessage("please wait ..... ");
            dialog.show();

            user_table.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // check if phone number exist
                    if (dataSnapshot.child(txt_phone.getText().toString()).exists()){
                        dialog.dismiss();
                        Toast.makeText(SignUp.this, "phone number already exists", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog.dismiss();
                        User user=new User(txt_name.getText().toString(),txt_password.getText().toString());
                        user_table.child(txt_phone.getText().toString()).setValue(user);
                        Toast.makeText(SignUp.this, "sign up success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
