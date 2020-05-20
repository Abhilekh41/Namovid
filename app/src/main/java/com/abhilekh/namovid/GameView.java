package com.abhilekh.namovid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;

    private boolean isPlaying;

    private int screenx, screeny;

    private BackGround backGround1;

    private BackGround backGround2;

    private Paint paint;

    private float screenRationX, screenRationY;

    public GameView(Context context, int x , int y) {
        super(context);

        this.screenx = x;
        this.screeny = y;
        backGround1 = new BackGround(x, y, getResources());
        backGround2 = new BackGround(x, y ,getResources());
        backGround2.x = x;

        screenRationX=1920f/screenx;
        screenRationY=1920f/screeny;
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
        backGround2.x-=10*screenRationY;

        if(backGround1.x+backGround1.background.getWidth()<0)
        {
            backGround1.x = screenx;
        }

        if(backGround2.x+backGround2.background.getWidth()<0)
        {
            backGround2.x = screenx;
        }
    }

    private void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(backGround1.background,backGround1.x,backGround1.y,paint);
            canvas.drawBitmap(backGround2.background,backGround2.x,backGround2.y,paint);

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
}