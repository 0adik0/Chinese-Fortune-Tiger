package com.fortunetiger.chi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private ImageView imageView2;
    private TextView textScore, texttime;
    private int score = 0;
    private Handler handler = new Handler();
    private Runnable runnable;
    private static final int GAME_TIME = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        imageView2 = findViewById(R.id.imageView2);
        textScore = findViewById(R.id.textscore);
        texttime = findViewById(R.id.texttime);

        imageView2.setOnClickListener(v -> {
            score++;
            textScore.setText("Score: " + score);
            imageView2.setVisibility(View.INVISIBLE);
        });

        startGame();
    }

    private void startGame() {
        runnable = new Runnable() {
            @Override
            public void run() {
                // Поместить imageView2 в случайное место и сделать его видимым
                Random random = new Random();

                // Получаем размеры экрана для определения максимальных координат
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int maxWidth = size.x - imageView2.getWidth();
                int maxHeight = size.y - imageView2.getHeight();

                float dx = random.nextInt(maxWidth);
                float dy = random.nextInt(maxHeight);

                // Установка координат для imageView
                imageView2.setX(dx);
                imageView2.setY(dy);
                imageView2.setVisibility(View.VISIBLE);

                // Повторять каждые 500 мс
                handler.postDelayed(this, 500);
            }
        };

        // Запуск runnable с задержкой
        handler.postDelayed(runnable, 500);

        // Запуск таймера на 60 секунд
        new CountDownTimer(GAME_TIME, 1000) {

            public void onTick(long millisUntilFinished) {
                // Обновить секунды до конца игры
                textScore.setText("Score: " + score);
                texttime.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                imageView2.setVisibility(View.INVISIBLE);
                handler.removeCallbacks(runnable);
                showGameOverDialog();
            }

            private void showGameOverDialog() {
                final Dialog dialog = new Dialog(GameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);

                LayoutInflater inflater = GameActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_custom1, null);
                dialog.setContentView(dialogView);

                // Настройка размеров диалогового окна
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.horizontalMargin = convertDpToPx();

                ImageView restartButton = dialogView.findViewById(R.id.imageView5);
                ImageView mainMenuButton = dialogView.findViewById(R.id.imageView8);

                restartButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    startGame();
                });

                mainMenuButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });

                dialog.getWindow().setAttributes(layoutParams);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

            private int convertDpToPx() {
                return (int) (20 * getResources().getDisplayMetrics().density);
            }
        }.start();
    }
}