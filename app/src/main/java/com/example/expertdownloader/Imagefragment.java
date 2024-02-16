/*
package com.example.expertdownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Imagefragment extends Fragment {
    String link;
    Button button;
    ImageView imageView;
    EditText edittextimg;

    public Imagefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_imagefragment, container, false);
        button = view.findViewById(R.id.imgbutton);
        imageView = view.findViewById(R.id.imgview);
        edittextimg = view.findViewById(R.id.imgtext);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link = edittextimg.getText().toString();
                new Async().execute(link);

               */
/* Handler handler = new Handler() {
                    @Override
                    public void publish(LogRecord record) {

                    }

                    @Override
                    public void flush() {

                    }

                    @Override
                    public void close() throws SecurityException {

                    }
                };*//*

                //Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }


    public class Async extends AsyncTask<String, Void, Bitmap> {
        HttpURLConnection httpurlConnection;

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                httpurlConnection = (HttpURLConnection) url.openConnection();
                //  String contentType = httpurlConnection.getContentType();
//                if (contentType != null) {
//                    switch (contentType) {
//                        case "image/jpeg";
//                    }
//
//                }
                InputStream inputStream = new BufferedInputStream(httpurlConnection.getInputStream());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                httpurlConnection.disconnect();
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                saveimage(bitmap);
                imageView.setImageBitmap(bitmap);

            }else{
                Toast.makeText(getContext(), "something wrong", Toast.LENGTH_LONG ).show();
            }

        }

        private void saveimage(Bitmap bitmap) {

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(path.toURI());
            Log.d("TAG", "onPostExecute: " + file.getAbsolutePath());
            String imagename = filenameset(link);
            file = new File(file, imagename);

            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_LONG ).show();
                throw new RuntimeException(e);
            }
        }
        private String filenameset(String fname) {
            try {
                URL parseurl = new URL(fname);
                String path = parseurl.getPath();
                String decodefilename= URLDecoder.decode(path.substring(path.lastIndexOf('/')+1), StandardCharsets.UTF_8.name());
                return decodefilename;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}*/
