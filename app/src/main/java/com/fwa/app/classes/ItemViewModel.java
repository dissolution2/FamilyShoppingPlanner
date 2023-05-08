package com.fwa.app.classes;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<String> selectedItem = new MutableLiveData<String>();

    public void setData(String item){
        selectedItem.setValue(item);
        Log.d("ItemViewModel","value set: " + item);
    }

    public LiveData<String> getSelectedItem(){
        return selectedItem;
    }
}
