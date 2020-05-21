package com.abhilekh.namovid;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.abhilekh.namovid.GameView.screenRationX;
import static com.abhilekh.namovid.GameView.screenRationY;

public class Flight
{
    int toShoot=0;
    boolean isGoingUp = false;
    int x,y,width,height, wingcounter = 0;
    Bitmap flight1,flight2,modijiDead;
    private GameView gameView;

     Flight(GameView gameView,int screenY, Resources res) {
         this.gameView = gameView;
        flight1= BitmapFactory.decodeResource(res,R.drawable.modiji1);
        flight2= BitmapFactory.decodeResource(res,R.drawable.modiji2);
        modijiDead = BitmapFactory.decodeResource(res,R.drawable.modijidead);


        width=flight1.getWidth();
        height=flight1.getHeight();

        width/=4;
        height/=4;

        width = (int)(width *  screenRationX);
        height = (int)(height *   screenRationY);

        flight1=Bitmap.createScaledBitmap(flight1,width,height,false);
        flight2=Bitmap.createScaledBitmap(flight2,width,height,false);
        modijiDead = Bitmap.createScaledBitmap(modijiDead,width,height,false);

         y=screenY/2;
         x=(int) (64 * screenRationX);
    }

    Bitmap getFlight()
    {
        if(toShoot!=0)
        {
            toShoot--;
            gameView.newBullet();
        }
        if(wingcounter==0)
        {
            wingcounter++;
            return  flight1;
        }
        wingcounter--;
        return  flight2;
    }

    Rect getCollisionShape()
    {
        return new Rect(x,y,x+width,y+height);
    }

    Bitmap getDeadModiji()
    {
        return modijiDead;
    }
}
