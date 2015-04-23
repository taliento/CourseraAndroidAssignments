package org.coursera.vandy.mooc.downloadimageviewer.callback;

import android.net.Uri;

/**
 * Created by User on 4/19/2015.
 */
public interface TaskCompletionCallback {
    void handleTaskCompletion(Uri fileUri);
}
