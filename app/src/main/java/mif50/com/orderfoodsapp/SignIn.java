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

import common.Common;
import model.User;

public class SignIn extends AppCompatActivity implements View.OnClickListener{

    Button btn_sign_in;
    TextView txt_phone,txt_password;

    FirebaseDatabase database;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        database=FirebaseDatabase.getInstance();
        user=database.getReference("User");

        txt_phone=findViewById(R.id.txt_phone_sign_in);
        txt_password=findViewById(R.id.txt_password_sign_in);
        btn_sign_in=findViewById(R.id.btn_sign_in);
        btn_sign_in.setOnClickListener(this);
    }
    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,SignIn.class);
        return intent;
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();

        if (id==R.id.btn_sign_in){
            final ProgressDialog dialog =new ProgressDialog(this);
            dialog.setMessage("please wait ... ");
            dialog.show();

            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // check if phone number is exist
                    if (dataSnapshot.child(txt_phone.getText().toString()).exists()){
                        // get user info
                        dialog.dismiss();
                        User login = dataSnapshot.child(txt_phone.getText().toString()).getValue(User.class);
                        if (login.getPassword().equals(txt_password.getText().toString())){
                            //Toast.makeText(SignIn.this, "login success", Toast.LENGTH_SHORT).show();
                            login.setPhone(txt_phone.getText().toString());
                            Common.currentUser=login;
                            startActivity(Home.newItent(SignIn.this));
                            finish();
                        }else{
                            Toast.makeText(SignIn.this, "password is wrong", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        dialog.dismiss();
                        Toast.makeText(SignIn.this, "user not exists", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
