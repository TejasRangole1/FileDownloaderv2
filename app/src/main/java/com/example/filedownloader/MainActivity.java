package com.example.filedownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    class ExampleRunnable implements Runnable {

        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    private static final String TAG = "MainActivity";
    private Button startButton;
    private Button stopButton;
    private volatile boolean stopThread = false;
    private TextView downloadText;
    private int downloadProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        downloadText = findViewById(R.id.downloadText);
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Downloading...");

                downloadText.setVisibility(View.VISIBLE);
            }
        });

        for(downloadProgress = 0; downloadProgress <= 100; downloadProgress += 10) {
            if(stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                        downloadText.setVisibility(View.INVISIBLE);
                        downloadText.setText("");
                    }
                });
                return;
            }
            Log.d(TAG, "Download Progress: " + downloadProgress + "%");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startButton.setText("Downloading...");
                    downloadText.setText("Download Progress: " + downloadProgress + "%");
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
                downloadText.setVisibility(View.INVISIBLE);
                downloadText.setText("");
            }
        });

        return;
    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }

}