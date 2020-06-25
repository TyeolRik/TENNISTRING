package io.tyeolrik.tennistring.ui.userInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import io.tyeolrik.tennistring.R;

public class UserInfoFragment extends Fragment {

    FrameLayout userInfoFrameLayout;

    private UserInfoViewModel userInfoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userInfoViewModel =
                ViewModelProviders.of(this).get(UserInfoViewModel.class);
        View view = inflater.inflate(R.layout.fragment_userinfo, container, false);

        userInfoFrameLayout     = view.findViewById(R.id.userInfoFrameLayout);

        StringerFragment stringerFragment = new StringerFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.userInfoFrameLayout, stringerFragment)
                .addToBackStack(null)
                .commit();

        
        return view;
    }
}