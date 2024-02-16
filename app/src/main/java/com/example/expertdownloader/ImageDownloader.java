/*
package com.example.expertdownloader;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class ImageDownloader implements Runnable {
    private String link;
    private Testfrag testFrag;

    public ImageDownloader(String link, Testfrag testFrag) {
        this.link = link;
        this.testFrag = testFrag;
    }

    @Override
    public void run() {
        HttpURLConnection httpurlConnection = null;

        try {
            URL url = new URL(link);
            httpurlConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = new BufferedInputStream(httpurlConnection.getInputStream());
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap != null) {
                saveImage(bitmap);
                testFrag.updateImageView(bitmap);
            } else {
                showToast("Something went wrong");
            }
        } catch (Exception e) {
            showToast("Error: " + e.getMessage());
        } finally {
            if (httpurlConnection != null) {
                httpurlConnection.disconnect();
            }
        }
    }

    private void saveImage(Bitmap bitmap) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path.toURI());
        String imageName = filenameSet(link);
        file = new File(file, imageName);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            showToast("Error saving image");
        }
    }

    private String filenameSet(String fname) {
        try {
            URL parseurl = new URL(fname);
            String path = parseurl.getPath();
            return URLDecoder.decode(path.substring(path.lastIndexOf('/') + 1), StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showToast(final String message) {
        testFrag.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(testFrag.getContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
*/
