package com.abhilekh.namovid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.abhilekh.namovid.GameView.screenRationX;
import static com.abhilekh.namovid.GameView.screenRationY;

public class Virus
{
    public int speed=1;
    public boolean wasShot=true;
    int x,y,width,height,virusCounter=1;

    Bitmap virus1, virus2, virus3, virus4, virus5;



    Virus(Resources resources)
    {
        virus1 = BitmapFactory.decodeResource(resources,R.drawable.coronavirus);
        virus2 = BitmapFactory.decodeResource(resources,R.drawable.coronavirus);
        virus3 = BitmapFactory.decodeResource(resources,R.drawable.coronavirus);
        virus4 = BitmapFactory.decodeResource(resources,R.drawable.coronavirus);
        virus5 = BitmapFactory.decodeResource(resources,R.drawable.coronavirus);

        width = virus1.getWidth();
        height = virus1.getHeight();

        width /=6;
        height /=6;

        width = (int) (width * screenRationX);
        height = (int) (width * screenRationY);

        virus1 = Bitmap.createScaledBitmap(virus1,width,height,false);
        virus2 = Bitmap.createScaledBitmap(virus2,width,height,false);
        virus3 = Bitmap.createScaledBitmap(virus3,width,height,false);
        virus4 = Bitmap.createScaledBitmap(virus4,width,height,false);
        virus5 = Bitmap.createScaledBitmap(virus5,width,height,false);

        y = y - height;
    }

    Bitmap getVirus()
    {
        if(virusCounter==1)
        {
            virusCounter++;
            return virus1;
        }
        if(virusCounter==2)
        {
            virusCounter++;
            return virus2;
        }
        if(virusCounter==3)
        {
            virusCounter++;
            return virus3;
        }
        if(virusCounter==4)
        {
            virusCounter++;
            return virus4;
        }
        if(virusCounter==5)
        {
            virusCounter++;
            return virus1;
        }
        virusCounter =1;
        return virus1;
    }

    Rect getCollisionShape()
    {
        return new Rect(x,y,x+width,y+height);
    }
}
