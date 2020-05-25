package com.abhilekh.namovid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static boolean isMute = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });

        TextView highScore = findViewById(R.id.highScore);

        final SharedPreferences sharedPreferences = getSharedPreferences("game", MODE_PRIVATE);
        highScore.setText("HighScore : " + sharedPreferences.getInt("highscore", 0));

        isMute = sharedPreferences.getBoolean("isMute", false);

        final ImageView volumeControl = findViewById(R.id.volumeCtrl);

        if (isMute) {
            volumeControl.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            volumeControl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }

        volumeControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;

                if (isMute) {
                    volumeControl.setImageResource(R.drawable.ic_volume_up_black_24dp);
                } else {
                    volumeControl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}
