package com.example.firebaseproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseproject.Model.Chat;
import com.example.firebaseproject.Model.Users;
import com.example.firebaseproject.UserAdapter.MessageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = MessageActivity.class.getSimpleName();

    private static final String SERVER_KEY = "key=AAAA0XcsDpE:APA91bFKqSVwgB4e38QVH4WWXHjXEFZVqTO5b1HajVuEaSWMGX2L6mEfaBgjqwJDatg9V3hPz8HE2p2Ao7vnGbLaTokN0IwOY9GUpLPszY0wwu9jUOpvkluZVU9b-LM9yb-6ZHHxJeUz";
    private static final String CLIENT_REGISTRATION_TOKEN = "cUPw0s3VQ5WwM98eegBxOt:APA91bEusIfOReV5o6vPoIiq2Cv3W6wYSjgY-QLfD6C8E2N04uZ1NIj5pSudZPwITGNEZ0JDqEX5Ezm2ZBraCP8i-dYVRy4TV_L_1Eseae5ZxR8IR4tILNzSpXXIl1oZJVJ_v807YFgu";

    TextView username;
    ImageView imageView;
    ImageButton button_load;
    ImageButton button_load2;
    ImageButton button_load3;
    ImageButton button_load4;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    FirebaseStorage mStorage;
    String url;
    StorageReference storageRef;
    RemoteMessage remoteMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //   imageView = findViewById(R.id.image_message);
        button_load = findViewById(R.id.btn_send);
        button_load2 = findViewById(R.id.btn_send2);
        button_load3 = findViewById(R.id.btn_send3);
        button_load4 = findViewById(R.id.btn_send4);

        mStorage = FirebaseStorage.getInstance();
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

        storageRef.child("embarrassed.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(MessageActivity.this)
                        .load(uri.toString())
                        .error(R.mipmap.ic_launcher)
                        .resize(50, 50)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(button_load2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        storageRef.child("goodbye.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(MessageActivity.this)
                        .load(uri.toString())
                        .error(R.mipmap.ic_launcher)
                        .resize(50, 50)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(button_load3);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        storageRef.child("in-love.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(MessageActivity.this)
                        .load(uri.toString())
                        .error(R.mipmap.ic_launcher)
                        .resize(50, 50)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(button_load4);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        username = findViewById(R.id.username_meassge);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


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
        reference = FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);

                    //username.setText(user.getUsername());

                    //if (user.getImageURL().equals("default")) {
                    //    imageView.setImageResource(R.mipmap.ic_launcher);
                    // } else {
                    //    Glide.with(MessageActivity.this)
                    //            .load(user.getImageURL())
                    //            .into(imageView);
                }
                Users user = dataSnapshot.getValue(Users.class);

                readMessage(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        button_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("happy.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), userid, link_happy);
                    }
                });

//                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MessageActivity.this, new OnSuccessListener<InstanceIdResult>() {
//                    @Override
//                    public void onSuccess(InstanceIdResult instanceIdResult) {
//                        String token = instanceIdResult.getToken();
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.e("Token", token);
//                        Toast.makeText(MessageActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
                sendMessageToDeviceTask(CLIENT_REGISTRATION_TOKEN);
            }
        });
        button_load2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("embarrassed.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), userid, link_happy);
                    }
                });
                sendMessageToDeviceTask(CLIENT_REGISTRATION_TOKEN);
            }
        });
        button_load3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("goodbye.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), userid, link_happy);
                    }
                });
                sendMessageToDeviceTask(CLIENT_REGISTRATION_TOKEN);
            }
        });
        button_load4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("in-love.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), userid, link_happy);
                    }
                });
                sendMessageToDeviceTask(CLIENT_REGISTRATION_TOKEN);
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMessage(String myid, String userid, String imageurl) {
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void sendMessageToDeviceTask(String token) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessageToDevice(token);
            }
        }).start();
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void sendMessageToDevice(String targetToken) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "Message Title");
            jNotification.put("body", "Message body ");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            /*
            // We can add more details into the notification if we want.
            // We happen to be ignoring them for this demo.
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            */
            jdata.put("title", "data title");
            jdata.put("content", "data content");

            /***
             * The Notification object is now populated.
             * Next, build the Payload that we send to the server.
             */

            // If sending to a single client
            jPayload.put("to", targetToken); // CLIENT_REGISTRATION_TOKEN);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

            /***
             * The Payload object is now populated.
             * Send it to Firebase to send the message to the appropriate recipient.
             */
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", SERVER_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());
            outputStream.close();

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: " + resp);
                    Toast.makeText(MessageActivity.this, resp, Toast.LENGTH_LONG).show();
                }
            });
            Log.i(TAG, "Successfully sent notification to client: " + targetToken);
        } catch (JSONException | IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Helper function
     *
     * @param is
     * @return
     */
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}





