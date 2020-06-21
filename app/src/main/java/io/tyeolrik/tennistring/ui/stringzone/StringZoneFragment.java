package io.tyeolrik.tennistring.ui.stringzone;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import io.tyeolrik.tennistring.R;

public class StringZoneFragment extends Fragment {

    private StringZoneViewModel stringZoneViewModel;

    FirebaseFirestore database;

    TextView stringerRecommend_StringName, stringerRecommend_StringBrand, stringerRecommend_StringDiameter, stringerRecommend_mention;
    TextView needDurability, needPower, needControl, needSoftness, needSpin, needTension;
    TextView needSearchButton;

    ListView statisticsListView;
    StringListViewAdapter stringListViewAdapter;

    FrameLayout stringFragmentFrameLayout;
    ImageButton stringInformationBackButton;
    TextView stringInformation_Brand, stringInformation_Gauge, stringInformation_Name, stringInformation_Durability, stringInformation_Power, stringInformation_Control, stringInformation_Feel, stringInformation_Spin, stringInformation_Tension;

    ArrayList<String> whatIsClickedNow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        stringZoneViewModel =
                ViewModelProviders.of(this).get(StringZoneViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stringzone, container, false);
        database = FirebaseFirestore.getInstance();

        stringerRecommend_StringName        = root.findViewById(R.id.stringerRecommend_StringName);
        stringerRecommend_StringBrand       = root.findViewById(R.id.stringerRecommend_StringBrand);
        stringerRecommend_StringDiameter    = root.findViewById(R.id.stringerRecommend_StringDiameter);
        stringerRecommend_mention           = root.findViewById(R.id.stringerRecommend_mention);

        needDurability                      = root.findViewById(R.id.needDurability);
        needPower                           = root.findViewById(R.id.needPower);
        needControl                         = root.findViewById(R.id.needControl);
        needSoftness                        = root.findViewById(R.id.needSoftness);
        needSpin                            = root.findViewById(R.id.needSpin);
        needTension                         = root.findViewById(R.id.needTension);

        needSearchButton                    = root.findViewById(R.id.needSearchButton);

        statisticsListView                  = root.findViewById(R.id.statisticsListView);
        stringListViewAdapter               = new StringListViewAdapter();
        statisticsListView.setAdapter(stringListViewAdapter);
        stringListViewAdapter.addTitle();

        stringFragmentFrameLayout           = root.findViewById(R.id.stringFragmentFrameLayout);
        stringInformationBackButton         = root.findViewById(R.id.stringInformationBackButton);

        stringInformation_Brand             = root.findViewById(R.id.stringInformation_Brand);
        stringInformation_Gauge             = root.findViewById(R.id.stringInformation_Gauge);
        stringInformation_Name             = root.findViewById(R.id.stringInformation_Name);
        stringInformation_Durability             = root.findViewById(R.id.stringInformation_Durability);
        stringInformation_Power             = root.findViewById(R.id.stringInformation_Power);
        stringInformation_Control             = root.findViewById(R.id.stringInformation_Control);
        stringInformation_Feel             = root.findViewById(R.id.stringInformation_Feel);
        stringInformation_Spin             = root.findViewById(R.id.stringInformation_Spin);
        stringInformation_Tension             = root.findViewById(R.id.stringInformation_Tension);

        whatIsClickedNow = new ArrayList<>();

