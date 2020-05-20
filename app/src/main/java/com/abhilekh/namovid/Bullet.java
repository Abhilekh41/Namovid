package com.abhilekh.namovid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.abhilekh.namovid.GameView.screenRationX;
import static com.abhilekh.namovid.GameView.screenRationY;

public class Bullet
{
    int x,y;
    Bitmap bullet;

    Bullet(Resources res)
    {
        bullet = BitmapFactory.decodeResource(res,R.drawable.bullet);

        int width=bullet.getWidth();
        int height=bullet.getHeight();

        width/=4;
        height/=4;


        width = (int)(width *  screenRationX);
        height = (int)(height *   screenRationY);

        bullet = Bitmap.createScaledBitmap(bullet,width,height,false);

    }
}
