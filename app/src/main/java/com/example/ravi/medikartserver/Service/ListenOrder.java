package com.example.ravi.medikartserver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;

import com.example.ravi.medikartserver.Model.Request;
import com.example.ravi.medikartserver.OrderStatus;
import com.example.ravi.medikartserver.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {

FirebaseDatabase db;
DatabaseReference orders;

    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        orders=db.getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
    return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//trigger here
        Request request=dataSnapshot.getValue(Request.class);
        if(request.getStatus().equals("0"))
        {
            showNotification(dataSnapshot.getKey(),request);
        }

    }

    private void showNotification(String key, Request request) {
        Intent intent=new Intent(getBaseContext(), OrderStatus.class);
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,0);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true).
                setDefaults(Notification.DEFAULT_ALL).
                setTicker("agency").
                setContentInfo("new order").
                setContentText("you received new order # "+key).
                setSmallIcon(R.mipmap.ic_launcher).
        setContentIntent(contentIntent);

        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //if you want many notification to show
        int randomint=new Random().nextInt(9999-1)+1;
        manager.notify(randomint,builder.build());

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
