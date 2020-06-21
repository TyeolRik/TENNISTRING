package io.tyeolrik.tennistring.ui.mypage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

import io.tyeolrik.tennistring.R;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment extends Fragment {

    private Drawable addButton, checkButton;

    public static final int REQUEST_CODE_ADD_STRING = 100;

    private NumberPicker itemPicker;
    private TextView Durability, Power, Control, Feel, Spin, Stability;

    double durability, power, control, feel, spin, stability;
    int availableCount;

    ImageButton addStringListItem;
    ListView listViewMyStringRecord;
    StringListViewAdapter stringListViewAdapter;

    ArrayList<StringRecordItem> stringRecordItemArrayList;

    FirebaseFirestore firestore;

    boolean isButtonAddButton = true;

    private MyPageViewModel myPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("LifeCycle", "MyPageFragment onCreateView");

        myPageViewModel =
                ViewModelProviders.of(this).get(MyPageViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        firestore = FirebaseFirestore.getInstance();

        // BoilerPlate
        Durability      = root.findViewById(R.id.Durability);
        Power           = root.findViewById(R.id.Power);
        Control         = root.findViewById(R.id.Control);
        Feel            = root.findViewById(R.id.Feel);
        Spin            = root.findViewById(R.id.Spin);
        Stability       = root.findViewById(R.id.Stability);

        addButton   = root.getResources().getDrawable(R.drawable.ic_round_add_24_accentcolor, root.getContext().getTheme());
        checkButton = root.getResources().getDrawable(R.drawable.ic_baseline_check_24, root.getContext().getTheme());

        addStringListItem = root.findViewById(R.id.addStringListItem);
        itemPicker = root.findViewById(R.id.itemPicker);
        listViewMyStringRecord = root.findViewById(R.id.ListViewMyStringRecord);
        stringListViewAdapter = new StringListViewAdapter();

        listViewMyStringRecord.setAdapter(stringListViewAdapter);

        addStringListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isButtonAddButton) {
                    // 이제 추가 모드로 변경
                    isButtonAddButton = false;
                    AddStringRecordFragment nextFragment = new AddStringRecordFragment();
                    nextFragment.setTargetFragment(MyPageFragment.this, REQUEST_CODE_ADD_STRING);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, nextFragment)
                            .addToBackStack("AddStringRecord")
                            .commit();
                } else {
                    // 변경 확정
                    isButtonAddButton = true;
                }
            }
        });

        if (stringListViewAdapter.getCount() == 0) {
            stringListViewAdapter.addItem("날짜", "브랜드", "스트링명", "메인 | 크로스");
        }

        String collectionPath = "/user" + "/" + FirebaseAuth.getInstance().getUid() + "/StringWorks";
        Log.d("CollectionPath", collectionPath);

        firestore.collection(collectionPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.d("Firebase", "SUCCESS!");
                            if (task.getResult() != null) {
                                for(QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("Firebase",document.getId() + " => " + document.getData());
                                    String date = (String) document.getData().get("Date");
                                    String Brand = (String) document.getData().get("Brand");
                                    String Name = (String) document.getData().get("Name");
                                    Long tensionMain = (Long) document.getData().get("Main");
                                    Long tensionCross = (Long) document.getData().get("Cross");
                                    stringListViewAdapter.addItem(date, Brand, Name, String.format(Locale.KOREA, "%02d | %02d", tensionMain, tensionCross));
                                }
                                stringListViewAdapter.notifyDataSetChanged();
                                updateAllPreference(stringListViewAdapter.getStringRecordList());
                            } else {
                                Log.d("Firebase", "NO DATA!");
                            }
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });



        myPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_STRING) {
            if (resultCode == RESULT_OK) {
                Log.d("onActivityResult", "From MyPageFragment, RESULT OK");
                stringListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    String TAG = "updateAllPreference";
    public void updateAllPreference(ArrayList<StringRecordItem> input) {
        availableCount = 0;
        durability      = 0.0;
        power           = 0.0;
        control         = 0.0;
        feel            = 0.0;
        spin            = 0.0;
        stability       = 0.0;
        for(final StringRecordItem item : input) {
            Log.d("Check", "Before Firestore: " + availableCount);
            firestore.collection("string")
                    .document(item.getStringBrand() + "-" + item.getStringName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    availableCount = availableCount + 1;
                                    Log.d("Check", "Before Firestore: " + availableCount);
                                    durability      += (double) document.getData().get("Durability");
                                    power           += (double) document.getData().get("Power");
                                    control         += (double) document.getData().get("Control");
                                    feel            += (double) document.getData().get("Feel");
                                    spin            += (double) document.getData().get("Spin");
                                    stability       += (double) document.getData().get("Tension Stability");
                                    Durability.setText(String.format(Locale.KOREA, "%.0f", (durability / availableCount * 100)));
                                    Power.setText(String.format(Locale.KOREA, "%.0f", (power / availableCount * 100)));
                                    Control.setText(String.format(Locale.KOREA, "%.0f", (control / availableCount * 100)));
                                    Feel.setText(String.format(Locale.KOREA, "%.0f", (feel / availableCount * 100)));
                                    Spin.setText(String.format(Locale.KOREA, "%.0f", (spin / availableCount * 100)));
                                    Stability.setText(String.format(Locale.KOREA, "%.0f", (stability / availableCount * 100)));
                                    Log.d("Check", "After Firestore: " + availableCount);
                                } else {
                                    Log.d(TAG, "No such document: " + item.getStringBrand() + "-" + item.getStringName());
                                }
                            } else {
                                Log.d(TAG, "Get Failed with ",task.getException());
                            }
                        }
                    });
        }
    }
}