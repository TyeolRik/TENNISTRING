package io.tyeolrik.tennistring.ui.mypage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import io.tyeolrik.tennistring.R;

public class MyPageFragment extends Fragment {

    private Drawable addButton, checkButton;

    private NumberPicker itemPicker;
    ImageButton addStringListItem;
    ListView listViewMyStringRecord;
    StringListViewAdapter stringListViewAdapter;

    boolean isButtonAddButton = true;

    private MyPageViewModel myPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel =
                ViewModelProviders.of(this).get(MyPageViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_mypage, container, false);
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
                    addStringListItem.setImageDrawable(checkButton);
                    isButtonAddButton = false;
                    AddStringRecordFragment nextFragment = new AddStringRecordFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, nextFragment)
                            .addToBackStack("AddStringRecord")
                            .commit();
                } else {
                    // 변경 확정
                    addStringListItem.setImageDrawable(addButton);
                    isButtonAddButton = true;
                }
            }
        });

        if (stringListViewAdapter.getCount() == 0) {
            stringListViewAdapter.addItem("날짜", "브랜드", "스트링명", "메인 | 크로스");
        }

        myPageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}