package org.coursera.vandy.mooc.downloadimageviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.coursera.vandy.mooc.downloadimageviewer.R;

public class MainActivity extends LifecycleLoggingActivity {
    public static final String FILE_URL = "FILE_URL";

    private static final String DOWNLOAD_INTENT = "org.coursera.vandy.mooc.downloadimageviewer.DOWNLOAD";
    private static final String FILTER_INTENT = "org.coursera.vandy.mooc.downloadimageviewer.FILTER";

    private static final int DOWNLOAD_IMAGE_REQUEST_CODE = 1;
    private static final int FILTER_IMAGE_REQUEST_CODE = 2;

    private EditText urlField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlField = (EditText)findViewById(R.id.url_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(resultCode) {
            case RESULT_OK:
                processOKResult(requestCode, data);
                break;
            default:
                processFailureResult();
                break;
        }
    }

    public void processImage(View view) {
        hideKeyboard(this, urlField.getWindowToken());
        Intent downloadImageIntent = prepareDownloadIntent(getUrl());
        if(downloadImageIntent != null) {
            startActivityForResult(prepareDownloadIntent(getUrl()), DOWNLOAD_IMAGE_REQUEST_CODE);
        }
    }

    private void processFailureResult() {
        Toast.makeText(this, "Unable to download image from URL provided", Toast.LENGTH_LONG).show();
    }

    private void processOKResult(final int requestCode, final Intent data) {
        String filePathUrl = null;
        switch(requestCode) {
            case DOWNLOAD_IMAGE_REQUEST_CODE:
                filePathUrl = data.getStringExtra(FILE_URL);
                startActivityForResult(prepareFilterIntent(filePathUrl), FILTER_IMAGE_REQUEST_CODE);
                break;
            case FILTER_IMAGE_REQUEST_CODE:
                filePathUrl = data.getStringExtra(FILE_URL);
                startActivity(prepareGalleryOpenIntent(filePathUrl));
                break;
            default:
                break;
        }
    }

    private Intent prepareDownloadIntent(final Uri uri) {
        if(uri != null) {
            Intent downloadImageIntent = new Intent();
            downloadImageIntent.setAction(DOWNLOAD_INTENT);
            downloadImageIntent.setData(uri);
            return downloadImageIntent;
        }
        return null;
    }

    private Intent prepareFilterIntent(final String pathToImagefile) {
        Intent filterImageIntent = new Intent();
        filterImageIntent.setAction(FILTER_INTENT);
        filterImageIntent.setDataAndType(Uri.parse(pathToImagefile), "image/*");
        return filterImageIntent;
    }

    private Intent prepareGalleryOpenIntent(final String pathToImagefile) {
        Intent openGalleryIntent = new Intent(Intent.ACTION_VIEW);
        openGalleryIntent.setDataAndType(Uri.parse("file://"+pathToImagefile), "image/*");
        return openGalleryIntent;
    }


    private void hideKeyboard(Activity activity, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,0);
    }

    protected Uri getUrl() {
        Uri url = Uri.parse(urlField.getText().toString());
        String uri = url.toString();

        if (uri == null || uri.equals("")) {
            url = Uri.parse(getResources().getString(R.string.default_url));
            uri = url.toString();
        }

        if (Patterns.WEB_URL.matcher(uri).matches()) {
            return url;
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
