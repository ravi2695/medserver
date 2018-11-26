package com.example.ravi.medikartserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ravi.medikartserver.Common.Common;
import com.example.ravi.medikartserver.Model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class logIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    edtPassword=findViewById(R.id.password);
    edtPhone=findViewById(R.id.phoneNumber);
    btnSignIn=findViewById(R.id.signedIn);

    //initialize database
        FirebaseApp.initializeApp(this);
    db=FirebaseDatabase.getInstance();
    users=db.getReference("Users");
    btnSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());
        }
    });

    }

    private void signInUser(String phone, String password) {
        final ProgressDialog pd=new ProgressDialog(logIn.this);
        pd.setMessage("please wait..");
        pd.show();
        final String localPhone=phone;
        final String localPassword=password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(localPhone).exists())
                {
                    pd.dismiss();
                    User user=dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    //if the IsStaff is true in database then it wil enter the condition for further verification as password
                    if(Boolean.parseBoolean(user.getIsStaff()))
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            //login
                            Intent intent=new Intent(logIn.this,menu.class);
                            Common.currentUser=user;
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(logIn.this,"wrong password !!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(logIn.this,"login with staff ID..",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    pd.dismiss();
                    Toast.makeText(logIn.this,"inavalid user!!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
