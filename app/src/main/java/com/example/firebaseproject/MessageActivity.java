package com.example.firebaseproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {
    TextView username;
    ImageView imageView;
    ImageButton button_load;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;


    FirebaseStorage mStorage;
    String url;
    StorageReference storageRef;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

     //   imageView = findViewById(R.id.image_message);
        button_load  = findViewById(R.id.btn_send);

        mStorage= FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://citric-hawk-307721.appspot.com");


        storageRef.child("happy.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(MessageActivity.this)
                        .load(uri.toString())
                        .error(R.mipmap.ic_launcher)
                        .resize(50, 50)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(button_load);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        username = findViewById(R.id.username_meassge);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        intent = getIntent();
        String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Myusers").child(userid);
        //reference.addValueEventListener(new ValueEventListener() {
          //  @Override
          //  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           //       Users user = dataSnapshot.getValue(Users.class);
           //       username.setText(user.getUsername());

          //      if (user.getImageURL().equals("default")) {
          //          imageView.setImageResource(R.mipmap.ic_launcher);
         //       } else {
         //           Glide.with(MessageActivity.this)
          //                  .load(user.getImageURL())
          //                  .into(imageView);
          //      }
          //  }

         //   @Override
           // public void onCancelled(@NonNull DatabaseError error){
        //        }
          //  });
        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("happy.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) { String link_happy = uri.toString();
                    sendMessage(fuser.getUid(),userid,link_happy);
                    }});

            }
        });

    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);
    }

}
