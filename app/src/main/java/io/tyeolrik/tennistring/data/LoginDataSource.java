package io.tyeolrik.tennistring.data;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import io.tyeolrik.tennistring.R;
import io.tyeolrik.tennistring.data.model.LoggedInUser;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(final String username, final String password) {
        final LoggedInUser[] loggedInUser = {null};
        try {
            // TODO: handle loggedInUser authentication
            // Access a Cloud Firestore instance from your Activity
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final String TAG = "Firebase LOGIN";
            Log.d("TEST", "Before DB Collection");
            db.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String userName = "";
                                loggedInUser[0] =
                                        new LoggedInUser(
                                                java.util.UUID.randomUUID().toString(),
                                                "UnknownUser");
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TyeolRik Test", "TEST Google Firebase");
                                    Log.d(TAG, document.getId() + " => " + document.getData() + "Type: ");
                                    if (document.getData().get("id") == username && document.getData().get("pw") == password) {
                                        Log.d("Firebase LOGIN", "ID: " + userName + "\tPassword: " + password);
                                        loggedInUser[0] =
                                                new LoggedInUser(
                                                        java.util.UUID.randomUUID().toString(),
                                                        username);

                                        break;
                                    }
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
            Log.d("TEST", "After DB Collection");
            // Thread.sleep(10000); // 1초 기다림
            // return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        } finally {
            while(loggedInUser[0] == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Log.d("LOG", "No Return from Firebase Server");
                }
            }
            return new Result.Success<>(loggedInUser[0]);
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}