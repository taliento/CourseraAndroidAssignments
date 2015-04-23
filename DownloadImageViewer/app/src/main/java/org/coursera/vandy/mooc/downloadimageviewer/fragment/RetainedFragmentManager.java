package org.coursera.vandy.mooc.downloadimageviewer.fragment;

import android.app.Activity;
import android.app.FragmentManager;

/**
 * Created by User on 4/20/2015.
 */
public class RetainedFragmentManager {
    private String TAG = this.getClass().getName();

    private final String fragmentName;
    private final FragmentManager fragmentManager;

    private RetainedFragment retainedFragment;

    public RetainedFragmentManager(Activity activity, String fragmentName) {
        this.fragmentName = fragmentName;
        this.fragmentManager = activity.getFragmentManager();
    }

    public boolean isInitialized() {
        retainedFragment = (RetainedFragment) this.fragmentManager.findFragmentByTag(fragmentName);

        if(retainedFragment != null) {
            return false;
        } else {
            retainedFragment = new RetainedFragment();
            this.fragmentManager.beginTransaction().add(retainedFragment, fragmentName).commit();
            return true;
        }
    }

    public void put(String name, Object object) {
        retainedFragment.putData(name, object);
    }

    public <T> T get(String name) {
        return retainedFragment.getData(name);
    }
}
