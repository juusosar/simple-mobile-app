package com.example.mobiletechapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        uploadSingleValueToRealtimeDatabase("Week7", "Testing realtime database");
        downloadSingleValueFromRealtimeDB("Week7");

        MyLocationPlace currentLocation =
                new MyLocationPlace(-35.2369777,149.0841217, "UC Building 6");
        uploadClassInstanceToRealtimeDB(currentLocation);
        downloadClassInstanceFromRealtimeDB();

        uploadSingleResourceFileToCloudStorage(R.drawable.dogs, "doggo");
        downloadSingleFilefromStorage("doggo");

        //Button menuButton = findViewById(R.id.buttonUiEvent);
        //menuButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //    }
        // });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_uievent:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("message", "Hello World!");
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("message", "Hello World!");
        startActivity(intent);
    }

    public void viewMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void viewLocationServices(View view) {
        Intent intent = new Intent(this, LocationServicesActivity.class);
        startActivity(intent);
    }

    public void showStreetView(View view) {
        Intent intent = new Intent(this, StreetViewActivity.class);
        startActivity(intent);
    }

    public void openSQLActivity(View view) {
        Intent intent = new Intent(this, SQLiteActivity.class);
        startActivity(intent);
    }

    public void viewDogs(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("image", "dogs");
        startActivity(intent);
    }

    public void viewDucks(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("image", "ducks");
        startActivity(intent);
    }

    public void uploadSingleValueToRealtimeDatabase(String name, String value) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Mobile Tech");
        dbRef.child(name).setValue(value);
    }

    public void downloadSingleValueFromRealtimeDB(String name) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Mobile Tech");

        // Create the addListenerForSingleValueEvent event listener from dbRef that
        // contains 2 event listeners (Steps for creating a runtime event listener
        // are in Week 4 review slides) then add the following lines to
        // the onDataChange event listener and uncomment them
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text_downloaded = snapshot.child(name).getValue().toString();
                Toast.makeText(getApplicationContext(), text_downloaded, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void uploadClassInstanceToRealtimeDB(MyLocationPlace myLocationPlace) {
        if (myLocationPlace == null)
            return;
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String key = "Juuson avain";
        //dbRef.push().getKey(); // to generate a random key
        dbRef.child(key).child("latitude").setValue(myLocationPlace.getLatitude());
        dbRef.child(key).child("longitude").setValue(myLocationPlace.getLongitude());
        dbRef.child(key).child("address").setValue(myLocationPlace.getAddress());
    }

    public void downloadClassInstanceFromRealtimeDB() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        // Create the addChildEventListener event listener from dbRef
        // that contains 5 event listeners then comment out the following lines
        // and add them to the onChildAdded event listener

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                for (DataSnapshot key : snapshot.getChildren()) {
                     Toast.makeText(getApplicationContext(),
                                    snapshot.child(key.getKey()).getValue().toString(),
                                    Toast.LENGTH_LONG).show();
                     }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void uploadSingleResourceFileToCloudStorage(int resourceId, String filenameOnCloud) {
        // Reference to cloud storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Find the path to a resource file using its resource id
        Uri uri = Uri.parse("android.resource://" +
                R.class.getPackage().getName() + "/" + resourceId);
        // Upload the resource file to cloud storage and rename it to filenameOnCloud
        storageRef.child(filenameOnCloud).putFile(uri);
    }

    public void downloadSingleFilefromStorage(String filenameOnCloud) {
        // fileOnPhone is a local file used to download file filenameOnCloud to it
        File fileOnPhone = null;
        try {
            fileOnPhone = File.createTempFile("temp_", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get reference to file in storage with this filename
        StorageReference fileRef = FirebaseStorage.getInstance().getReference()
                .child(filenameOnCloud);

        // Check if the filenameOnCloud file exists on Cloud storage
        if (fileRef == null) return;

        // Download the filenameOnCloud file to localFile
        // and use localFile to display image on Image View
        if (fileOnPhone != null) {
            File finalLocalFile = fileOnPhone;
            // get filenameOnCloud from cloud storage & save it to fileOnPhone
            fileRef.getFile(fileOnPhone)
                    .addOnSuccessListener(
                            new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Uri uri = Uri.fromFile(finalLocalFile);
                                    ImageView imageView = (ImageView) findViewById(R.id.imageViewStart);
                                    imageView.setImageURI(uri);
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(getApplicationContext(), "Unable to download ", Toast.LENGTH_SHORT).show();
                                }
                            });
        }
    }


}