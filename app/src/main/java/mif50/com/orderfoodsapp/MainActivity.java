package mif50.com.orderfoodsapp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_sign_up,btn_sign_in;
    TextView txt_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_up=findViewById(R.id.btn_sign_up_home);
        btn_sign_in=findViewById(R.id.btn_sign_in_home);
        txt_login=findViewById(R.id.txt_login);
        Typeface face=Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txt_login.setTypeface(face);
        btn_sign_in.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_sign_in_home:
                startActivity(SignIn.newIntent(this));
                break;
            case R.id.btn_sign_up_home:
                startActivity(SignUp.newIntent(this));
                break;
        }
    }
}
