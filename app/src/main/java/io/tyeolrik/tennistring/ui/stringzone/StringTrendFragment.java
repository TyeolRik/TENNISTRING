package io.tyeolrik.tennistring.ui.stringzone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import io.tyeolrik.tennistring.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StringTrendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StringTrendFragment extends Fragment {

    FirebaseFirestore database;
    TextView average_main_tension_textView, average_cross_tension_textView;
    ImageButton goBackToStringZone;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StringTrendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StringTrendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StringTrendFragment newInstance(String param1, String param2) {
        StringTrendFragment fragment = new StringTrendFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_string_trend, container, false);

        database = FirebaseFirestore.getInstance();

        average_main_tension_textView   =   view.findViewById(R.id.average_main_tension_textView);
        average_cross_tension_textView  =   view.findViewById(R.id.average_cross_tension_textView);
        goBackToStringZone              =   view.findViewById(R.id.goBackToStringZone);

        database.collection("application")
                .document("Aggregate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult() != null) {
                                double averageMainTension   = (double) task.getResult().getData().get("avg_main_tension");
                                double averageCrossTension  = (double) task.getResult().getData().get("avg_cross_tension");
                                average_main_tension_textView.setText(String.format(Locale.KOREA, "%02.1f", averageMainTension));
                                average_cross_tension_textView.setText(String.format(Locale.KOREA, "%02.1f", averageCrossTension));
                            }
                        } else {

                        }
                    }
                });

        goBackToStringZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStackImmediate();
            }
        });

        return view;
    }
}