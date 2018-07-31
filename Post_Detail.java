package com.atwebpages.awaillixa.merofirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

public class Post_Detail extends AppCompatActivity {

    TextView title,description;
    ImageView Image;
    EditText addCommment;
    Button CommentBtn;
    String StrTitle,StrDescrption,ImageUrl,StrComment,postId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__detail);
        handeToolbarActions();

        title =findViewById(R.id.caption);
        description =findViewById(R.id.description2);
        Image =findViewById(R.id.image1);
        addCommment =findViewById(R.id.add_comment);
        CommentBtn = findViewById(R.id.comment_button);


        StrTitle = getIntent().getExtras().getString("tiltle");
        StrDescrption = getIntent().getExtras().getString("description");
        ImageUrl = getIntent().getExtras().getString("image");
        postId = getIntent().getExtras().getString("postId");

        title.setText(StrTitle);
        description.setText(StrDescrption);
        Glide.with(Image.getContext()).load(ImageUrl).into(Image);

        handeToolbarActions();

       CommentBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               StrComment = addCommment.getText().toString();
               if(!TextUtils.isEmpty(StrComment))
               {
                   addCommentToDatabase();
               }
               else{
                   addCommment.setError("Required");
               }
           }
       });


    }

    private void handeToolbarActions(){
        getSupportActionBar().setTitle("Post Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addCommentToDatabase(){
        CommentDAO comment = new CommentDAO();
        comment.setComment(StrComment);
        comment.setCommenterId(FirebaseAuth.getInstance().getUid());
        comment.setPostId(postId);

        String commentId=
        FirebaseDatabase.getInstance().getReference()
                .child("comments")
                .child(postId).push().getKey();

        FirebaseDatabase.getInstance().getReference()
                .child("comments")
                .child(postId)
                .child(commentId)
                .setValue(comment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(Post_Detail.this, "Added to Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
