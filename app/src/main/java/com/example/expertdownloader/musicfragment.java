package com.example.expertdownloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class musicfragment extends Fragment {
    Button b1, play, pause, forward, backward;
    EditText editText;
    String songlink;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;

    public musicfragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musicfragment, container, false);
        b1 = view.findViewById(R.id.button1);
        play = view.findViewById(R.id.play);
        pause = view.findViewById(R.id.pause);
        forward = view.findViewById(R.id.forward);
        backward = view.findViewById(R.id.backward);
        editText = view.findViewById(R.id.editTextSongLink);
        seekBar = view.findViewById(R.id.seekBar);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songlink = editText.getText().toString();
                new DownloadMusicTask().execute(songlink);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    updateSeekBar();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int duration = mediaPlayer.getDuration();
                    int forwardTime = 10000; // 10 seconds forward
                    int newPosition = currentPosition + forwardTime;
                    if (newPosition <= duration) {
                        mediaPlayer.seekTo(newPosition);
                    }
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    int backwardTime = 10000; // 10 seconds backward
                    int newPosition = currentPosition - backwardTime;
                    if (newPosition >= 0) {
                        mediaPlayer.seekTo(newPosition);
                    }
                }
            }
        });

        return view;
    }

    private void updateSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                }
                // Repeat every 1000 milliseconds (1 second)
                seekBar.postDelayed(this, 1000);

            }
        });
    }

    private class DownloadMusicTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String apiUrl = params[0];
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    int fileSize = urlConnection.getContentLength();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    // Convert InputStream to String or save the data as needed
                    return saveaudio(in, fileSize);

                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                publishProgress(-1); // Indicate download failure
                Log.e("HTTP", "Error downloading music", e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == -1) {
                Toast.makeText(getContext(), "Something went wrong during download", Toast.LENGTH_LONG).show();
            } else {
                seekBar.setMax(values[0]);
                seekBar.setProgress(values[1]);
            }
        }

        @Override
        protected void onPostExecute(String filepath) {
            super.onPostExecute(filepath);
            if (filepath != null) {
                Toast.makeText(getContext(), "Download complete", Toast.LENGTH_SHORT).show();
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(filepath);
                    mediaPlayer.prepare();
                    updateSeekBar();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private String saveaudio(InputStream inputStream, int fileSize) {
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String filename = filenameset(songlink);
            File file = new File(directory, filename);
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[4096]; // Adjust the buffer size as needed
                int bytesRead;
                int totalRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalRead += bytesRead;
                    publishProgress(fileSize, totalRead);
                }
                outputStream.close();
                inputStream.close();
                Log.d("File", "Saved music: " + file.getAbsolutePath());
            } catch (IOException e) {
                Log.e("File", "Error saving to file", e);
                publishProgress(-1); // Indicate download failure
                return null;
            }
            return file.getAbsolutePath();
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
    }
}
