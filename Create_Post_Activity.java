package com.atwebpages.awaillixa.merofirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Create_Post_Activity extends AppCompatActivity {
    Uri uri;
    Bitmap bitmap;
    StorageReference storageReference;
    EditText caption, description;
    ImageView Image;
    Button SelectImage, button2;
    String c1, d1,b1;
    final Integer PICK_IMAGE = 1;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__post_);




        caption = findViewById(R.id.cap);
        description = findViewById(R.id.des);
        SelectImage = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        Image = findViewById(R.id.ima);
        progress =findViewById(R.id.show_pb) ;

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    addPostToDatabase();
                    Toast.makeText(Create_Post_Activity.this, "Valid", Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    private void addPostToDatabase(){
        progress.setVisibility(View.VISIBLE);
        final DAO post=new DAO();
        post.setPostIdUploader(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String postid = FirebaseDatabase.getInstance().getReference().child("posts").push().getKey();
        post.setPostId(postid);
        post.setCaption(c1);
        post.setDescription(d1);

        if(bitmap!=null){
            storageReference=FirebaseStorage.getInstance().getReference().child("post_pictures").child(String.valueOf(System.currentTimeMillis()));
            storageReference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }

                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        String downloadurl = uri.toString();
                        post.setImageUrl(downloadurl.toString());
                        uploadPost(post);
                        Log.d("Downlaod url", "onComplete: Url: "+ downloadurl.toString());

                        //progress.setVisibility(View.GONE);
                       // Toast.makeText(Create_Post_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }
            });
                    }
                    else {
                          post.setImageUrl("");
                          uploadPost(post);

                          }
        }
        private void uploadPost(DAO post){
        FirebaseDatabase.getInstance().getReference().child("posts")
                .child(post.getPostId())
                .setValue(post)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(Create_Post_Activity.this, "success", Toast.LENGTH_SHORT).show();
                            progress.setVisibility(View.GONE);
                        onBackPressed();
                        }else{
                            Toast.makeText(Create_Post_Activity.this, "error is:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }


    private boolean validate() {
        boolean isValid = false;
        c1 = caption.getText().toString();
        d1 = description.getText().toString();


        if (TextUtils.isEmpty(c1)) {
            caption.setError("Required Email");
        } else if (TextUtils.isEmpty(d1)) {
            description.setError("Required Password");
        } else {
            isValid = true;
        }
        return isValid;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE ) {
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}