package com.fortunetiger.chi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final String URL_TO_LOAD = "https://privatizerbot.space/866a91ec72_Chinese_Fortune_Tiger.html";

    private ImageView imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView3 = findViewById(R.id.imageView3);
        animateImageView();

        new Handler(Looper.getMainLooper()).postDelayed(() -> checkWebsiteStatus(), 3000);
    }

    private void animateImageView() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView3, "translationX", 600f);
        animation.setDuration(3500);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.start();
    }

    private void checkWebsiteStatus() {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(URL_TO_LOAD);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == 404) {
                    startMainActivity();
                } else {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    String responseBody = convertStreamToString(in);
                    if (responseBody.contains("404")) {
                        startMainActivity();
                    } else {
                        startIfraActivity();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException occurred", e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startIfraActivity() {
        Intent intent = new Intent(SplashActivity.this, IfraActivity.class);
        intent.putExtra("URL", URL_TO_LOAD);
        startActivity(intent);
        finish();
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
