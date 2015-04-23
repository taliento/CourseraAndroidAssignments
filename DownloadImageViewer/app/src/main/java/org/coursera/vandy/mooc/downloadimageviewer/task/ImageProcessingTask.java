package org.coursera.vandy.mooc.downloadimageviewer.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.coursera.vandy.mooc.downloadimageviewer.callback.TaskCompletionCallback;
import org.coursera.vandy.mooc.downloadimageviewer.util.Utils;

/**
 * Created by User on 4/19/2015.
 */
public class ImageProcessingTask extends AsyncTask {
    public enum TaskType{
        DOWNLOAD,
        FILTER
    };

    private TaskType taskType;
    private TaskCompletionCallback callback;

    public ImageProcessingTask(final TaskCompletionCallback callback, final TaskType taskType) {
        this.callback = callback;
        this.taskType = taskType;
    }

    @Override
    protected Uri doInBackground(Object[] params) {
        Uri url = (Uri) params[0];
        Context downloadImageActivityContext = (Context) params[1];

        switch(taskType) {
            case DOWNLOAD:
                return downloadImageToDevice(url, downloadImageActivityContext);
            case FILTER:
                return filterImage(url, downloadImageActivityContext);
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        callback.handleTaskCompletion((Uri) o);
    }

    private Uri downloadImageToDevice(final Uri url, final Context context) {
        return Utils.downloadImage(context, url);
    }

    private Uri filterImage(final Uri url, final Context context) {
        return Utils.grayScaleFilter(context, url);
    }
}
