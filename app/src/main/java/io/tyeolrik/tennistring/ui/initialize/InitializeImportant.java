package io.tyeolrik.tennistring.ui.initialize;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import io.tyeolrik.tennistring.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitializeImportant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitializeImportant extends Fragment {

    EditText initialize_userName, initialize_userTeam;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ImageView initialize_userImage;
    Button initialize_next_button;
    Animation _shaking;

    private static FirebaseUser user;

    public static InitializeImportant newInstance(FirebaseUser currentUser) {
        return new InitializeImportant();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initialize_important, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        // BoilerPlate
        initialize_userName = (EditText) view.findViewById(R.id.initialize_userName);
        initialize_userTeam = (EditText) view.findViewById(R.id.initialize_userTeam);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        // Inflate the layout for this fragment
        initialize_userImage = (ImageView) view.findViewById(R.id.initialize_userImage);
        String userImageURL = user.getPhotoUrl().toString();
        if(userImageURL.contains("google")) {
            userImageURL = userImageURL.replace("s96-c", "s400-c");
        } else if (userImageURL.contains("facebook")) {
            userImageURL = userImageURL + "?type=large";
        }
        Log.d("Picasso", "User PhotoURL: " + user.getPhotoUrl().toString() + "?height=500");
        Picasso.get().load(userImageURL).placeholder(R.drawable.ic_round_add_24).into(initialize_userImage);

        // Make ImageView circle
        initialize_userImage.setBackground(new ShapeDrawable(new OvalShape()));
        initialize_userImage.setClipToOutline(true);

        initialize_next_button = (Button) view.findViewById(R.id.initialize_next_button);
        initialize_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialize_userName.getText().toString().length() == 0) {
                    _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            initialize_userName.setBackgroundResource(R.color.colorAccent);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            initialize_userName.setBackgroundResource(R.color.fui_transparent);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    initialize_userName.startAnimation(_shaking);
                }

                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            radioGroup.setBackgroundResource(R.color.colorAccent);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            radioGroup.setBackgroundResource(R.color.fui_transparent);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    radioGroup.startAnimation(_shaking);
                }

                if (initialize_userTeam.getText().toString().length() == 0) {
                    _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            initialize_userTeam.setBackgroundResource(R.color.colorAccent);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            initialize_userTeam.setBackgroundResource(R.color.fui_transparent);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    initialize_userTeam.startAnimation(_shaking);
                }

                if ((initialize_userName.getText().toString().length() != 0) && (radioGroup.getCheckedRadioButtonId() != -1) && (initialize_userTeam.getText().toString().length() != 0)) {
                    InitializeAdditional nextFragment = new InitializeAdditional();
                    Bundle arguments = new Bundle();
                    arguments.putString("Username", initialize_userName.getText().toString());
                    arguments.putString("UserTeam", initialize_userTeam.getText().toString());
                    arguments.putString("UserCategory", ((RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());
                    nextFragment.setArguments(arguments);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.initialize_framelayout, nextFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return view;
    }
}