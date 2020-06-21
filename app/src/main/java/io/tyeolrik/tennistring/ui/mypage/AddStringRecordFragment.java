package io.tyeolrik.tennistring.ui.mypage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.tyeolrik.tennistring.Database.StringDataBase;
import io.tyeolrik.tennistring.R;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddStringRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddStringRecordFragment extends Fragment {

    StringDataBase stringDataBase;

    View view;

    TextView addStringYear, addStringMonth, addStringDate, addStringDataBrand, addStringDataStringName, addStringDataTensionMain, addStringDataTensionCross;
    NumberPicker itemPicker;
    ImageButton confirmStringRecord;

    FirebaseFirestore database;

    Task<QuerySnapshot> allStringData;
    HashMap<String, String> stringDatas;

    private String selectedBrand = "";
    private String selectedString = "";

    private int thisYear;
    private int thisMonth;
    private int thisDate;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddStringRecordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddStringRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddStringRecordFragment newInstance(String param1, String param2) {
        AddStringRecordFragment fragment = new AddStringRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        thisYear    = Calendar.getInstance().get(Calendar.YEAR);
        thisMonth   = Calendar.getInstance().get(Calendar.MONTH) + 1;
        thisDate    = Calendar.getInstance().get(Calendar.DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("LifeCycle", "AddStringRecordFragment onCreateView");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_string_record, container, false);
        database = FirebaseFirestore.getInstance();
        stringDataBase = StringDataBase.getInstance();
        addStringYear               = view.findViewById(R.id.addStringYear);
        addStringMonth              = view.findViewById(R.id.addStringMonth);
        addStringDate               = view.findViewById(R.id.addStringDate);
        addStringDataBrand          = view.findViewById(R.id.addStringDataBrand);
        addStringDataStringName     = view.findViewById(R.id.addStringDataStringName);
        addStringDataTensionMain    = view.findViewById(R.id.addStringDataTensionMain);
        addStringDataTensionCross   = view.findViewById(R.id.addStringDataTensionCross);
        itemPicker                  = view.findViewById(R.id.itemPicker);
        confirmStringRecord         = view.findViewById(R.id.confirmStringRecord);

        addStringYear.setText(String.format(Locale.KOREA, "%04d년", thisYear));
        addStringMonth.setText(String.format(Locale.KOREA, "%02d월", thisMonth));
        addStringDate.setText(String.format(Locale.KOREA, "%02d일", thisDate));

        addStringYear.setOnClickListener(wakeupNumberPicker);
        addStringMonth.setOnClickListener(wakeupNumberPicker);
        addStringDate.setOnClickListener(wakeupNumberPicker);
        addStringDataBrand.setOnClickListener(wakeupNumberPicker);
        addStringDataStringName.setOnClickListener(wakeupNumberPicker);
        addStringDataTensionMain.setOnClickListener(wakeupNumberPicker);
        addStringDataTensionCross.setOnClickListener(wakeupNumberPicker);

        stringDatas = new HashMap<>();
        allStringData = database.collection("user")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("StringWorks")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                String documentID = document.getId();
                                String date = documentID.substring(0, 10);
                                String count = documentID.substring(11);
                                Log.d("Firebase", "Date: " + date);
                                Log.d("Firebase", "Count: " + count);
                                stringDatas.put(date, count);
                            }
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });

        confirmStringRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedBrand.equals("")) {
                    Log.d("UnSelected", "selectedBrand");
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            addStringDataBrand.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            addStringDataBrand.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    addStringDataBrand.startAnimation(_shaking);
                }
                if(selectedString.equals("")) {
                    Log.d("UnSelected", "selectedString");
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            addStringDataStringName.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            addStringDataStringName.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    addStringDataStringName.startAnimation(_shaking);
                }
                if(Integer.parseInt(addStringDataTensionMain.getText().toString()) == 0)  {
                    Log.d("UnSelected", "addStringDataTensionMain");
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            addStringDataTensionMain.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            addStringDataTensionMain.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    addStringDataTensionMain.startAnimation(_shaking);
                }
                if(Integer.parseInt(addStringDataTensionCross.getText().toString()) == 0) {
                    Log.d("UnSelected", "addStringDataTensionCross");
                    Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                    _shaking.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            addStringDataTensionCross.setTextColor(Color.RED);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            addStringDataTensionCross.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    addStringDataTensionCross.startAnimation(_shaking);
                }

                if(selectedBrand != "" && selectedString != "" && Integer.parseInt(addStringDataTensionMain.getText().toString()) != 0  && Integer.parseInt(addStringDataTensionCross.getText().toString()) != 0) {
                    Map<String, Object> stringRecord = new HashMap<>();
                    String date = addStringYear.getText().toString().substring(0, 4) + "-"
                            + addStringMonth.getText().toString().substring(0, 2) + "-"
                            + addStringDate.getText().toString().substring(0, 2);
                    stringRecord.put("Date", date);
                    stringRecord.put("Brand", addStringDataBrand.getText().toString());
                    stringRecord.put("Name", addStringDataStringName.getText().toString());
                    stringRecord.put("Main", Integer.parseInt(addStringDataTensionMain.getText().toString()));
                    stringRecord.put("Cross", Integer.parseInt(addStringDataTensionCross.getText().toString()));

                    int next = 1;
                    if(stringDatas.get(date) != null) {
                        next = Integer.parseInt(stringDatas.get(date)) + 1;
                    }

                    Log.d("Next", String.valueOf(next));

                    database.collection("user")
                            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                            .collection("StringWorks")
                            .document(date + "-" + String.valueOf(next))
                            .set(stringRecord)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("Firebase", "SUCCESS! Return to MyPage!");
                                        getParentFragmentManager().beginTransaction()
                                                .replace(R.id.nav_host_fragment, new MyPageFragment())
                                                .addToBackStack("AddStringRecord")
                                                .commit();
                                    } else {
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    View.OnClickListener wakeupNumberPicker = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            itemPicker.setDisplayedValues(null);

            if (itemPicker.getVisibility() == View.INVISIBLE || itemPicker.getVisibility() == View.GONE) {
                itemPicker.setVisibility(View.VISIBLE);
            }

            switch (view.getId()) {
                case R.id.addStringYear:
                    itemPicker.setMinValue(1950);
                    itemPicker.setMaxValue(thisYear);
                    itemPicker.setValue(thisYear);
                    itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            addStringYear.setText(String.format(Locale.KOREA, "%04d년", i1));
                        }
                    });
                    break;

                case R.id.addStringMonth:
                    itemPicker.setMinValue(Calendar.JANUARY + 1);
                    itemPicker.setMaxValue(Calendar.DECEMBER + 1);
                    itemPicker.setValue(thisMonth);
                    itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            addStringMonth.setText(String.format(Locale.KOREA, "%02d월", i1));
                        }
                    });
                    break;

                case R.id.addStringDate:
                    LocalDate that = LocalDate.of(Integer.parseInt(addStringYear.getText().toString().substring(0, 4)), Integer.parseInt(addStringMonth.getText().toString().substring(0, 2)), 11);
                    LocalDate lastDay = that.with(TemporalAdjusters.lastDayOfMonth());
                    int lastDayOfMonth = Integer.parseInt(lastDay.format(DateTimeFormatter.ofPattern("dd")));
                    itemPicker.setMinValue(1);
                    itemPicker.setMaxValue(lastDayOfMonth);
                    itemPicker.setValue(thisDate);
                    itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            addStringDate.setText(String.format(Locale.KOREA, "%02d일", i1));
                        }
                    });
                    break;

                case R.id.addStringDataBrand:
                    itemPicker.setMinValue(0);
                    itemPicker.setMaxValue(StringDataBase.getBrands().size() - 1);
                    itemPicker.setDisplayedValues(StringDataBase.getBrands().toArray(new String[0]));
                    itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            addStringDataBrand.setText(StringDataBase.getBrands().get(i1));
                            selectedBrand = StringDataBase.getBrands().get(i1);
                        }
                    });
                    break;

                case R.id.addStringDataStringName:
                    if (selectedBrand.equals("")) { // 아무것도 선택 안됨.
                        itemPicker.setVisibility(View.INVISIBLE);
                        Animation _shaking = AnimationUtils.loadAnimation(view.getContext(), R.anim.shaking);
                        _shaking.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                addStringDataBrand.setTextColor(Color.RED);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                addStringDataBrand.setTextColor(Color.BLACK);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        addStringDataBrand.startAnimation(_shaking);
                    } else {
                        itemPicker.setMinValue(0);
                        itemPicker.setMaxValue(StringDataBase.getNamesInBrands(selectedBrand).size() - 1);
                        itemPicker.setDisplayedValues(StringDataBase.getNamesInBrands(selectedBrand).toArray(new String[0]));
                        if(StringDataBase.getNamesInBrands(selectedBrand).size() == 1) {
                            addStringDataStringName.setText(StringDataBase.getNamesInBrands(selectedBrand).get(0));
                            selectedString = StringDataBase.getNamesInBrands(selectedBrand).get(0);
                        }
                        itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                                addStringDataStringName.setText(StringDataBase.getNamesInBrands(selectedBrand).get(i1));
                                selectedString = StringDataBase.getNamesInBrands(selectedBrand).get(0);
                            }
                        });
                    }
                    break;

                case R.id.addStringDataTensionMain:
                    itemPicker.setMinValue(20);
                    itemPicker.setMaxValue(75);
                    itemPicker.setValue(50);
                    itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            addStringDataTensionMain.setText(String.format(Locale.KOREA, "%02d", i1));
                        }
                    });
                    break;

                case R.id.addStringDataTensionCross:
                    itemPicker.setMinValue(20);
                    itemPicker.setMaxValue(75);
                    itemPicker.setValue(50);
                    itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                            addStringDataTensionCross.setText(String.format(Locale.KOREA, "%02d", i1));
                        }
                    });
                    break;
            }

        }
    };
}