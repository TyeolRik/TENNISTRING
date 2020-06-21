package io.tyeolrik.tennistring.ui.equipmentzone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EquipmentZoneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EquipmentZoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("장비 추천 관련 Page가 올라올 예정\n개발중 @_@");
    }

    public LiveData<String> getText() {
        return mText;
    }
}