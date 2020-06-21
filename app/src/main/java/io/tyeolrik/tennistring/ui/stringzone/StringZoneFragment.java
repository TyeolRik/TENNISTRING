package io.tyeolrik.tennistring.ui.stringzone;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                    point = "Softness";
                    break;
                case R.id.needSpin:
                    point = "Spin";
                    break;
                case R.id.needTension:
                    point = "Tension";
                    break;
            }
            if (whatIsClickedNow.contains(point)) {
                view.setBackgroundColor(Color.TRANSPARENT);
                whatIsClickedNow.remove(point);
            } else {
                if (whatIsClickedNow.size() >= 1) {
                    Toast.makeText(getContext(), "최대 1개까지 선택 가능합니다!\n하나를 선택해제해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    view.setBackgroundColor(Color.GRAY);
                    whatIsClickedNow.add(point);
                }
            }
        }
    };
}