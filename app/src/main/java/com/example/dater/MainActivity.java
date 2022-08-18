package com.example.dater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // test change
    private static final String TAG = "MainActivity";
    TextView textViewUsername, textViewEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {

            DocumentReference docRef = db.collection("users").document(currentUser.getUid());
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    Log.d(TAG, "User ID: " + currentUser.getUid());
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        assert document != null;
//                        if (document.exists()) {
//                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    textViewUsername = findViewById(R.id.textView_main_test);
                    textViewEmail = findViewById(R.id.textView_main_test2);

                    textViewUsername.setText(user.getUsername());
                    textViewEmail.setText(user.getEmail());
                }
            });
        }

        Button buttonLogout = findViewById(R.id.button_main_logoutButton);
        buttonLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });


    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}