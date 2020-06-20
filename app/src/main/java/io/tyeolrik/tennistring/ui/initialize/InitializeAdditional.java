package io.tyeolrik.tennistring.ui.initialize;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.tyeolrik.tennistring.MainActivity;
import io.tyeolrik.tennistring.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitializeAdditional#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitializeAdditional extends Fragment {

    private static int thisYear, thisMonth, arrayBrandLength, arrayWeightLength, arrayHeadSizeLength;
    private static boolean[] dataWatcher = new boolean[6];

    String[] racketBrands;
    String[] racketHeadSize;
    String[] racketWeight;
    NumberPicker racketBrandPicker;
    TextView intializeRacketBrandName, initializeRacketGram, initializeRacketHeadSize, initializeFirstTennisYear, initializeFirstTennisMonth;
    Button initializeEndButton;
    EditText initializeRacketName;

    FirebaseFirestore db;

    Animation shaking;

    int nowPickerState = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERNAME = "Username";
    private static final String USER_TEAM = "UserTeam";
    private static final String USER_CATEGORY = "UserCategory";

    // TODO: Rename and change types of parameters
    private String userName;
    private String userTeam;
    private String userCategory;

    public InitializeAdditional() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InitializeAdditional.
     */
    // TODO: Rename and change types and number of parameters
    public static InitializeAdditional newInstance(String param1, String param2) {
        return new InitializeAdditional();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(USERNAME);
            userTeam = getArguments().getString(USER_TEAM);
            userCategory = getArguments().getString(USER_CATEGORY);
            Log.d("Bundle", "UserName : " + userName + " UserTeam : " + userTeam + " UserCategory : " + userCategory);
        }
        thisYear    = Calendar.getInstance().get(Calendar.YEAR);
        thisMonth   = Calendar.getInstance().get(Calendar.MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initialize_additional, container, false);

        racketBrands = (String[]) Arrays.asList(getResources().getStringArray(R.array.racket_brand)).toArray();
        racketHeadSize = (String[]) Arrays.asList(getResources().getStringArray(R.array.racket_head_size)).toArray();
        racketWeight = (String[]) Arrays.asList(getResources().getStringArray(R.array.racket_weight)).toArray();

        arrayBrandLength = racketBrands.length;
        arrayHeadSizeLength = racketHeadSize.length;
        arrayWeightLength = racketWeight.length;

        // BoilerPlate
        initializeFirstTennisYear   = (TextView) view.findViewById(R.id.initialize_first_tennis_year);
        initializeFirstTennisYear.setText(String.valueOf(thisYear));
        initializeFirstTennisMonth  = (TextView) view.findViewById(R.id.initialize_first_tennis_month);
        initializeFirstTennisMonth.setText(String.format(Locale.KOREA, "%02d", thisMonth));
        intializeRacketBrandName    = (TextView) view.findViewById(R.id.intializeRacketBrandName);
        initializeRacketGram        = (TextView) view.findViewById(R.id.initializeRacketGram);
        initializeRacketHeadSize    = (TextView) view.findViewById(R.id.initializeRacketHeadSize);
        racketBrandPicker           = (NumberPicker) view.findViewById(R.id.initializeRacketPicker);
        initializeEndButton         = (Button) view.findViewById(R.id.initializeEndButton);
        initializeRacketName        = (EditText) view.findViewById(R.id.initializeRacketName);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(racketBrandPicker.getVisibility() == View.VISIBLE) {
                    racketBrandPicker.setVisibility(View.GONE);
                    Log.d("OnClick", "racketBrandPicker.setVisibility(View.GONE)");
                }
            }
        });



        initializeFirstTennisYear.setOnClickListener(popupPicker);
        initializeFirstTennisMonth.setOnClickListener(popupPicker);
        intializeRacketBrandName.setOnClickListener(popupPicker);
        initializeRacketGram.setOnClickListener(popupPicker);
        initializeRacketHeadSize.setOnClickListener(popupPicker);

        initializeEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dataWatcher[0]) {
                    // Tennis Year
                    final TextView thisView = initializeFirstTennisYear;
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            thisView.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thisView.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    thisView.startAnimation(_shaking);
                }
                if (!dataWatcher[1]) {
                    // Tennis Month
                    final TextView thisView = initializeFirstTennisMonth;
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            thisView.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thisView.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    thisView.startAnimation(_shaking);
                }
                if (!dataWatcher[2]) {
                    // Racket Brand
                    final TextView thisView = intializeRacketBrandName;
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            thisView.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thisView.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    thisView.startAnimation(_shaking);
                }
                if (!dataWatcher[3]) {
                    // Racket Gram
                    final TextView thisView = initializeRacketGram;
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            thisView.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thisView.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    thisView.startAnimation(_shaking);
                }
                if (!dataWatcher[4]) {
                    // Racket Head Size
                    final TextView thisView = initializeRacketHeadSize;
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            thisView.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thisView.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    thisView.startAnimation(_shaking);
                }
                if (initializeRacketName.getText().toString().length() == 0) {
                    dataWatcher[5] = false;
                } else {
                    dataWatcher[5] = true;
                }
                if (!dataWatcher[5]) {
                    // Racket Name
                    final EditText thisView = initializeRacketName;
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            thisView.setBackgroundResource(R.color.colorAccent);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            thisView.setBackgroundResource(R.color.fui_transparent);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    thisView.startAnimation(_shaking);
                }
                if (dataWatcher[0] && dataWatcher[1] && dataWatcher[2] && dataWatcher[3] && dataWatcher[4] && dataWatcher[5]) {
                    // Send data to Firebase
                    Map<String, Object> user = new HashMap<>();
                    user.put("UserName", userName);
                    user.put("UserTeam", userTeam);
                    user.put("UserCategory", userCategory);
                    user.put("StartYear", initializeFirstTennisYear.getText().toString());
                    user.put("StartMonth", initializeFirstTennisMonth.getText().toString());
                    user.put("RacketBrand", intializeRacketBrandName.getText().toString());
                    user.put("RacketName", initializeRacketName.getText().toString());
                    user.put("RacketGram", initializeRacketGram.getText().toString());
                    user.put("RacketHeadSize", initializeRacketHeadSize.getText().toString());
                    db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .set(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Firebase", "SUCCESS!");
                                        Intent userInformation = new Intent(getContext(), MainActivity.class);
                                        startActivity(userInformation);
                                    }
                                }
                            });
                }
            }
        });
        db = FirebaseFirestore.getInstance();
        return view;
    }

    TextView.OnClickListener popupPicker = new TextView.OnClickListener() {
        @Override
        public void onClick(View view) {
            racketBrandPicker.setVisibility(View.GONE);
            racketBrandPicker.setDisplayedValues(null);
            int pickerVisibility = racketBrandPicker.getVisibility();
            if(pickerVisibility == View.GONE) {
                racketBrandPicker.setVisibility(View.VISIBLE);
            }
            switch (view.getId()) {
                case R.id.initialize_first_tennis_year:
                    racketBrandPicker.setMinValue(1950);
                    racketBrandPicker.setMaxValue(thisYear);
                    racketBrandPicker.setValue(thisYear);
                    racketBrandPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            initializeFirstTennisYear.setText(String.valueOf(i1));
                            dataWatcher[0] = true;
                        }
                    });
                    break;

                case R.id.initialize_first_tennis_month:
                    racketBrandPicker.setMinValue(1);
                    racketBrandPicker.setMaxValue(12);
                    racketBrandPicker.setValue(thisMonth+1);
                    racketBrandPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            initializeFirstTennisMonth.setText(String.format(Locale.KOREA, "%02d", i1));
                            dataWatcher[1] = true;
                        }
                    });
                    break;

                case R.id.intializeRacketBrandName:
                    racketBrandPicker.setMinValue(0);
                    racketBrandPicker.setMaxValue(arrayBrandLength - 1);
                    racketBrandPicker.setDisplayedValues(racketBrands);

                    racketBrandPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            intializeRacketBrandName.setText(racketBrands[i1]);
                            dataWatcher[2] = true;
                        }
                    });

                    break;

                case R.id.initializeRacketGram:
                    racketBrandPicker.setMinValue(0);
                    racketBrandPicker.setMaxValue(arrayWeightLength - 1);
                    racketBrandPicker.setDisplayedValues(racketWeight);

                    racketBrandPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            initializeRacketGram.setText(racketWeight[i1] + "g");
                            dataWatcher[3] = true;
                        }
                    });

                    break;

                case R.id.initializeRacketHeadSize:
                    racketBrandPicker.setMinValue(0);
                    racketBrandPicker.setMaxValue(arrayHeadSizeLength - 1);
                    racketBrandPicker.setDisplayedValues(racketHeadSize);

                    racketBrandPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            initializeRacketHeadSize.setText(racketHeadSize[i1] + "sq. in.");
                            dataWatcher[4] = true;
                        }
                    });
                    break;
            }

        }
    };
}