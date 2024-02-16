package com.example.expertdownloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView; 
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Testfrag extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "download_channel";

    private String link;
    private Button button;
    private ImageView imageView;
    private EditText edittextimg;
    private SeekBar seekBar;
    private int progress = 0;

    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testfrag, container, false);
        button = view.findViewById(R.id.imgbutton11);
        imageView = view.findViewById(R.id.imgview11);
        edittextimg = view.findViewById(R.id.imgtext11);
        seekBar = view.findViewById(R.id.seekbar4);
        handler = new Handler(Looper.getMainLooper());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link = edittextimg.getText().toString();
                String downloadPath = "";

                // Check if the app has the required permission
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.USE_FULL_SCREEN_INTENT) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, perform the operation that requires this permission
                    // For example, show a notification
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(new Threadclass1(link, downloadPath));
                } else {
                    // Permission is not granted, request the permission from the user
                    // You can use ActivityCompat.requestPermissions() to request the permission
                    // Handle the result in onRequestPermissionsResult() method
                    requestPermission();
                }
            }
        });
        return view;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.USE_FULL_SCREEN_INTENT}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, perform the operation
            link = edittextimg.getText().toString();
            String downloadPath = "";

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(new Threadclass1(link, downloadPath));
        } else {
            // Permission denied, handle accordingly (show a message, etc.)
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private class Threadclass1 implements Runnable {

        private String linkUrl;
        private String downloadPath;

        public Threadclass1(String linkUrl, String downloadPath) {
            this.linkUrl = linkUrl;
            this.downloadPath = downloadPath;
        }

        @Override
        public void run() {
            URL url;
            HttpURLConnection httpurlConnection;
            httpurlConnection = null;
            try {
                url = new URL(linkUrl);
                httpurlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(httpurlConnection.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Save the image and get the download path
                downloadPath = saveimage(bitmap);

                // Notify download completion
                showNotification(downloadPath);

                // Reset the progress
                updateSeekBar(0);

            } catch (MalformedURLException e) {
                handleError(e);

            } catch (IOException e) {
                handleError(e);

            } finally {
                if (httpurlConnection != null) {
                    httpurlConnection.disconnect();
                }
            }
        }

        private void handleError(Exception e) {
            // Handle errors, display a toast, etc.
            updateSeekBar(0);
            showToast("Error: " + e.getMessage());
        }
    }

    private String saveimage(Bitmap bitmap) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(path.toURI());
        String imagename = filenameset(link);
        file = new File(file, imagename);

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            showToast("Error saving image");
            return "";
        }
    }

    private String filenameset(String fname) {
        try {
            URL parseurl = new URL(fname);
            String path = parseurl.getPath();
            String decodefilename = URLDecoder.decode(path.substring(path.lastIndexOf('/') + 1), StandardCharsets.UTF_8.name());
            return decodefilename;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showNotification(String downloadPath) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Download Complete")
                .setContentText("Image downloaded to: " + downloadPath)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            createNotificationChannel();
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for download notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showToast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSeekBar(final int progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(progress);
            }
        });
    }
}
