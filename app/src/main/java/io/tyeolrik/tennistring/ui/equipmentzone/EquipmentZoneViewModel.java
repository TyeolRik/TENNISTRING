package io.tyeolrik.tennistring.ui.equipmentzone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EquipmentZoneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EquipmentZoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is equipmentzone fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}