package com.atwebpages.awaillixa.merofirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class LoginPage extends AppCompatActivity {


    EditText edit1,edit2;
    Button but,but1;
    String s1,s2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(!TextUtils.isEmpty(FirebaseAuth.getInstance().getUid())){
            Intent intent = new Intent(LoginPage.this, FeedsDisplay.class);
            startActivity(intent);
        }else {

            setContentView(R.layout.activity_login_page);
            edit1 = findViewById(R.id.email);
            edit2 = findViewById(R.id.pass);
            but = findViewById(R.id.Butt);
            but1 = findViewById(R.id.but1);

            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()) {
                        Toast.makeText(LoginPage.this, "Validated input", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            but1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                    startActivity(intent);
                }
            });
        }

    }
    private boolean validate(){
        boolean isValid = false;
        s1 = edit1.getText().toString();
        s2 = edit2.getText().toString();



        if(TextUtils.isEmpty(s1)){
            edit1.setError("Required Email");
        }
        else if(TextUtils.isEmpty(s2)){
            edit2.setError("Required Password");
        }
        else if(s2.length()<8){
            edit2.setError("Password should be 8 words");
        }
        else{
            isValid=true;
        }
        return isValid;

    }

}
