package com.atwebpages.awaillixa.merofirebase;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    EditText edit1,edit2,edit3;
    Button but1;
    String s1,s2,s3,s4;
    RadioGroup radio;
    TextView R12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        edit1 = findViewById(R.id.Nam);
        edit2 = findViewById(R.id.Ema);
        radio = findViewById(R.id.Radio);
        edit3 = findViewById(R.id.pass);
        but1 = findViewById(R.id.butt21);
         R12 = (TextView) findViewById(R.id.alr);






        R12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);

            }
        });

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    User user = new User();
                    user.setEmail(s2);
                    user.setName(s1);
                    user.setGender(s4);
                    user.setPassword(s3);
                    registryUserToFirebase(user);


                }
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group,@IdRes int checkedId) {
                RadioButton checkedButton = (RadioButton) findViewById(checkedId);
                s4= checkedButton.getText().toString();
            }
        });
    }

        private boolean validate() {
            boolean isValid = false;
            s1 = edit1.getText().toString();
            s2 = edit2.getText().toString();
            s3 = edit3.getText().toString();
            if (TextUtils.isEmpty(s1)) {
                edit1.setError("Required Name");
            } else if (TextUtils.isEmpty(s2)) {
                edit2.setError("Required Email");
            } else if (TextUtils.isEmpty(s4)) {
                Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(s3)) {
                edit3.setError("Required Password");
            } else if (s3.length() < 8) {
                edit3.setError("Password should be 8 words");
            } else {
                isValid = true;
            }
            return isValid;

        }
        private void registryUserToFirebase(final User user){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(),user.getPassword())

                    .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("SignUp","inside onComplete");
                    if(task.isSuccessful()){
                        Log.d("SignUp","inside Successful");
                        Toast.makeText(SignUpPage.this, "Successful", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference()
                                .child("user")
                                .child(task.getResult().getUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpPage.this, "added", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(SignUpPage.this, "failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Log.d("Signup","failed"+task.getException().getMessage() );
                        Toast.makeText(SignUpPage.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



}



