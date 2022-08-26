package com.example.dater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class AddSpotActivity extends AppCompatActivity {

    FirebaseFirestore db;

    double latitude = 0;
    double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot);

        db = FirebaseFirestore.getInstance();

        Intent intentFromMain = getIntent();
        latitude = intentFromMain.getDoubleExtra(MainActivity.LATITUDE, 0);
        longitude = intentFromMain.getDoubleExtra(MainActivity.LONGITUDE, 0);

        TextView spotLocation = findViewById(R.id.textView_addSpot_location);
        Resources res = getResources();
        String strLat = new DecimalFormat("#.0#").format(latitude);
        String strLon = new DecimalFormat("#.0#").format(longitude);
        String location = String.format(res.getString(R.string.addSpot_locationCoordinates), strLat, strLon);
        spotLocation.setText(location);
    }

    public void onAddSpotButtonClicked(View view) {
        EditText editTextSpotName = findViewById(R.id.editText_addSpot_name);
        EditText editTextSpotSnippet = findViewById(R.id.editText_addSpot_snippet);
        EditText editTextSpotDescription = findViewById(R.id.editText_addSpot_description);

        String spotName = editTextSpotName.getText().toString();
        String spotSnippet = editTextSpotSnippet.getText().toString();
        String spotDescription = editTextSpotDescription.getText().toString();

        if (spotName.equals("") || spotDescription.equals("")) {
            /*
            Source:
            https://stackoverflow.com/questions/2115758/how-do-i-display-an-alert-dialog-on-android
             */
            new AlertDialog.Builder(this)
                    .setTitle("OI OI OI!")
                    .setMessage("Please enter the name and location!")

                    // A null listener allows the button to dismiss the dialog and
                    // take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .show();
        } else {
            addSpot(spotName, latitude, longitude, spotSnippet, spotDescription);
        }
    }

    void addSpot(String name, double latitude, double longitude,String snippet, String description) {
        Spot spot = new Spot(name, latitude, longitude,snippet, description);

        // Add a new document with a generated ID
        db.collection("spots")
                .add(spot)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Debug", "DocumentSnapshot added with ID: "
                                + documentReference.getId());
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Debug", "Error adding document", e);
                    }
                });
    }
}