package io.tyeolrik.tennistring.ui.stringzone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StringZoneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StringZoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is stringzone fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}