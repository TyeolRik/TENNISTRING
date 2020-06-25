package io.tyeolrik.tennistring.ui.userInfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

import io.tyeolrik.tennistring.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StringerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StringerFragment extends Fragment {

    FirebaseFirestore database;
    DocumentSnapshot inquiredPerson;

    Spinner searchingConditionForStringer;
    EditText searchingTextForStringer;
    Button searchingButtonForStringer, unsubscribeButtonInStringerFragment;
    ListView stringerSearchingListView;

    TextView inquiryName, inquiryCategory, inquiryTeam, inquiryRacketBrand, inquiryRacketName;

    String condition;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StringerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StringerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StringerFragment newInstance(String param1, String param2) {
        StringerFragment fragment = new StringerFragment();
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
        View view = inflater.inflate(R.layout.fragment_stringer, container, false);
        database = FirebaseFirestore.getInstance();

        // BoilerPlate
        searchingConditionForStringer   = view.findViewById(R.id.searchingConditionForStringer);
        searchingTextForStringer        = view.findViewById(R.id.searchingTextForStringer);
        searchingButtonForStringer      = view.findViewById(R.id.searchingButtonForStringer);

        inquiryName                     = view.findViewById(R.id.inquiryName);
        inquiryCategory                 = view.findViewById(R.id.inquiryCategory);
        inquiryTeam                     = view.findViewById(R.id.inquiryTeam);
        inquiryRacketBrand              = view.findViewById(R.id.inquiryRacketBrand);
        inquiryRacketName               = view.findViewById(R.id.inquiryRacketName);



        searchingConditionForStringer.setSelection(0);
        searchingConditionForStringer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                condition = adapterView.getItemAtPosition(position).toString();
                Log.d("Spinner", condition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchingButtonForStringer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchingCondition = "";
                String searchingText = searchingTextForStringer.getText().toString();
                switch (condition) {
                    case "이름":
                        searchingCondition = "UserName";
                        break;
                    case "소속":
                        searchingCondition = "UserTeam";
                        break;
                    case "라켓":
                        searchingCondition = "RacketName";
                        break;
                }
                if (searchingCondition != "") {

                    final String finalSearchingCondition = searchingCondition;
                    final String finalSearchingText = searchingText;

                    Task<Void> getAllUserInformation = database.runTransaction(new Transaction.Function<Void>() {
                        @Nullable
                        @Override
                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                            Log.d("Firebase", "Start getAllUserInformation Transaction");
                            Log.d("LOG", "finalSearchingCondition: " + finalSearchingCondition);
                            Log.d("LOG", "finalSearchingText: " + finalSearchingText);
                            final List<DocumentSnapshot> documentSnapshotList = database.collection("user").whereEqualTo(finalSearchingCondition, finalSearchingText).get().getResult().getDocuments();
                            final String[] inquiredPersonUID = {""};
                            if (documentSnapshotList.size() == 1) {
                                Log.d("LOG", "documentSnapshotList.size(): " + documentSnapshotList.size());
                                inquiredPerson = documentSnapshotList.get(0);
                                inquiredPersonUID[0] = inquiredPerson.getId();
                            } else if (documentSnapshotList.size() > 1) {
                                Log.d("LOG", "documentSnapshotList.size(): " + documentSnapshotList.size());
                                CharSequence[] items = new String[documentSnapshotList.size()];;
                                for (int i = 0; i < items.length; i++) {
                                    items[i] = documentSnapshotList.get(i).getData().get("UserName") + " - " + documentSnapshotList.get(i).getData().get("UserTeam");
                                }
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                alertDialogBuilder.setTitle("선택해주세요");
                                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        inquiredPerson = documentSnapshotList.get(i);
                                        inquiredPersonUID[0] = inquiredPerson.getId();
                                    }
                                });
                            } else {
                                Log.d("LOG", "documentSnapshotList.size(): " + documentSnapshotList.size());
                                Toast.makeText(getContext(), "데이터가 없습니다!\n조건을 확인해주세요!", Toast.LENGTH_LONG).show();
                            }

                            inquiryName.setText(inquiredPerson.get("UserName").toString());
                            inquiryCategory.setText(inquiredPerson.get("UserCategory").toString());
                            inquiryTeam.setText(inquiredPerson.get("UserTeam").toString());
                            inquiryRacketBrand.setText(inquiredPerson.get("RacketBrand").toString());
                            inquiryRacketName.setText(inquiredPerson.get("RacketName").toString());

                            inquiryName.setVisibility(View.VISIBLE);
                            inquiryCategory.setVisibility(View.VISIBLE);
                            inquiryTeam.setVisibility(View.VISIBLE);
                            inquiryRacketBrand.setVisibility(View.VISIBLE);
                            inquiryRacketName.setVisibility(View.VISIBLE);

                            database.collection("user").document(inquiredPersonUID[0]).collection("StringWorks").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                if (task.getResult() != null) {
                                                    for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                                        Log.d("UserData", snapshot.getId() + "->" + snapshot.getData());
                                                    }
                                                } else {
                                                    Log.d("UserData", "Something Strange!");
                                                }
                                            } else {
                                                Log.d("UserData", "FAILED with " + task.getException());
                                            }
                                        }
                                    });

                            return null;
                        }
                    });
                    /*
                    database.collection("user").whereEqualTo(searchingCondition, searchingText).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()) {
                                        final List<DocumentSnapshot> document = task.getResult().getDocuments();
                                        if (document.size() == 1) {
                                            inquiredPerson = document.get(0);
                                        } else if (document.size() > 1) {
                                            CharSequence[] items = new String[document.size()];;
                                            for (int i = 0; i < items.length; i++) {
                                                items[i] = document.get(i).getData().get("UserName") + " - " + document.get(i).getData().get("UserTeam");
                                            }
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                                            alertDialogBuilder.setTitle("선택해주세요");
                                            alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    inquiredPerson = document.get(i);
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getContext(), "데이터가 없습니다!\n조건을 확인해주세요!", Toast.LENGTH_LONG).show();
                                        }

                                        inquiryName.setText(inquiredPerson.get("UserName").toString());
                                        inquiryCategory.setText(inquiredPerson.get("UserCategory").toString());
                                        inquiryTeam.setText(inquiredPerson.get("UserTeam").toString());
                                        inquiryRacketBrand.setText(inquiredPerson.get("RacketBrand").toString());
                                        inquiryRacketName.setText(inquiredPerson.get("RacketName").toString());

                                    } else {
                                        Log.d("Firebase", "FAILED!");
                                    }
                                }
                            });

                     */
                } else {
                    Toast.makeText(getContext(), "조건을 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}