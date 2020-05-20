package com.abhilekh.namovid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;

    private boolean isPlaying;

    private int screenx, screeny;

    private BackGround backGround1;

    private BackGround backGround2;

    private Paint paint;

    public static float screenRationX, screenRationY;

    private List<Bullet> bullets;

    private Flight flight;

    public GameView(Context context, int x , int y) {
        super(context);

        this.screenx = x;
        this.screeny = y;
        backGround1 = new BackGround(x, y, getResources());
        backGround2 = new BackGround(x, y ,getResources());
        backGround2.x = x;

        screenRationX=1920f/screenx;
        screenRationY=1920f/screeny;


        flight = new Flight(this,screeny,getResources());

        bullets = new ArrayList<>();
        paint = new Paint();
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
        }

        for(Bullet bullet : trash)
        {
            bullets.remove(bullet);
        }
    }

    private void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backGround1.background,backGround1.x,backGround1.y,paint);
            canvas.drawBitmap(backGround2.background,backGround2.x,backGround2.y,paint);

            canvas.drawBitmap(flight.getFlight(),flight.x,flight.y,paint);

            for(Bullet bullet :bullets)
            {
                canvas.drawBitmap(bullet.bullet,bullet.x,bullet.y,paint);
            }
            getHolder().unlockCanvasAndPost(canvas);

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
        Bullet bullet  = new Bullet(getResources());
        bullet.x = flight.x+flight.width;
        bullet.y = flight.y+(flight.height/2);
        bullets.add(bullet);
    }
}
