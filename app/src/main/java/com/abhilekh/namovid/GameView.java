package com.abhilekh.namovid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;

    private boolean isPlaying, isGameOver = false;

    private int screenx, screeny,score=0;

    private BackGround backGround1;

    private BackGround backGround2;

    private SoundPool soundPool;

    private Paint paint;

    public static float screenRationX, screenRationY;

    private List<Bullet> bullets;

    private Virus[] virusList;

    private Flight flight;

    private Random random;

    private SharedPreferences preferences;

    private GameActivity activity;

    private int sound;

    public GameView(GameActivity gameActivity, int x , int y) {
        super(gameActivity);

        preferences= gameActivity.getSharedPreferences("game",Context.MODE_PRIVATE);

        this.activity = gameActivity;
        this.screenx = x;
        this.screeny = y;
        backGround1 = new BackGround(x, y, getResources());
        backGround2 = new BackGround(x, y ,getResources());
        backGround2.x = x;

        screenRationX=1920f/screenx;
        screenRationY=1920f/screeny;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();

        }
        else
        {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }

        sound = soundPool.load(activity,R.raw.shoot,1);

        flight = new Flight(this,screeny,getResources());

        bullets = new ArrayList<>();
        virusList = new Virus[5];

        paint = new Paint();

        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        random = new Random();

        for(int i=0; i<5;i++)
        {
            Virus virus = new Virus(getResources());
            virusList[i]=virus;
        }
    }

    @Override
    public void run()
    {
        while(isPlaying)
        {
            update();
            draw();
            sleep();
        }
    }

    public void resume()
    {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause()
    {
        isPlaying= false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update()
    {
        backGround1.x-=10*screenRationX;
        backGround2.x-=10*screenRationX;

        if(backGround1.x+backGround1.background.getWidth()<0)
        {
            backGround1.x = screenx;
        }

        if(backGround2.x+backGround2.background.getWidth()<0)
        {
            backGround2.x = screenx;
        }

        if(flight.isGoingUp)
            flight.y -=  30*screenRationY;
        else
            flight.y += 30 * screenRationY;

        if(flight.y<0)
            flight.y=0;

        if(flight.y>screeny - flight.height)
            flight.y= screeny-flight.height;

        List<Bullet> trash = new ArrayList<>();
        for(Bullet bullet : bullets)
        {
            if(bullet.x>screenx)
            {
                trash.add(bullet);
            }

            bullet.x += 50*screenRationX;

            for(Virus virus : virusList)
            {
                if(Rect.intersects(virus.getCollisionShape(),bullet.getCollisionShape()))
                {
                    score++;
                    virus.x=-500;
                    bullet.x= screenx+500;
                    virus.wasShot=true;

                }
            }
        }

        for(Bullet bullet : trash)
        {
            bullets.remove(bullet);
        }

        for(Virus virus : virusList)
        {
            virus.x -= virus.speed;
            if(virus.x + virus.width <0)
            {

                if(!virus.wasShot)
                {
                    isGameOver = true;
                    return;
                }
                int bound = (int) (30 * screenRationX);
                virus.speed = random.nextInt(bound);

                if(virus.speed<0.01 * screenRationX)
                {
                    virus.speed = (int) (0.01*screenRationX);
                }

                virus.x = screenx;
                virus.y = random.nextInt(screeny - virus.height);

                virus.wasShot = false;
            }

            if(Rect.intersects(virus.getCollisionShape(),flight.getCollisionShape()))
            {
                isGameOver = true;
            }
        }
    }

    private void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backGround1.background,backGround1.x,backGround1.y,paint);
            canvas.drawBitmap(backGround2.background,backGround2.x,backGround2.y,paint);

            for(Virus virus:virusList)
            {
                canvas.drawBitmap(virus.getVirus(),virus.x,virus.y,paint);
            }

            canvas.drawText(score+"",screenx/2f,164,paint);

            if(isGameOver)
            {
                isPlaying = false;
                canvas.drawBitmap(flight.getDeadModiji(),flight.x,flight.y,paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();

                return;
            }



            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);

            for(Bullet bullet :bullets)
            {
                canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
            }
            getHolder().unlockCanvasAndPost(canvas);

        }
    }

    private void waitBeforeExiting()
    {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity,MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore()
    {
        if(preferences.getInt("highscore",0)<score)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highscore",score);
            editor.apply();
        }
    }

    private void sleep()
    {
        try {
            thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(event.getX() <screenx/2)
                {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp=false;
                if(event.getX()>screenx/2)
                {
                    flight.toShoot++;
                }
        }
        return true;
    }

    public void newBullet()
    {

        if(preferences.getBoolean("isMute",false))
        {
            soundPool.play(sound,1,1,0,0,1);
        }
        Bullet bullet  = new Bullet(getResources());
        bullet.x = flight.x+flight.width;
        bullet.y = flight.y+(flight.height/2);
        bullets.add(bullet);


    }
}
