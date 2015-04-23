package org.coursera.vandy.mooc.downloadimageviewer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.coursera.vandy.mooc.downloadimageviewer.callback.TaskCompletionCallback;
import org.coursera.vandy.mooc.downloadimageviewer.fragment.RetainedFragmentManager;
import org.coursera.vandy.mooc.downloadimageviewer.task.ImageProcessingTask;


public class ImageDownloadActivity extends LifecycleLoggingActivity implements TaskCompletionCallback {
    private final String TAG = this.getClass().getName();
    private final String FRAGMENT_TAG = getClass().getSimpleName();
    private final String URI = "URI";
    private final String TASK = "TASK";
    private final String IMAGE_PATH = "IMAGE_PATH";

    private Uri imageDownloadUri;
    private ImageProcessingTask imageProcessingTask;
    private RetainedFragmentManager retainedFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retainedFragmentManager = new RetainedFragmentManager(this, FRAGMENT_TAG);

        if(retainedFragmentManager.isInitialized())  {
            Log.i(TAG, "Retained Fragment not initialized - Initializing");
            retainedFragmentManager.put(URI, this.getIntent().getData());
            imageDownloadUri = this.getIntent().getData();
        } else {
            Uri filePath = retainedFragmentManager.get(IMAGE_PATH);
            if(filePath != null) {
                Log.i(TAG, "Image already downloaded, returning to main with uri "+filePath.toString());
                returnToActivity(filePath);
            } else {
                Log.i(TAG, "Image download in progress, waiting for completion");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ImageProcessingTask task = retainedFragmentManager.get(TASK);

        if(task == null) {
            Log.d(TAG, "Task not present, doing initialization");
            imageProcessingTask = new ImageProcessingTask(this, ImageProcessingTask.TaskType.DOWNLOAD);
            retainedFragmentManager.put(TASK, imageProcessingTask);

            Object[] params = new Object[2];
            params[0] = (Object) imageDownloadUri;
            params[1] = (Object) this;

            imageProcessingTask.execute(params);
        } else {
            Log.d(TAG, "Task already present, skipping initialization");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void handleTaskCompletion(Uri fileUri) {
        retainedFragmentManager.put(IMAGE_PATH, fileUri);
        returnToActivity(fileUri);
    }

    public void returnToActivity(Uri fileUri) {
        if(fileUri != null) {
            final Intent resultIntent = new Intent();
            resultIntent.putExtra(MainActivity.FILE_URL, fileUri.toString());
            setResult(RESULT_OK, resultIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
