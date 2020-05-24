package com.abhilekh.namovid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BackGround
{
    int x=0, y=0;
    Bitmap background;

    public BackGround(int x, int y, Resources resources) {
       background= BitmapFactory.decodeResource(resources,R.drawable.germs);
       background= Bitmap.createScaledBitmap(background,x,y,false);
    }
}
