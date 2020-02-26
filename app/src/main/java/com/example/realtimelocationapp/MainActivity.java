package com.example.realtimelocationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realtimelocationapp.Model.User;
import com.example.realtimelocationapp.Utils.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

//import com.google.android.gms.common.internal.service.Common;

public class MainActivity extends AppCompatActivity {

    DatabaseReference user_information;
    private static final int MY_REQUEST_CODE = 3434; // we can use any number
    List<AuthUI.IdpConfig> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Paper.init(this);
        Paper.init(this);

        //init firebase
        user_information = FirebaseDatabase.getInstance().getReference(Common.USER_INFORMATION);

        //init provider
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );


        // Request permission location
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                            // User is signed out
                            ShowSignInOptions();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(MainActivity.this, "Your must accept permission to use app", Toast.LENGTH_LONG);
                        Intent intent = new Intent(MainActivity.this, OnBoardingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }


    @SuppressLint("ResourceType")
    private void ShowSignInOptions()
    {
        startActivityForResult(
                 AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.og_imag_icon)
                .setTheme(R.style.AppTheme)
                .setAvailableProviders(providers)
                .build(), MY_REQUEST_CODE

        );
    }

    //ctrl+o

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //checkk if user exists on Database
                user_information.orderByKey()
                        .equalTo(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() == null ) // user is not exist
                                {
                                    //Creating user named Model.
                                    if (!dataSnapshot.child(firebaseUser.getUid()).exists())
                                    {
                                        Common.loggedUser = new User(firebaseUser.getUid(), firebaseUser.getEmail());
                                        //Add to database
                                        user_information.child(Common.loggedUser.getUid())
                                                .setValue(Common.loggedUser);
                                    }
                                }
                                else // if user is available
                                {
                                    Common.loggedUser = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                                }

                                // Save UID to storage to update location from background
                                Paper.book().write(Common.USER_UID_SAVE_KEY, Common.loggedUser.getUid());
                                updateToken(firebaseUser);
                                setupUI();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Toast.makeText(MainActivity.this, "ERRRORRR", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }


    private void setupUI() {

        //Navigate Home
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();

    }

    private void updateToken(final FirebaseUser firebaseUser) {

        final DatabaseReference tokens = FirebaseDatabase.getInstance()
                .getReference(Common.TOKENS);

        //Get Token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        tokens.child(firebaseUser.getUid())
                                .setValue(instanceIdResult.getToken());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //to clear all old opened activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
