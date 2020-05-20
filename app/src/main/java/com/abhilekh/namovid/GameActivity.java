package com.abhilekh.namovid;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Point point  = new Point();

       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       getWindowManager().getDefaultDisplay().getSize(point);

       gameView = new GameView(this,point.x,point.y);
       setContentView(gameView);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        gameView.resume();
    }
}
