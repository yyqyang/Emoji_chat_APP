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
import com.example.firebaseproject.Notification.Token;
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

    TextView username;
    ImageButton button_load;
    ImageButton button_load2;
    ImageButton button_load3;
    ImageButton button_load4;
    FirebaseUser fuser;
    DatabaseReference reference;
    DatabaseReference tokenReference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    FirebaseStorage mStorage;
    StorageReference storageRef;
    private String receiverUserName;

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

        // The id of the other user of the chat.
        String receiverUserid = intent.getStringExtra("userid");
        getReceiverUserName(receiverUserid);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.w(TAG, "Failed finding user info for userId: " + fuser.getUid());
                    return;
                }
                Users user = dataSnapshot.getValue(Users.class);
                readMessage(fuser.getUid(), receiverUserid, user.getImageURL());
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
                        sendMessage(fuser.getUid(), receiverUserid, link_happy);
                    }
                });
                sendMessageToDeviceTask(fuser.getEmail(), receiverUserid);
            }
        });
        button_load2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("embarrassed.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), receiverUserid, link_happy);
                    }
                });
                sendMessageToDeviceTask(fuser.getEmail(), receiverUserid);
            }
        });
        button_load3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("goodbye.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), receiverUserid, link_happy);
                    }
                });
                sendMessageToDeviceTask(fuser.getEmail(), receiverUserid);
            }
        });
        button_load4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageRef.child("in-love.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String link_happy = uri.toString();
                        sendMessage(fuser.getUid(), receiverUserid, link_happy);
                    }
                });
                sendMessageToDeviceTask(fuser.getEmail(), receiverUserid);
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

    private void getReceiverUserName(String receiverUserid) {
        Log.i(TAG, "Finding username for userid: " + receiverUserid);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyUsers")
                .child(receiverUserid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    Log.i(TAG, "Found username: " + user.getUsername());
                    receiverUserName = user.getUsername();
                    if (!receiverUserName.isEmpty()) {
                        username.setText(receiverUserName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Send notification to user with the given receiverId
     */
    public void sendMessageToDeviceTask(String senderId, String receiverId) {
        // Get the client token of the user to send notification to
        tokenReference = FirebaseDatabase.getInstance().getReference("Tokens")
                .child(receiverId);
        tokenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.w(TAG,"Token data does not exist for user: " + receiverId);
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Sending notification to " + receiverId);
                        Token token = snapshot.getValue(Token.class);
                        sendMessageToDevice(token.getToken(), senderId);
                    }
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Pushes a notification to a given device-- in particular, this device,
     * because that's what the instanceID token is defined to be.
     */
    private void sendMessageToDevice(String targetToken, String senderId) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            jNotification.put("title", "Got a message from " + senderId);
            jNotification.put("body", "Got a new sticker");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");

            jdata.put("title", "");
            jdata.put("content", "data content");

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
                    // Toast.makeText(MessageActivity.this, resp, Toast.LENGTH_LONG).show();
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