        database.collection("application")
                .document("string_recommend")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                stringerRecommend_StringName.setText(document.getData().get("Name").toString());
                                stringerRecommend_StringBrand.setText(document.getData().get("Brand").toString());
                                stringerRecommend_StringDiameter.setText(String.format(Locale.KOREA, "%.2fmm", (Double) document.getData().get("Diameter")));
                                stringerRecommend_mention.setText(document.getData().get("mention").toString());
                                Log.d("Recommend", "SUCCESS: Name: " + document.getData().get("Name").toString());
                                Log.d("Recommend", "SUCCESS: Brand: " + document.getData().get("Brand").toString());
                                Log.d("Recommend", "SUCCESS: Diameter: " + String.format(Locale.KOREA, "%.2fmm", (Double) document.getData().get("Diameter")));
                                Log.d("Recommend", "SUCCESS: mention: " + document.getData().get("mention").toString());
                            } else {
                                Log.d("Recommend", "There is no Recommend! ERROR");
                            }
                        } else {
                            Log.d("Recommend", "ERROR with: ", task.getException());
                        }
                    }
                });

        needDurability.setOnClickListener(onClickNeedThings);
        needPower.setOnClickListener(onClickNeedThings);
        needControl.setOnClickListener(onClickNeedThings);
        needSoftness.setOnClickListener(onClickNeedThings);
        needSpin.setOnClickListener(onClickNeedThings);
        needTension.setOnClickListener(onClickNeedThings);

        needSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference collectionReference = database.collection("string");
                double standard = 0.3;
                switch (whatIsClickedNow.size()) {
                    case 0:
                        break;
                    case 1:
                        collectionReference.whereGreaterThan(whatIsClickedNow.get(0), standard).orderBy(whatIsClickedNow.get(0), Query.Direction.DESCENDING).limit(15).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        stringListViewAdapter.addTitle();
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            Log.d("SearchButton", "" + documentSnapshot.getData());
                                            HashMap<String, Object> hashMap = (documentSnapshot.getData() instanceof HashMap) ? (HashMap) documentSnapshot.getData() : new HashMap<String, Object>(documentSnapshot.getData());
                                            stringListViewAdapter.addHashMapItem(hashMap);
                                        }
                                        stringListViewAdapter.notifyDataSetChanged();
                                    }
                                });
                        break;
                    default:
                        break;
                }
            }
        });

        statisticsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("onItemClick", "Position: " + i);
                HashMap<String, Object> item = (HashMap<String, Object>) stringListViewAdapter.getItem(i);
                String brand = item.get("Brand").toString();
                String name = item.get("Name").toString();
                String brand_name = brand + "-" + name;

                database.collection("string")
                        .document(brand_name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    if(task.getResult() != null) {

                                        double durability      = (double) task.getResult().getData().get("Durability");
                                        double power           = (double) task.getResult().getData().get("Power");
                                        double control         = (double) task.getResult().getData().get("Control");
                                        double feel            = (double) task.getResult().getData().get("Feel");
                                        double spin            = (double) task.getResult().getData().get("Spin");
                                        double stability       = (double) task.getResult().getData().get("Tension Stability");

                                        stringInformation_Brand.setText(task.getResult().getData().get("Brand").toString());
                                        stringInformation_Gauge.setText(String.format(Locale.KOREA, "%.2fmm", (Double) task.getResult().getData().get("Diameter")));
                                        stringInformation_Name.setText(task.getResult().getData().get("Name").toString());
                                        stringInformation_Durability.setText(String.format(Locale.KOREA, "%.0f", (durability * 100)));
                                        stringInformation_Power.setText(String.format(Locale.KOREA, "%.0f", (power * 100)));
                                        stringInformation_Control.setText(String.format(Locale.KOREA, "%.0f", (control * 100)));
                                        stringInformation_Feel.setText(String.format(Locale.KOREA, "%.0f", (feel * 100)));
                                        stringInformation_Spin.setText(String.format(Locale.KOREA, "%.0f", (spin * 100)));
                                        stringInformation_Tension.setText(String.format(Locale.KOREA, "%.0f", (stability * 100)));

                                        statisticsListView.setVisibility(View.INVISIBLE);
                                        statisticsListView.setClickable(false);
                                        stringFragmentFrameLayout.setVisibility(View.VISIBLE);
                                        stringFragmentFrameLayout.setClickable(true);
                                    } else {
                                        Log.d("Firebase", "THERE IS No Data FATAL ERROR!!!!");
                                    }
                                } else {
                                    Log.d("Firebase", "FATAL ERROR with", task.getException());
                                }
                            }
                        });
            }
        });

        stringInformationBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statisticsListView.setVisibility(View.VISIBLE);
                statisticsListView.setClickable(true);
                stringFragmentFrameLayout.setVisibility(View.INVISIBLE);
                stringFragmentFrameLayout.setClickable(false);
            }
        });

        stringZoneViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    View.OnClickListener onClickNeedThings = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String point = "";
            switch (view.getId()) {
                case R.id.needDurability:
                    point = "Durability";
                    break;
                case R.id.needPower:
                    point = "Power";
                    break;
                case R.id.needControl:
                    point = "Control";
                    break;
                case R.id.needSoftness:
                    point = "Feel";
                    break;
                case R.id.needSpin:
                    point = "Spin";
                    break;
                case R.id.needTension:
                    point = "Tension Stability";
                    break;
            }
            if (whatIsClickedNow.contains(point)) {
                view.setBackgroundColor(Color.TRANSPARENT);
                whatIsClickedNow.remove(point);
            } else {
                if (whatIsClickedNow.size() >= 1) {
                    // Toast.makeText(getContext(), "최대 1개까지 선택 가능합니다!\n하나를 선택해제해주세요!", Toast.LENGTH_SHORT).show();
                    whatIsClickedNow.remove(0);
                    whatIsClickedNow.add(point);

                    needDurability.setBackgroundColor(Color.TRANSPARENT);
                    needPower.setBackgroundColor(Color.TRANSPARENT);
                    needControl.setBackgroundColor(Color.TRANSPARENT);
                    needSoftness.setBackgroundColor(Color.TRANSPARENT);
                    needSpin.setBackgroundColor(Color.TRANSPARENT);
                    needTension.setBackgroundColor(Color.TRANSPARENT);

                    view.setBackgroundColor(Color.GRAY);
                } else {
                    view.setBackgroundColor(Color.GRAY);
                    whatIsClickedNow.add(point);
                }
            }
        }
    };
}