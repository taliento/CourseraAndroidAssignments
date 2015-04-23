package org.coursera.vandy.mooc.downloadimageviewer.fragment;

import android.app.Fragment;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 4/20/2015.
 */
public class RetainedFragment extends Fragment {
    private Map<String,Object> dataStore = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void putData(String key, Object value) {
        dataStore.put(key, value);
    }

    public <T> T getData(String key) {
        return (T) dataStore.get(key);
    }
}
