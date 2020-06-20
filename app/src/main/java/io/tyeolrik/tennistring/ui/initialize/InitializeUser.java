package io.tyeolrik.tennistring.ui.initialize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.util.Assert;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.tyeolrik.tennistring.R;

public class InitializeUser extends AppCompatActivity {

    FragmentManager fm;
    InitializeImportant fragmentImportant;
    InitializeAdditional fragmentAdditional;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize_user);
        setTitle("당신은 어떤 사람인지 궁금해요!");

        fm = getSupportFragmentManager();
        fragmentImportant = new InitializeImportant();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.initialize_framelayout, fragmentImportant).commitAllowingStateLoss();
    }
}